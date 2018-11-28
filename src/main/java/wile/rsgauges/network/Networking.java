package wile.rsgauges.network;

import wile.rsgauges.ModConfig;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.client.OverlayEventHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import io.netty.buffer.ByteBuf;

public class Networking
{
  private static SimpleNetworkWrapper netw;

  private static void preInitCommon()
  {
    netw = NetworkRegistry.INSTANCE.newSimpleChannel(ModRsGauges.MODID+"_a");
    netw.registerMessage(OverlayTextMessage.NetEventHandler.class, OverlayTextMessage.class, OverlayTextMessage.MESSAGE_ID, Side.SERVER);
    netw.registerMessage(OverlayTextMessage.NetEventHandler.class, OverlayTextMessage.class, OverlayTextMessage.MESSAGE_ID, Side.CLIENT);
  }

  public static void preInitServer()
  { if(!ModConfig.zmisc.without_switch_status_overlay) preInitCommon(); }

  public static void preInitClient()
  { if(!ModConfig.zmisc.without_switch_status_overlay) preInitCommon(); }

  /**
   * Simple string message that may contain localisation patterns for
   * client side localisation.
   */
  public static class OverlayTextMessage implements IMessage
  {
    public static final int DISPLAY_TIME_MS = 3000;
    public static final byte MESSAGE_ID = 44;
    private TextComponentTranslation data_;
    private TextComponentTranslation data() { return data_; }

    public OverlayTextMessage()
    { data_ = new TextComponentTranslation("[unset]"); }

    public OverlayTextMessage(final TextComponentTranslation tct)
    { data_ = tct.createCopy(); }

    /**
     * Sends a OverlayStringMessage from the server to a player.
     */
    public static void sendToClient(EntityPlayerMP player, TextComponentTranslation message)
    { netw.sendTo(new OverlayTextMessage(message), player); }

    /**
     * Message parsing
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
      try {
        data_ = (TextComponentTranslation)ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
      } catch(Throwable e) {
        data_= new TextComponentTranslation("[incorrect translation]");
      }
    }

    /**
     * Message composition
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
      try {
        ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(data()));
      } catch(Throwable e) {
        ModRsGauges.logger.error("OverlayTextMessage.toBytes() failed: " + e.toString());
      }
    }

    /**
     * Message handler for this message
     */
    public static class NetEventHandler implements IMessageHandler<OverlayTextMessage, IMessage>
    {
      /**
       * Message event
       */
      @Override
      public IMessage onMessage(OverlayTextMessage message, MessageContext ctx)
      { if(ctx.side == Side.SERVER) { server(message.data()); } else { client(message.data()); } return null; }

      /**
       * Server handler for these messages
       */
      @SideOnly(Side.SERVER)
      private static void server(TextComponentTranslation message)
      {} // currently no channel to the server needed.

      /**
       * Client handler for these messages
       */
      @SideOnly(Side.CLIENT)
      private static void client(TextComponentTranslation message)
      {
        Minecraft minecraft = Minecraft.getMinecraft();
        //minecraft.addScheduledTask(new Runnable() { @Override public void run(){OverlayEventHandler.show(message, DISPLAY_TIME_MS);} });
        minecraft.addScheduledTask(() -> OverlayEventHandler.show(message, DISPLAY_TIME_MS));
      }
    }
  }
}
