/**
 * @file GaugeTileEntity.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Tile entity class for the GaugeBlock. Implements ITickable
 * with update (sample) timer to prevent unneeded performance
 * overhead. Only active on the client (currently used only for
 * display purposes). Frequently invokes `GaugeBlock.updateBlock()`.
 *
**/

package wile.rsgauges.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wile.rsgauges.Config;

public class GaugeTileEntity extends RsTileEntity<GaugeBlock> implements ITickable {

  private long trigger_time = 0;

  @Override
  public void update() {
    if(!hasWorld() || (!getWorld().isRemote)) return;
    final long t = world.getTotalWorldTime();
    if(t < trigger_time) return;
    trigger_time += 40; // in case of early returns on error
    if(!getWorld().isBlockLoaded(getPos())) return;
    final IBlockState state = world.getBlockState(getPos());
    if(state == null) return;
    final Block block = state.getBlock();
    if(!(block instanceof GaugeBlock)) return;
    final BlockPos targetPos = pos.offset((EnumFacing)state.getValue(GaugeBlock.FACING), -1);
    int T = ((GaugeBlock)block).getAlternationTimeBase();
    if(T <= 2) T = Config.getGaugeUpdateInterval();
    trigger_time = (((t + T - 1) / T) + 1) * T;
    GaugeBlock.updateBlock((GaugeBlock)block, state, world, getPos(), targetPos, 16); // no bit1|bit2 = don't send updates
  }
}
