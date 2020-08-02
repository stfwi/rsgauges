/*
 * @file BlockPowerPlant.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Specialised plant-like contact switch.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.detail.ModResources;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import javax.annotation.Nullable;

public class BlockPowerPlant extends BlockContactSwitch
{
  public BlockPowerPlant(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public BlockPowerPlant(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }
}
