/*
 * @file IRsNeighbourInteractionSensitive.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Switch sensitive to interactions one block away.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRsNeighbourInteractionSensitive
{
  boolean onNeighborBlockPlayerInteraction(World world, BlockPos pos, IBlockState state, BlockPos fromPos, EntityLivingBase entity, EnumHand hand, boolean isLeftClick);
}
