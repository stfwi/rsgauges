/*
 * @file PlayerBlockInteraction.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2020 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Encapsulates Player interaction events with blocks
 */
package wile.rsgauges.libmc.detail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.EventPriority;
import org.apache.logging.log4j.Logger;


public class PlayerBlockInteraction
{
  public interface INeighbourBlockInteractionSensitive
  {
    default boolean onNeighborBlockPlayerInteraction(Level world, BlockPos pos, BlockState state, BlockPos fromPos, LivingEntity entity, InteractionHand hand, boolean isLeftClick)
    { return false; }
  }

  public static void init(String modid, Logger logger)
  {
    MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,false, PlayerInteractEvent.class, PlayerBlockInteraction::onPlayerInteract);
  }

  public static void onPlayerInteract(PlayerInteractEvent event)
  {
    final Level world = event.getLevel();
    if(world.isClientSide()) return;
    final boolean is_rclick = (event instanceof RightClickBlock) && (event.getHand()==InteractionHand.MAIN_HAND);
    final boolean is_lclick = (event instanceof LeftClickBlock) && (event.getHand()==InteractionHand.MAIN_HAND) && (event.getFace()!= Direction.DOWN); // last one temporary workaround for double trigger on mouse release
    if((!is_rclick) && (!is_lclick)) return;
    final BlockPos fromPos = event.getPos();
    for(Direction facing: Direction.values()) {
      if(event.getFace() == facing) continue;
      final BlockPos pos = fromPos.relative(facing);
      final BlockState state = event.getLevel().getBlockState(pos);
      if(!((state.getBlock()) instanceof INeighbourBlockInteractionSensitive)) continue;
      if(((INeighbourBlockInteractionSensitive)state.getBlock()).onNeighborBlockPlayerInteraction(world, pos, state, fromPos, event.getEntity(), event.getHand(), is_lclick)) {
        event.setCancellationResult(InteractionResult.CONSUME);
      }
    }
  }

}