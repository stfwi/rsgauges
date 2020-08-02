/*
 * @file BlockBistableKnockSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Seismic mass based adjacent block "knock" detection activate.
 */
package wile.rsgauges.blocks;

import net.minecraft.util.EnumFacing;
import wile.rsgauges.detail.ModResources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.util.math.*;
import javax.annotation.Nullable;

public class BlockKnockPulseSwitch extends BlockSwitch implements IRsNeighbourInteractionSensitive
{

  public BlockKnockPulseSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound, @Nullable Material material)
  { super(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config|SWITCH_CONFIG_PULSE, powerOnSound, powerOffSound, material);
  }

  public BlockKnockPulseSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config, powerOnSound, powerOffSound, null); }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean onNeighborBlockPlayerInteraction(World world, BlockPos pos, IBlockState state, BlockPos fromPos, EntityLivingBase entity, EnumHand hand, boolean isLeftClick)
  {
    EnumFacing facing = state.getValue(BlockSwitch.FACING);
    if(!pos.offset(facing).equals(fromPos)) return false;
    onSwitchActivated(world, pos, state, null, facing);
    return false;
  }
}
