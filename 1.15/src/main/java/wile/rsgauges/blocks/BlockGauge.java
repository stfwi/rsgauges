/*
 * @file BlockGauge.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing a redstone signal
 * display, measuring the redstone signal of the block
 * it is attached to (only the back not the sides or front).
 * The block has a "tickable" tile entity to ensure that
 * the gauge display is updated even if a block update event
 * was lost. Depending on the model/type additional constants
 * like power-to-light scaling are implemented, e.g. for LED
 * indicators.
 *
 */
package wile.rsgauges.blocks;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.ModContent;
import wile.rsgauges.ModConfig;
import wile.rsgauges.detail.DevUtils;
import wile.rsgauges.detail.ModColors;
import wile.rsgauges.detail.ModResources;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import javax.annotation.Nullable;


public class BlockGauge extends RsDirectedBlock implements ModColors.ColorTintSupport
{
  public static final long GAUGE_DATA_POWER_MASK            = 0x000000000000000fl;
  public static final int  GAUGE_DATA_POWER_SHIFT           = 0;
  public static final long GAUGE_DATA_COLOR_MASK            = 0x00000000000000f0l;
  public static final int  GAUGE_DATA_COLOR_SHIFT           = 4;
  public static final long GAUGE_DATA_BLINKING              = 0x0000000000000100l;
  //
  public static final long GAUGE_CONFIG_COLOR_TINT_SUPPORT  = RSBLOCK_CONFIG_COLOR_TINT_SUPPORT;

  public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
  @Nullable public final ModResources.BlockSoundEvent power_on_sound;
  @Nullable public final ModResources.BlockSoundEvent power_off_sound;

  // -------------------------------------------------------------------------------------------------------------------

