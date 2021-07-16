/*
 * @file PlayerBlockInteraction.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2020 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Encapsulates Player interaction events with blocks
 */
package wile.rsgauges.libmc.detail;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    default boolean onNeighborBlockPlayerInteraction(World world, BlockPos pos, BlockState state, BlockPos fromPos, LivingEntity entity, Hand hand, boolean isLeftClick)
    { return false; }
  }

  public static void init(String modid, Logger logger)
  {
    MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,false, PlayerInteractEvent.class, PlayerBlockInteraction::onPlayerInteract);
  }

  public static void onPlayerInteract(PlayerInteractEvent event)
  {
    final World world = event.getWorld();
    if(world.isClientSide()) return;
    final boolean is_rclick = (event instanceof RightClickBlock) && (event.getHand()==Hand.MAIN_HAND);
    final boolean is_lclick = (event instanceof LeftClickBlock) && (event.getHand()==Hand.MAIN_HAND) && (event.getFace()!=Direction.DOWN); // last one temporary workaround for double trigger on mouse release
    if((!is_rclick) && (!is_lclick)) return;
    final BlockPos fromPos = event.getPos();
    for(Direction facing: Direction.values()) {
      if(event.getFace() == facing) continue;
      final BlockPos pos = fromPos.relative(facing);
      final BlockState state = event.getWorld().getBlockState(pos);
      if(!((state.getBlock()) instanceof INeighbourBlockInteractionSensitive)) continue;
      if(((INeighbourBlockInteractionSensitive)state.getBlock()).onNeighborBlockPlayerInteraction(world, pos, state, fromPos, event.getEntityLiving(), event.getHand(), is_lclick)) {
        event.setCancellationResult(ActionResultType.CONSUME);
      }
    }
  }


}