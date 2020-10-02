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

import net.minecraft.util.ActionResultType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wile.rsgauges.libmc.detail.Overlay;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.SwitchLinkPearlItem;

import javax.annotation.Nullable;
import java.util.Random;

public class DimmerSwitchBlock extends SwitchBlock
{
  public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

  // -------------------------------------------------------------------------------------------------------------------

  public DimmerSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config|0xff, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
  {}

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWER); }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  {
    if((!(state.getBlock() instanceof DimmerSwitchBlock))) return ActionResultType.FAIL;
    if(world.isRemote) return ActionResultType.SUCCESS;
    SwitchTileEntity te = getTe(world, pos);
    if(te==null) return ActionResultType.FAIL;
    te.click_config(null, false);
    ClickInteraction ck = ClickInteraction.get(state, world, pos, player, hand, hit);
    boolean was_powered = te.on_power()!=0;
    if(ck.touch_configured) {
      int p = (int)ck.y;
      if(p != te.on_power()) {
        te.on_power(p);
        p = te.on_power();
        Overlay.show(player,
          Auxiliaries.localizable("switchconfig.dimmerswitch.output_power", TextFormatting.RED, new Object[]{p})
        );
        final int state_p = state.get(POWER);
        if(state_p!=p) {
          world.setBlockState(pos, state.with(POWER, p).with(POWERED, p>0), 1|2|8|16);
          notifyNeighbours(world, pos, state, te, false);
          te.markDirty();
        }
        if(was_powered && (p==0)) power_off_sound.play(world, pos); else power_on_sound.play(world, pos);
        if(((was_powered) == (p==0)) && (((config & SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0)) ) {
          if(!was_powered) {
            // Fire link requests when changing from unpowered to powered.
            if(!te.activateSwitchLinks(SwitchLinkPearlItem.SwitchLink.SWITCHLINK_RELAY_ACTIVATE)) {
              ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
            }
          }
        }
      }
      return ActionResultType.SUCCESS;
    } else if(ck.wrenched) {
      if(te.click_config(this, false)) {
        Overlay.show(player, te.configStatusTextComponentTranslation((SwitchBlock) state.getBlock()));
      }
    }
    return ActionResultType.SUCCESS;
  }
}