  public BlockGauge(long config, Block.Properties props, final AxisAlignedBB aabb, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, props, aabb);
    power_on_sound = powerOnSound;
    power_off_sound = powerOffSound;
  }

  public BlockGauge(long config, Block.Properties props, final AxisAlignedBB aabb)
  { this(config, props, aabb, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected void neighborUpdated(BlockState state, World world, BlockPos pos, BlockPos facingPos)
  {
    if(!isAffectedByNeigbour(state, world, pos, facingPos)) return;
    if(world.isRemote) return;
    final TileEntityGauge te = getTe(world, pos);
    if(te == null) te.reset_timer();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
  { world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 1); }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  {
    if(!world.isRemote()) DevUtils.blockActivateHook.hook(state, world, pos, player, hand, hit);
    return ActionResultType.SUCCESS;
  }

  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
  {
    if(world.isRemote) return;
    DevUtils.blockClickHook.hook(state, world, pos, player);
    final TileEntityGauge te = getTe(world, pos);
    final ItemStack item = player.getHeldItemMainhand();
    if((te==null) || (item==null)) return;
    //    if((DyeUtils.isDye(item)) && ((config & GAUGE_CONFIG_COLOR_TINT_SUPPORT) != 0) && (!ModConfig.without_color_tinting)) {
    //      int dye = DyeUtils.rawMetaFromStack(item);
    //      te.color_tint((dye<0)?(0):((dye>15)?15:dye));
    //      te.markDirty();
    //      world.markAndNotifyBlock(pos, null, state, state, 1|2|4|16);
    //      ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.tinting", null, new Object[]{ModAuxiliaries.localizable("switchconfig.tinting." + ModAuxiliaries.DyeColorFilters.nameByIndex[dye & 0xf], null)}));
    //    }
  }

  @Override
  public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side)
  { return false; }

  @Override
  public boolean getWeakChanges(BlockState state, IWorldReader world, BlockPos pos)
  { return true; }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
  { return ((state.getBlock() instanceof BlockGauge) && (side == state.get(FACING).getOpposite())); }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWER); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (getDefaultState().has(BlockGauge.POWER)) ? (state.with(BlockGauge.POWER, 0)) : (state);
  }

  @Override
  public boolean hasTileEntity(BlockState state)
  { return true; }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new TileEntityGauge(ModContent.TET_GAUGE); }

  // -------------------------------------------------------------------------------------------------------------------
  // Gauge specific block methods
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean isWallMount()
  { return true; }

  @Override
  public boolean isCube()
  { return false; }

  @Override
  public boolean isLateral()
  { return false; }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public TileEntityGauge getTe(IWorldReader world, BlockPos pos)
  {
    final TileEntity te = world.getTileEntity(pos);
    return (te instanceof TileEntityGauge) ? ((TileEntityGauge)te) : (null);
  }

  /**
   * Tile entity to update the gauge block frequently.
   */
  public static class TileEntityGauge extends RsBlock.RsTileEntity implements ITickableTileEntity
  {
    private boolean alternation_state_ = false; // client, mainly sound indicators
    private long trigger_timer_ = 0;
    private long scd_ = 0;

    public TileEntityGauge(TileEntityType<?> te_type)
    { super(te_type); }

    public TileEntityGauge()
    { super(ModContent.TET_GAUGE); }

    public int power()
    { return (int)((scd_ & GAUGE_DATA_POWER_MASK) >> GAUGE_DATA_POWER_SHIFT); }

    @Override
    public int color_tint()
    { return (int)((scd_ & GAUGE_DATA_COLOR_MASK) >> GAUGE_DATA_COLOR_SHIFT); }

    public void power(int p)
    { scd_ =  (scd_ & (~GAUGE_DATA_POWER_MASK)) | ((((p<=0)?(0):((p>15)?15:p)) << GAUGE_DATA_POWER_SHIFT) & GAUGE_DATA_POWER_MASK); }

    public void color_tint(int p)
    { scd_ =  (scd_ & (~GAUGE_DATA_COLOR_MASK)) | ((((p<=0)?(0):((p>15)?15:p)) << GAUGE_DATA_COLOR_SHIFT) & GAUGE_DATA_COLOR_MASK); }

    public void reset_timer()
    { trigger_timer_= 0; }

    public void reset()
    { reset(getWorld()); }

    public void reset(IWorldReader world)
    {
      trigger_timer_= 0;
      if(world == null) {
        scd_ = 0;
      } else {
        try {
          final long current_scd = scd_;
          scd_ = (int) ((((BlockGauge) (world.getBlockState(getPos()).getBlock())).config));
          if(current_scd != scd_) markDirty();
        } catch(Exception e) {
          scd_ = 0; // ok, the default then
        }
      }
    }

    @Override
    public void writeNbt(CompoundNBT nbt, boolean updatePacket)
    { nbt.putLong("scd", scd_); }

    @Override
    public void readNbt(CompoundNBT nbt, boolean updatePacket)
    { scd_= nbt.getLong("scd"); }

    @Override
    @SuppressWarnings("deprecation")
    public void tick()
    {
      if(--trigger_timer_ > 0) return;
      trigger_timer_ = ModConfig.gauge_update_interval;
      try {
        BlockState state = getBlockState();
        final BlockGauge block = (BlockGauge) state.getBlock();
        if(world.isRemote) {
          if(((block.config & GAUGE_DATA_BLINKING) != 0) && (block instanceof BlockIndicator)) {
            if(state.get(BlockIndicator.POWERED)) {
              if((block.power_off_sound != null) || (block.power_on_sound != null)) {
                final boolean alternation = (System.currentTimeMillis() & 1024) < 512;
                if(alternation != alternation_state_ ) {
                  alternation_state_ = alternation;
                  if(alternation && (block.power_on_sound != null)) {
                    block.power_on_sound.play(world, pos);
                  } else if(!alternation && (block.power_off_sound != null)) {
                    block.power_off_sound.play(world, pos);
                  }
                }
              }
            }
          }
        } else {
          final BlockPos neighbourPos = pos.offset((Direction) state.get(BlockGauge.FACING), -1);
          if(!world.isBlockLoaded(neighbourPos)) return;
          final BlockState neighborState = world.getBlockState(neighbourPos);
          int p = 0;
          if((block instanceof BlockIndicator) && (world.isBlockPowered(getPos()))) {
            p = 15;
          } else if(neighborState.canProvidePower()) {
            p = world.getRedstonePower(neighbourPos, state.get(FACING).getOpposite());
          } else if(neighborState.hasComparatorInputOverride()) {
            p = neighborState.getComparatorInputOverride(world, neighbourPos);
          } else {
            final boolean is_indicator = (block instanceof BlockIndicator);
            for(Direction nbf : Direction.values()) {
              if((p >= 15) || (is_indicator && (p>0))) break;
              final BlockPos nbp = neighbourPos.offset(nbf);
              if(!world.isBlockLoaded(nbp)) continue;
              final BlockState nbs = world.getBlockState(nbp);
              p = Math.max(p, world.getRedstonePower(nbp, nbf));
            }
          }
          final boolean sync = (power() != p);
          if((block.config & GAUGE_DATA_BLINKING) == 0) {
            if((block.power_on_sound != null) && (power() == 0) && (p > 0)) {
              block.power_on_sound.play(world, pos);
            } else if((block.power_off_sound != null) && (power() > 0) && (p == 0)) {
              block.power_off_sound.play(world, pos);
            }
          }
          power(p);
          if(block instanceof BlockIndicator) {
            boolean powered = p != 0; // TE also used for indicator, no need to register yet another tile entity.
            if(state.get(BlockIndicator.POWERED) != powered) world.setBlockState(pos, state.with(BlockIndicator.POWERED, powered), 1|2|16); // |32
          } else {
            if((state.get(POWER) != p)) world.setBlockState(pos, state.with(POWER, p), 1|2|16); // |32
          }
        }
      } catch(Throwable e) {
        trigger_timer_ = 100;
        ModRsGauges.logger().error("TE update() failed: " + e);
      }
    }
  }
}
