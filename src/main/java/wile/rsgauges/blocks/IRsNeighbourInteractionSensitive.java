/*
 * @file IRsNeighbourInteractionSensitive.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Switch sensitive to interactions one block away.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRsNeighbourInteractionSensitive
{
  default boolean onNeighborBlockPlayerInteraction(World world, BlockPos pos, BlockState state, BlockPos fromPos, LivingEntity entity, Hand hand, boolean isLeftClick)
  { return false; }
}
