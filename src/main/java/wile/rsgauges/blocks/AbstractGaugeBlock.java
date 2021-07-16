/*
 * @file AbstractGaugeBlock.java
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

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.ModContent;
import wile.rsgauges.ModConfig;
import wile.rsgauges.detail.SwitchLink;
import wile.rsgauges.detail.SwitchLink.LinkMode;
import wile.rsgauges.detail.SwitchLink.RequestResult;
import wile.rsgauges.items.SwitchLinkPearlItem;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.libmc.detail.Overlay;

import javax.annotation.Nullable;
import java.util.Optional;


public class AbstractGaugeBlock extends RsDirectedBlock implements SwitchLink.ISwitchLinkable
{
  public static final long GAUGE_DATA_POWER_MASK            = 0x000000000000000fl;
  public static final int  GAUGE_DATA_POWER_SHIFT           = 0;
  public static final long GAUGE_DATA_BLINKING              = 0x0000000000000100l;
  public static final long GAUGE_DATA_INVERTED              = 0x0000000000000200l;
  public static final long GAUGE_DATA_COMPARATOR_MODE       = 0x0000000000000800l;

  @Nullable public final ModResources.BlockSoundEvent power_on_sound;
  @Nullable public final ModResources.BlockSoundEvent power_off_sound;

  // -------------------------------------------------------------------------------------------------------------------

  public AbstractGaugeBlock(long config, AbstractBlock.Properties props, final AxisAlignedBB aabb, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, props, aabb, null); power_on_sound = powerOnSound; power_off_sound = powerOffSound; }

  public AbstractGaugeBlock(long config, AbstractBlock.Properties props, final AxisAlignedBB aabb)
  { this(config, props, aabb, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
  {
    if((world.isClientSide()) || (!isAffectedByNeigbour(state, world, pos, fromPos))) return;
    final GaugeTileEntity te = getTe(world, pos);
    if(te == null) te.reset_timer();
  }

  @Override
  public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
  { world.getBlockTicks().scheduleTick(pos, state.getBlock(), 1); }

  @Override
  public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  {
    if(world.isClientSide()) return ActionResultType.SUCCESS;
    ItemStack stack_held = player.getItemInHand(hand);
    if(ModConfig.isWrench(stack_held)) {
      GaugeTileEntity te = getTe(world, pos);
      if(te==null) return ActionResultType.CONSUME;
      te.on_wrench(state, world, pos, player, player.getItemInHand(hand));
    } else if((stack_held.getItem() == Items.ENDER_PEARL) || (stack_held.getItem() == ModContent.SWITCH_LINK_PEARL)) {
      attack(state, world, pos, player);
    }
    return ActionResultType.CONSUME;
  }

  @Override
  @SuppressWarnings("deprecation")
  public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player)
  {
    final ItemStack item_held = player.inventory.getSelected();
    if(item_held.getItem() == Items.ENDER_PEARL) {
      if(ModConfig.without_switch_linking) return;
      ItemStack link_stack = SwitchLinkPearlItem.createFromPearl(world, pos, player);
      if(link_stack.isEmpty()) {
        Overlay.show(player, Auxiliaries.localizable("switchlinking.target_assign.error_notarget"));
        ModResources.BlockSoundEvents.SWITCHLINK_CANNOT_LINK_THAT.play(world, pos);
      } else {
        player.inventory.setItem(player.inventory.selected, link_stack);
        Overlay.show(player, Auxiliaries.localizable("switchlinking.target_assign.ok"));
        ModResources.BlockSoundEvents.SWITCHLINK_LINK_TARGET_SELECTED.play(world, pos);
      }
    } else if(item_held.getItem() == ModContent.SWITCH_LINK_PEARL) {
      if(ModConfig.without_switch_linking) return;
      if(SwitchLinkPearlItem.cycleLinkMode(item_held, world, pos, true)) {
        Overlay.show(player, Auxiliaries.localizable("switchlinking.relayconfig.confval" + Integer.toString(SwitchLink.fromItemStack(item_held).mode().index())));
      } else {
        Overlay.show(player, Auxiliaries.localizable("switchlinking.source_assign.error_nosource"));
      }
    }
  }

  @Override
  public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side)
  { return false; }

  @Override
  public boolean getWeakChanges(BlockState state, IWorldReader world, BlockPos pos)
  { return true; }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
  { return ((state.getBlock() instanceof GaugeBlock) && (side == state.getValue(FACING).getOpposite())); }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? (null) : ((state.hasProperty(GaugeBlock.POWER)) ? (state.setValue(GaugeBlock.POWER, 0)) : (state));
  }

  @Override
  public boolean hasTileEntity(BlockState state)
  { return true; }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new GaugeTileEntity(ModContent.TET_GAUGE); }

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
  // Linking
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean switchLinkHasTargetSupport(World world, BlockPos pos)
  { return true; }

  @Override
  public boolean switchLinkHasSourceSupport(World world, BlockPos pos)
  { return false; }

  @Override
  public boolean switchLinkHasAnalogSupport(World world, BlockPos pos)
  { return true; }

  @Override
  public ImmutableList<LinkMode> switchLinkGetSupportedTargetModes()
  { return ImmutableList.of(LinkMode.AS_STATE, LinkMode.INV_STATE); }

  @Override
  public Optional<Integer> switchLinkOutputPower(World world, BlockPos pos)
  {
    GaugeTileEntity te = getTe(world, pos);
    if(te==null) return Optional.empty();
    return Optional.of(te.power());
  }

  @Override
  public Optional<Integer> switchLinkInputPower(World world, BlockPos pos)
  { return Optional.empty(); }

  @Override
  public Optional<Integer> switchLinkComparatorInput(World world, BlockPos pos)
  { return Optional.empty(); }

  @Override
  public SwitchLink.RequestResult switchLinkTrigger(SwitchLink link)
  {
    GaugeTileEntity te = getTe(link.world, link.target_position);
    if(te==null) return RequestResult.TARGET_GONE;
    te.switchlink_input(link.source_analog_power);
    return RequestResult.OK;
  }

  @Override
  public void switchLinkInit(SwitchLink link)
  {
    GaugeTileEntity te = getTe(link.world, link.target_position);
    if(te!=null) te.switchlink_input(link.source_analog_power);
  }

  @Override
  public void switchLinkUnlink(SwitchLink link)
  {
    GaugeTileEntity te = getTe(link.world, link.target_position);
    if(te!=null) te.switchlink_input(0);
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public GaugeTileEntity getTe(IWorldReader world, BlockPos pos)
  {
    final TileEntity te = world.getBlockEntity(pos);
    return (te instanceof GaugeTileEntity) ? ((GaugeTileEntity)te) : (null);
  }

  /**
   * Tile entity to update the gauge block frequently.
   */
  public static class GaugeTileEntity extends RsBlock.RsTileEntity implements ITickableTileEntity
  {
    private boolean alternation_state_ = false; // client, mainly sound indicators
    private long trigger_timer_ = 0;
    private long scd_ = 0;
    private long last_wrench_click_ = 0;
    private int switchlink_input_ = 0;

    public GaugeTileEntity(TileEntityType<?> te_type)
    { super(te_type); }

    public GaugeTileEntity()
    { super(ModContent.TET_GAUGE); }

    public int power()
    { return (int)((scd_ & GAUGE_DATA_POWER_MASK) >> GAUGE_DATA_POWER_SHIFT); }

    public void power(int p)
    { scd_ =  (scd_ & (~GAUGE_DATA_POWER_MASK)) | ((((p<=0)?(0):(Math.min(p, 15))) << GAUGE_DATA_POWER_SHIFT) & GAUGE_DATA_POWER_MASK); }

    public boolean inverted()
    { return (scd_ & GAUGE_DATA_INVERTED) != 0; }

    public void inverted(boolean i)
    { scd_ =  (scd_ & (~GAUGE_DATA_INVERTED)) | (i ? GAUGE_DATA_INVERTED : 0); }

    public int switchlink_input()
    { return switchlink_input_; }

    public void switchlink_input(int power)
    { switchlink_input_ = power; trigger_timer_ = Math.min(trigger_timer_, 1); }

    public boolean comparator_mode()
    { return (scd_ & GAUGE_DATA_COMPARATOR_MODE) != 0; }

    public void comparator_mode(boolean i)
    { scd_ =  (scd_ & (~GAUGE_DATA_COMPARATOR_MODE)) | (i ? GAUGE_DATA_COMPARATOR_MODE : 0); }

    public void reset_timer()
    { trigger_timer_= 0; }

    public void reset()
    { reset(getLevel()); }

    public void reset(IWorldReader world)
    {
      trigger_timer_= 0;
      if(world == null) {
        scd_ = 0;
      } else {
        try {
          final long current_scd = scd_;
          scd_ = (int) ((((GaugeBlock) (world.getBlockState(getBlockPos()).getBlock())).config));
          if(current_scd != scd_) setChanged();
        } catch(Exception e) {
          scd_ = 0; // ok, the default then
        }
      }
    }

    public void on_wrench(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack wrench)
    {
      long t = world.getGameTime();
      if(Math.abs(t-last_wrench_click_) < 40) {
        switch((inverted()?1:0)|(comparator_mode()?2:0)) {
          case  0: inverted( true); comparator_mode(false); break;
          case  1: inverted(false); comparator_mode( true); break;
          case  2: inverted( true); comparator_mode( true); break;
          default: inverted(false); comparator_mode(false); break;
        }
      }
      last_wrench_click_ = t;
      final TranslationTextComponent tr = Auxiliaries.localizable("gaugeconfig.options."+(inverted()?"inverted":"notinverted"), TextFormatting.DARK_AQUA);
      if(comparator_mode()) tr.append(new StringTextComponent(" | ")).append(Auxiliaries.localizable("gaugeconfig.options.comparator", TextFormatting.DARK_AQUA));
      Overlay.show(player, tr);
    }

    @Override
    public void write(CompoundNBT nbt, boolean updatePacket)
    { nbt.putLong("scd", scd_); nbt.putInt("lnkinp", switchlink_input_); }

    @Override
    public void read(CompoundNBT nbt, boolean updatePacket)
    { scd_= nbt.getLong("scd"); switchlink_input_ = nbt.getInt("lnkinp"); }

    @Override
    @SuppressWarnings("deprecation")
    public void tick()
    {
      if(--trigger_timer_ > 0) return;
      trigger_timer_ = ModConfig.gauge_update_interval;
      try {
        BlockState state = getBlockState();
        final AbstractGaugeBlock block = (AbstractGaugeBlock) state.getBlock();
        if(level.isClientSide()) {
          if(((block.config & GAUGE_DATA_BLINKING) != 0) && (block instanceof IndicatorBlock)) {
            if(state.getValue(IndicatorBlock.POWERED)) {
              if((block.power_off_sound != null) || (block.power_on_sound != null)) {
                final boolean alternation = (System.currentTimeMillis() & 1024) < 512;
                if(alternation != alternation_state_ ) {
                  alternation_state_ = alternation;
                  if(alternation && (block.power_on_sound != null)) {
                    block.power_on_sound.play(level, worldPosition);
                  } else if(!alternation && (block.power_off_sound != null)) {
                    block.power_off_sound.play(level, worldPosition);
                  }
                }
              }
            }
          }
        } else {
          final BlockPos neighbourPos = worldPosition.relative((Direction) state.getValue(GaugeBlock.FACING), -1);
          if(!level.hasChunkAt(neighbourPos)) return;
          final BlockState neighborState = level.getBlockState(neighbourPos);
          int p = 0;
          if(comparator_mode()) {
            // Explicit comparator-only mode
            if(neighborState.hasAnalogOutputSignal()) {
              p = neighborState.getAnalogOutputSignal(level, neighbourPos);
            }
          } else {
            // Direct or indirect redstonr mode, automatic comparator mode if applicable
            if((block instanceof IndicatorBlock) && (level.hasNeighborSignal(getBlockPos()))) {
              p = 15;
            } else if(neighborState.isSignalSource()) {
              p = level.getSignal(neighbourPos, state.getValue(FACING).getOpposite());
            } else if(neighborState.hasAnalogOutputSignal()) {
              p = neighborState.getAnalogOutputSignal(level, neighbourPos);
            } else {
              final boolean is_indicator = (block instanceof IndicatorBlock);
              for(Direction nbf : Direction.values()) {
                if((p >= 15) || (is_indicator && (p>0))) break;
                final BlockPos nbp = neighbourPos.relative(nbf);
                if(!level.hasChunkAt(nbp)) continue;
                final BlockState nbs = level.getBlockState(nbp);
                p = Math.max(p, level.getSignal(nbp, nbf));
              }
            }
          }
          if(inverted()) p = MathHelper.clamp(15-p, 0, 15);
          final boolean sync = (power() != p);
          if((block.config & GAUGE_DATA_BLINKING) == 0) {
            if((block.power_on_sound != null) && (power() == 0) && (p > 0)) {
              block.power_on_sound.play(level, worldPosition);
            } else if((block.power_off_sound != null) && (power() > 0) && (p == 0)) {
              block.power_off_sound.play(level, worldPosition);
            }
          }
          p = Math.max(p, switchlink_input_);
          power(p);
          if(block instanceof IndicatorBlock) {
            boolean powered = p != 0; // TE also used for indicator, no need to register yet another tile entity.
            if(state.getValue(IndicatorBlock.POWERED) != powered) level.setBlock(worldPosition, state.setValue(IndicatorBlock.POWERED, powered), 1|2|16); // |32
          } else if(block instanceof GaugeBlock) {
            if((state.getValue(GaugeBlock.POWER) != p)) level.setBlock(worldPosition, state.setValue(GaugeBlock.POWER, p), 1|2|8|16); // |32
          }
        }
      } catch(Throwable e) {
        trigger_timer_ = 100;
        ModRsGauges.logger().error("TE update() failed: " + e);
      }
    }
  }
}
