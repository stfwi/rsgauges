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

import net.minecraft.world.server.ServerWorld;
import net.minecraft.block.BlockState;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.SwitchLink;
import java.util.Random;
import javax.annotation.Nullable;


public class BistableSwitchBlock extends SwitchBlock
{
  public BistableSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public BistableSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  public BistableSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBB)
  { super(config, properties, unrotatedBB, unrotatedBB, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
  {}

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new SwitchTileEntity(ModContent.TET_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public SwitchLink.RequestResult switchLinkTrigger(SwitchLink link)
  {
    World world = link.world;
    BlockPos pos = link.target_position;
    SwitchTileEntity te = getTe(world, pos);
    if((te==null) || (!te.verifySwitchLinkTarget(link))) return SwitchLink.RequestResult.REJECTED;
    return onSwitchActivated(world, pos, world.getBlockState(pos), link.player, null) ? (SwitchLink.RequestResult.OK) : (SwitchLink.RequestResult.REJECTED);
  }

}
