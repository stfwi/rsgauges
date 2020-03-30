/*
 * @file BlockBistableKnockSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Seismic mass based adjacent block "knock" detection activate.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import wile.rsgauges.detail.ModResources;
import net.minecraft.block.Block;
import net.minecraft.util.math.*;
import javax.annotation.Nullable;

public class BlockKnockBistableSwitch extends BlockBistableSwitch implements IRsNeighbourInteractionSensitive
{
  public BlockKnockBistableSwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  @Override
  public boolean isCube()
  { return true; }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean onNeighborBlockPlayerInteraction(World world, BlockPos pos, BlockState state, BlockPos fromPos, LivingEntity entity, Hand hand, boolean isLeftClick)
  {
    Direction facing = state.get(BlockSwitch.FACING);
    if(!pos.offset(facing).equals(fromPos)) return false;
    onSwitchActivated(world, pos, state, null, facing);
    return false;
  }
}
