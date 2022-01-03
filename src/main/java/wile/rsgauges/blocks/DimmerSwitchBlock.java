/*
 * @file DimmerSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;


import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Overlay;

import javax.annotation.Nullable;
import java.util.Random;


public class DimmerSwitchBlock extends SwitchBlock
{
  public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

  // -------------------------------------------------------------------------------------------------------------------

  public DimmerSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config|0xff, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random)
  {}

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); builder.add(POWER); }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
  {
    if((!(state.getBlock() instanceof DimmerSwitchBlock))) return InteractionResult.FAIL;
    if(world.isClientSide()) return InteractionResult.SUCCESS;
    SwitchTileEntity te = getTe(world, pos);
    if(te==null) return InteractionResult.FAIL;
    te.click_config(null, false);
    ClickInteraction ck = ClickInteraction.get(state, world, pos, player, hand, hit);
    boolean was_powered = te.setpower()!=0;
    if(ck.touch_configured) {
      int p = (int)ck.y;
      if(p != te.setpower()) {
        te.setpower(p);
        p = te.setpower();
        Overlay.show(player,
          Auxiliaries.localizable("switchconfig.dimmerswitch.output_power", ChatFormatting.RED, new Object[]{p})
        );
        final int state_p = state.getValue(POWER);
        if(state_p!=p) {
          world.setBlock(pos, state.setValue(POWER, p).setValue(POWERED, p>0), 1|2|8|16);
          notifyNeighbours(world, pos, state, te, false);
          te.setChanged();
        }
        if(was_powered && (p==0)) power_off_sound.play(world, pos); else power_on_sound.play(world, pos);
        if((state_p!=p) && ((config & SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0))  {
          if(!te.activateSwitchLinks(p, (p>0)?15:0, (state_p==0)!=(p==0))) {
            ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
          }
        }
      }
    } else if(ck.wrenched) {
      if(te.click_config(this, false)) {
        Overlay.show(player, te.configStatusTextComponentTranslation((SwitchBlock) state.getBlock()));
      }
    } else if(ck.item== ModContent.SWITCH_LINK_PEARL) {
      attack(state, world, pos, player);
    }
    return InteractionResult.CONSUME;
  }
}
