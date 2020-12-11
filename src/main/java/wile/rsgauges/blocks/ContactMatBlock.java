/*
 * @file ContactMatBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Specialised floor mounted contact switch.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import wile.rsgauges.detail.ModResources;
import javax.annotation.Nullable;

public class ContactMatBlock extends ContactSwitchBlock
{
  public ContactMatBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public ContactMatBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }
}
