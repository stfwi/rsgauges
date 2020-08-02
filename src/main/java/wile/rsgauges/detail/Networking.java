/*
 * @file Networking.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main client/server message handling.
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModRsGauges;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.network.NetworkDirection;
import java.util.function.Supplier;


public class Networking
{
  private static final String PROTOCOL = "1";

  private static final SimpleChannel DEFAULT_CHANNEL = NetworkRegistry.ChannelBuilder
    .named(new ResourceLocation(ModRsGauges.MODID, "default_ch"))
    .clientAcceptedVersions(PROTOCOL::equals).serverAcceptedVersions(PROTOCOL::equals).networkProtocolVersion(() -> PROTOCOL)
    .simpleChannel();

  public static void init()
  {
    int discr = -1;
    DEFAULT_CHANNEL.registerMessage(++discr, PacketTileNotifyClientToServer.class, PacketTileNotifyClientToServer::compose, PacketTileNotifyClientToServer::parse, PacketTileNotifyClientToServer.Handler::handle);
    DEFAULT_CHANNEL.registerMessage(++discr, PacketTileNotifyServerToClient.class, PacketTileNotifyServerToClient::compose, PacketTileNotifyServerToClient::parse, PacketTileNotifyServerToClient.Handler::handle);
    DEFAULT_CHANNEL.registerMessage(++discr, OverlayTextMessage.class, OverlayTextMessage::compose, OverlayTextMessage::parse, OverlayTextMessage.Handler::handle);
  }

  //--------------------------------------------------------------------------------------------------------------------
  // Tile entity notifications
  //--------------------------------------------------------------------------------------------------------------------

  public interface IPacketTileNotifyReceiver
  {
    default void onServerPacketReceived(CompoundNBT nbt) {}
    default void onClientPacketReceived(PlayerEntity player, CompoundNBT nbt) {}
  }

  public static class PacketTileNotifyClientToServer
  {
    CompoundNBT nbt = null;
    BlockPos pos = BlockPos.ZERO;

    public static void sendToServer(BlockPos pos, CompoundNBT nbt)
    { if((pos!=null) && (nbt!=null)) DEFAULT_CHANNEL.sendToServer(new PacketTileNotifyClientToServer(pos, nbt)); }

    public static void sendToServer(TileEntity te, CompoundNBT nbt)
    { if((te!=null) && (nbt!=null)) DEFAULT_CHANNEL.sendToServer(new PacketTileNotifyClientToServer(te, nbt)); }

    public PacketTileNotifyClientToServer()
    {}

    public PacketTileNotifyClientToServer(BlockPos pos, CompoundNBT nbt)
    { this.nbt = nbt; this.pos = pos; }

    public PacketTileNotifyClientToServer(TileEntity te, CompoundNBT nbt)
    { this.nbt = nbt; pos = te.getPos(); }

    public static PacketTileNotifyClientToServer parse(final PacketBuffer buf)
    { return new PacketTileNotifyClientToServer(buf.readBlockPos(), buf.readCompoundTag()); }

    public static void compose(final PacketTileNotifyClientToServer pkt, final PacketBuffer buf)
    { buf.writeBlockPos(pkt.pos); buf.writeCompoundTag(pkt.nbt); }

    public static class Handler
    {
      public static void handle(final PacketTileNotifyClientToServer pkt, final Supplier<NetworkEvent.Context> ctx)
      {
        ctx.get().enqueueWork(() -> {
          PlayerEntity player = ctx.get().getSender();
          World world = player.world;
          if(world == null) return;
          final TileEntity te = world.getTileEntity(pkt.pos);
          if(!(te instanceof IPacketTileNotifyReceiver)) return;
          ((IPacketTileNotifyReceiver)te).onClientPacketReceived(ctx.get().getSender(), pkt.nbt);
        });
        ctx.get().setPacketHandled(true);
      }
    }
  }

  public static class PacketTileNotifyServerToClient
  {
    CompoundNBT nbt = null;
    BlockPos pos = BlockPos.ZERO;

    public static void sendToPlayer(PlayerEntity player, TileEntity te, CompoundNBT nbt)
    {
      if((!(player instanceof ServerPlayerEntity)) || (player instanceof FakePlayer) || (te==null) || (nbt==null)) return;
      DEFAULT_CHANNEL.sendTo(new PacketTileNotifyServerToClient(te, nbt), ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public PacketTileNotifyServerToClient()
    {}

    public PacketTileNotifyServerToClient(BlockPos pos, CompoundNBT nbt)
    { this.nbt=nbt; this.pos=pos; }

    public PacketTileNotifyServerToClient(TileEntity te, CompoundNBT nbt)
    { this.nbt=nbt; pos=te.getPos(); }

    public static PacketTileNotifyServerToClient parse(final PacketBuffer buf)
    { return new PacketTileNotifyServerToClient(buf.readBlockPos(), buf.readCompoundTag()); }

    public static void compose(final PacketTileNotifyServerToClient pkt, final PacketBuffer buf)
    { buf.writeBlockPos(pkt.pos); buf.writeCompoundTag(pkt.nbt); }

    public static class Handler
    {
      public static void handle(final PacketTileNotifyServerToClient pkt, final Supplier<NetworkEvent.Context> ctx)
      {
        ctx.get().enqueueWork(() -> {
          World world = ModRsGauges.proxy.getWorldClientSide();
          if(world == null) return;
          final TileEntity te = world.getTileEntity(pkt.pos);
          if(!(te instanceof IPacketTileNotifyReceiver)) return;
          ((IPacketTileNotifyReceiver)te).onServerPacketReceived(pkt.nbt);
        });
        ctx.get().setPacketHandled(true);
      }
    }
  }

  //--------------------------------------------------------------------------------------------------------------------
  // Main window GUI text message
  //--------------------------------------------------------------------------------------------------------------------

  /**
   * Simple string message that may contain localisation patterns for
   * client side localisation.
   */
  public static class OverlayTextMessage
  {
    public static final int DISPLAY_TIME_MS = 3000;
    public static final byte MESSAGE_ID = 44;
    private ITextComponent data_;
    private ITextComponent data() { return data_; }

    public static void sendToPlayer(PlayerEntity player, ITextComponent message)
    {
      if((!(player instanceof ServerPlayerEntity)) || (player instanceof FakePlayer)) return;
      DEFAULT_CHANNEL.sendTo(new OverlayTextMessage(message), ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public OverlayTextMessage()
    { data_ = new TranslationTextComponent("[unset]"); }

    public OverlayTextMessage(final ITextComponent tct)
    { data_ = (ITextComponent)tct.deepCopy(); }

    public static OverlayTextMessage parse(final PacketBuffer buf)
    {
      try {
        return new OverlayTextMessage((ITextComponent)buf.readTextComponent());
      } catch(Throwable e) {
        return new OverlayTextMessage(new TranslationTextComponent("[incorrect translation]"));
      }
    }

    public static void compose(final OverlayTextMessage pkt, final PacketBuffer buf)
    {
      try {
        buf.writeTextComponent(pkt.data());
      } catch(Throwable e) {
        ModRsGauges.logger().error("OverlayTextMessage.toBytes() failed: " + e.toString());
      }
    }

    public static class Handler
    {
      public static void handle(final OverlayTextMessage pkt, final Supplier<NetworkEvent.Context> ctx)
      {
        ctx.get().enqueueWork(() -> OverlayEventHandler.show(pkt.data(), DISPLAY_TIME_MS));
        ctx.get().setPacketHandled(true);
      }
    }
  }
}
