/**
 * @file RsTileEntity.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Base class for tile entities used in this mod.
 * Implements basic encapsulation of NBT data I/O
 * by providing overridable methods `writeNbt()` and
 * `readNbt()`.
**/
package wile.rsgauges.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RsTileEntity<BlockType extends RsBlock> extends TileEntity {

  private static final int NBT_ENTITY_TYPE = 1; // @todo: no idea yet what effect this value actually has -> double check

  public void writeNbt(NBTTagCompound nbt, boolean updatePacket) {} // overridden if NBT is needed
  public void readNbt(NBTTagCompound nbt, boolean updatePacket)  {} // overridden if NBT is needed

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState os, IBlockState ns) {
    return (os.getBlock() != ns.getBlock()) || (!(os.getBlock() instanceof RsBlock)) || (!(ns.getBlock() instanceof RsBlock));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    this.writeNbt(nbt, false);
    return nbt;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    this.readNbt(nbt, false);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    super.readFromNBT(pkt.getNbtCompound());
    this.readNbt(pkt.getNbtCompound(), true);
    super.onDataPacket(net, pkt);
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    NBTTagCompound nbt = new NBTTagCompound();
    super.writeToNBT(nbt);
    this.writeNbt(nbt, true);
    return nbt;
  }

  @Override // Only on server.
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(pos, NBT_ENTITY_TYPE, getUpdateTag());
  }

  @Override
  public void handleUpdateTag(NBTTagCompound tag) {
    this.readFromNBT(tag);
  }

  @Override
  public void onLoad() {}

}
