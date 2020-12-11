/*
 * @file PowerPlantBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Specialised plant-like contact switch.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import wile.rsgauges.detail.ModResources;
import javax.annotation.Nullable;

public class PowerPlantBlock extends ContactSwitchBlock
{
  public PowerPlantBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public PowerPlantBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }
}
