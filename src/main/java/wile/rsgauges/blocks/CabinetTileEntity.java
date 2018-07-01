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

public class CabinetTileEntity extends RsTileEntity<CabinetBlock> implements ITickable {

  private long trigger_time = 0;

  @Override
  public void update() {
    if(!hasWorld() || (!getWorld().isRemote)) return;
  }
}
