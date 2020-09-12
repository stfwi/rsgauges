/*
 * @file BlockAutoSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;

import net.minecraft.world.server.ServerWorld;
import wile.rsgauges.blocks.IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.ItemSwitchLinkPearl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.IWorldReader;
import net.minecraft.tileentity.TileEntity;
import javax.annotation.Nullable;
import java.util.Random;


public abstract class AutoSwitchBlock extends SwitchBlock
{
  public AutoSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public AutoSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
  {}

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean onLinkRequest(final ItemSwitchLinkPearl.SwitchLink link, long req, final World world, final BlockPos pos, @Nullable final PlayerEntity player)
  {
    if((world==null) || ((config & (SWITCH_CONFIG_LINK_TARGET_SUPPORT))==0) || (world.isRemote)) return false;
    if((config & (SWITCH_CONFIG_TIMER_INTERVAL))==0) return false; // only interval timer can be a link target
    BlockState state = world.getBlockState(pos);
    if((state == null) || (!(state.getBlock() instanceof AutoSwitchBlock))) return false;
    TileEntityAutoSwitch te = getTe(world, pos);
    if((te==null) || (!te.verifySwitchLinkTarget(link))) return false;
    te.updateSwitchState(state, this, !state.get(POWERED), 0);
    return true;
  }

  @Override
  public TileEntityAutoSwitch getTe(IWorldReader world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof TileEntityAutoSwitch))) return null;
    return (TileEntityAutoSwitch)te;
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile Entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity base
   */
  public static abstract class TileEntityAutoSwitch extends SwitchTileEntity
  {
    public TileEntityAutoSwitch(TileEntityType<?> te_type)
    { super(te_type); }

    protected final void updateSwitchState(BlockState state, AutoSwitchBlock block, boolean active, int hold_time)
    {
      if(active) {
        on_timer_reset(hold_time);
        if(!state.get(POWERED)) {
          if(this instanceof IntervalTimerSwitchTileEntity) ((IntervalTimerSwitchTileEntity)this).restart();
          world.setBlockState(pos, (state.with(POWERED, true)), 1|2|8|16);
          block.power_on_sound.play(world, pos);
          world.notifyNeighborsOfStateChange(pos, block);
          BlockPos np = pos.offset(state.get(FACING).getOpposite());
          Block nb = world.getBlockState(np).getBlock();
          world.notifyNeighborsOfStateChange(np, nb);
          if((block.config & SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
            if(!activateSwitchLinks(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_ACTIVATE)) {
              ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
            }
          }
        }
      } else if(state.get(POWERED)) {
        if((hold_time<=0) || (on_time_remaining() <= 0)) {
          world.setBlockState(pos, (state.with(POWERED, false)), 1|2|8|16);
          block.power_off_sound.play(world, pos);
          world.notifyNeighborsOfStateChange(pos, block);
          BlockPos np = pos.offset(state.get(FACING).getOpposite());
          Block nb = world.getBlockState(np).getBlock();
          world.notifyNeighborsOfStateChange(np, nb);
          if((block.config & SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
            if(!activateSwitchLinks(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_DEACTIVATE)) {
              ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
            }
          }
        }
      }
    }
  }

}
