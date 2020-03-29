package wile.rsgauges.detail;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class DevUtils
{
  public interface IBlockClickedHook { void hook(BlockState state, World world, BlockPos pos, PlayerEntity player); }
  public interface IBlockActivateHook { void hook(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit); }
  public static IBlockClickedHook blockClickHook = (state, world, pos, player)->{};
  public static IBlockActivateHook blockActivateHook = (state, world, pos, player, hand, hit)->{};
}
