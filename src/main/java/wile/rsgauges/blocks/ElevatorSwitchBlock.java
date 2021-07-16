/*
 * @file ElevatorSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2020 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Elevator switches with up/down/up-down arrows depending
 * on placement.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import wile.rsgauges.detail.ModResources;

import javax.annotation.Nullable;


public class ElevatorSwitchBlock extends PulseSwitchBlock
{
  public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);

  public ElevatorSwitchBlock(long config, AbstractBlock.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound);
    registerDefaultState(super.defaultBlockState().setValue(VARIANT, 0));
  }

  public ElevatorSwitchBlock(long config, AbstractBlock.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { this(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); builder.add(VARIANT); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    BlockState state = super.getStateForPlacement(context);
    if(state==null) return null;
    int variant = 0;
    if(context.getClickedFace().getAxis().isHorizontal()) {
      int y = (int)Math.round(context.getClickLocation().subtract(Vector3d.atCenterOf(context.getClickedPos())).scale(16).y());
      if(y >  4) variant = 2;
      if(y < -4) variant = 1;
    }
    return state.setValue(VARIANT, variant);
  }

}
