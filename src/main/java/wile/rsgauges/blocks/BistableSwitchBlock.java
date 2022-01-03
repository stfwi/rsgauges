/*
 * @file BistableSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.SwitchLink;

import javax.annotation.Nullable;
import java.util.Random;


public class BistableSwitchBlock extends SwitchBlock
{
  public BistableSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public BistableSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  public BistableSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBB)
  { super(config, properties, unrotatedBB, unrotatedBB, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random)
  {}

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return new SwitchTileEntity(pos, state); }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public SwitchLink.RequestResult switchLinkTrigger(SwitchLink link)
  {
    Level world = link.world;
    BlockPos pos = link.target_position;
    SwitchTileEntity te = getTe(world, pos);
    if((te==null) || (!te.verifySwitchLinkTarget(link))) return SwitchLink.RequestResult.REJECTED;
    return onSwitchActivated(world, pos, world.getBlockState(pos), link.player, null) ? (SwitchLink.RequestResult.OK) : (SwitchLink.RequestResult.REJECTED);
  }

}
