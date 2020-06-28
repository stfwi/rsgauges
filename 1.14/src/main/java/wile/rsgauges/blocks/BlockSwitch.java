/*
 * @file BlockSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;

import net.minecraft.world.*;
import wile.rsgauges.ModContent;
import wile.rsgauges.ModConfig;
import wile.rsgauges.detail.ModColors;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.ItemSwitchLinkPearl;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.INBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.*;
import java.util.ArrayList;
import java.util.Random;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class BlockSwitch extends RsDirectedBlock implements ModColors.ColorTintSupport
{
  // -- Entity stored changable state data.
  public static final long SWITCH_DATA_POWERED_POWER_MASK       = 0x000000000000000fl;
  public static final long SWITCH_DATA_UNPOWERED_POWER_MASK     = 0x00000000000000f0l;
  public static final long SWITCH_DATA_INVERTED                 = 0x0000000000000100l;
  public static final long SWITCH_DATA_WEAK                     = 0x0000000000000200l;
  public static final long SWITCH_DATA_NOOUTPUT                 = 0x0000000000000400l;
  // -- Full block multiside output control
  public static final long SWITCH_DATA_SIDE_ENABLED_BOTTOM      = 0x0000000000001000l;
  public static final long SWITCH_DATA_SIDE_ENABLED_TOP         = 0x0000000000002000l;
  public static final long SWITCH_DATA_SIDE_ENABLED_FRONT       = 0x0000000000004000l;
  public static final long SWITCH_DATA_SIDE_ENABLED_BACK        = 0x0000000000008000l;
  public static final long SWITCH_DATA_SIDE_ENABLED_LEFT        = 0x0000000000010000l;
  public static final long SWITCH_DATA_SIDE_ENABLED_RIGHT       = 0x0000000000020000l;
  public static final long SWITCH_DATA_SIDE_ENABLED_ALL         = 0x000000000003f000l;
  public static final long SWITCH_DATA_SIDE_ENABLED_MASK        = SWITCH_DATA_SIDE_ENABLED_ALL;
  public static final long SWITCH_DATA_SIDE_ENABLED_SHIFT       = 12;
  public static final long SWITCH_DATA_ENTITY_DEFAULTS_MASK     = 0x000000000003ffffl;
  // -- Constant construction time switch config
  public static final long SWITCH_CONFIG_INVERTABLE             = 0x0000000000100000l;
  public static final long SWITCH_CONFIG_WEAKABLE               = 0x0000000000200000l;
  public static final long SWITCH_CONFIG_PULSETIME_CONFIGURABLE = 0x0000000000400000l;
  public static final long SWITCH_CONFIG_TOUCH_CONFIGURABLE     = 0x0000000000800000l;
  public static final long SWITCH_CONFIG_PULSE_EXTENDABLE       = 0x0000000001000000l;
  public static final long SWITCH_CONFIG_LCLICK_RESETTABLE      = 0x0000000002000000l;
  public static final long SWITCH_CONFIG_BISTABLE               = 0x0000000010000000l;
  public static final long SWITCH_CONFIG_PULSE                  = 0x0000000020000000l;
  public static final long SWITCH_CONFIG_CONTACT                = 0x0000000040000000l;
  public static final long SWITCH_CONFIG_TIMER_DAYTIME          = 0x0000000100000000l; // uses environmental tile entity due to slow update rate
  public static final long SWITCH_CONFIG_TIMER_INTERVAL         = 0x0000000200000000l;
  public static final long SWITCH_CONFIG_SENSOR_VOLUME          = 0x0000000400000000l;
  public static final long SWITCH_CONFIG_SENSOR_LINEAR          = 0x0000000800000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHT           = 0x0000001000000000l;
  public static final long SWITCH_CONFIG_SENSOR_RAIN            = 0x0000002000000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHTNING       = 0x0000004000000000l;
  public static final long SWITCH_CONFIG_SENSOR_BLOCKDETECT     = 0x0000008000000000l;
  public static final long SWITCH_CONFIG_SENSOR_TIME            = SWITCH_CONFIG_TIMER_INTERVAL;
  public static final long SWITCH_CONFIG_SENSOR_DETECTOR        = SWITCH_CONFIG_SENSOR_VOLUME|SWITCH_CONFIG_SENSOR_LINEAR;
  public static final long SWITCH_CONFIG_SENSOR_ENVIRONMENTAL   = SWITCH_CONFIG_SENSOR_LIGHT|SWITCH_CONFIG_TIMER_DAYTIME|SWITCH_CONFIG_SENSOR_RAIN|SWITCH_CONFIG_SENSOR_LIGHTNING;
  public static final long SWITCH_CONFIG_AUTOMATIC              = SWITCH_CONFIG_SENSOR_TIME|SWITCH_CONFIG_SENSOR_DETECTOR|SWITCH_CONFIG_SENSOR_ENVIRONMENTAL|SWITCH_CONFIG_SENSOR_BLOCKDETECT;
  public static final long SWITCH_CONFIG_WALLMOUNT              = 0x0000010000000000l;
  public static final long SWITCH_CONFIG_LATERAL                = 0x0000020000000000l;
  public static final long SWITCH_CONFIG_LATERAL_WALLMOUNT      = SWITCH_CONFIG_WALLMOUNT|SWITCH_CONFIG_LATERAL;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_ON    = 0x0000100000000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_OFF   = 0x0000200000000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE       = SWITCH_CONFIG_PROJECTILE_SENSE_ON|SWITCH_CONFIG_PROJECTILE_SENSE_OFF;
  public static final long SWITCH_CONFIG_SHOCK_SENSITIVE        = 0x0000400000000000l;
  public static final long SWITCH_CONFIG_HIGH_SENSITIVE         = 0x0000800000000000l;
  public static final long SWITCH_CONFIG_TRANSLUCENT            = RSBLOCK_CONFIG_TRANSLUCENT;
  public static final long SWITCH_CONFIG_COLOR_TINT_SUPPORT     = RSBLOCK_CONFIG_COLOR_TINT_SUPPORT;
  public static final long SWITCH_CONFIG_NOT_PASSABLE           = 0x0010000000000000l;
  public static final long SWITCH_CONFIG_SIDES_CONFIGURABLE     = 0x0040000000000000l;
  public static final long SWITCH_CONFIG_LINK_SOURCE_SUPPORT    = 0x0100000000000000l;
  public static final long SWITCH_CONFIG_LINK_TARGET_SUPPORT    = 0x0200000000000000l;
  public static final long SWITCH_CONFIG_LINK_RELAY             = 0x0400000000000000l;
  // Entity stored changable state value data.
  public static final int SWITCH_DATA_SVD_ACTIVE_TIME_MASK      = 0x000000ff;
  public static final int SWITCH_DATA_SVD_COLOR_MASK            = 0x00000f00;
  public static final int SWITCH_DATA_SVD_COLOR_SHIFT           = 8;

  // -------------------------------------------------------------------------------------------------------------------

  public static final BooleanProperty POWERED = BooleanProperty.create("powered");

  public static final int base_tick_rate = 2;
  public static final int default_pulse_on_time = 20;
  public final long config;
  protected final AxisAlignedBB unrotated_bb_powered;
  protected final ModResources.BlockSoundEvent power_on_sound;
  protected final ModResources.BlockSoundEvent power_off_sound;

  // -------------------------------------------------------------------------------------------------------------------

  public BlockSwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, properties, unrotatedBBUnpowered);
    if((powerOnSound==null) && (powerOffSound==null)) {
      powerOnSound  = ModResources.BlockSoundEvents.DEFAULT_SWITCH_ACTIVATION;
      powerOffSound = ModResources.BlockSoundEvents.DEFAULT_SWITCH_DEACTIVATION;
    } else {
      if(powerOnSound==null) powerOnSound =  ModResources.BlockSoundEvents.DEFAULT_SWITCH_MUTE;
      if(powerOffSound==null) powerOffSound = ModResources.BlockSoundEvents.DEFAULT_SWITCH_MUTE;
    }
    unrotated_bb_powered = (unrotatedBBPowered!=null) ? unrotatedBBPowered : unrotatedBBUnpowered;
    power_on_sound = powerOnSound;
    power_off_sound = powerOffSound;
    setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(POWERED, false));
    if((config & 0xff)==0xff) config &= ~0xffL; // explicitly initialise on and off power with 0.
    else if((config & 0xff)==0) config |= 15; // default power=15 if not specified.
    if((config & SWITCH_DATA_SIDE_ENABLED_MASK)!=0) config |= SWITCH_CONFIG_SIDES_CONFIGURABLE; // implicitly set flag if any side config is specified
    this.config = config;
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWERED); }

  @OnlyIn(Dist.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer()
  { return ((config & SWITCH_CONFIG_TRANSLUCENT) != 0) ? (BlockRenderLayer.TRANSLUCENT) : (BlockRenderLayer.CUTOUT); }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext)
  { return ((config & SWITCH_CONFIG_NOT_PASSABLE)==0) ? (VoxelShapes.empty()) : (getShape(state, world, pos, selectionContext)); }

  @Override
  @SuppressWarnings("deprecation")
  public boolean canProvidePower(BlockState state)
  { return ((config & SWITCH_CONFIG_LINK_RELAY)==0); }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
  { return (!isWallMount()) && ((side==null) || ((side)==(Direction.UP)) || ((side)==(state.get(FACING)))); }

  @Override
  @SuppressWarnings("deprecation")
  public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
  { return getPower(state, world, pos, side, false); }

  @Override
  @SuppressWarnings("deprecation")
  public int getStrongPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
  { return getPower(state, world, pos, side, true); }

  @Override
  protected boolean isValidPositionOnSide(IWorldReader world, BlockPos pos, Direction side)
  { return super.isValidPositionOnSide(world, pos, side); }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
  {
    final TileEntitySwitch te = getTe(world, pos);
    if(te != null) te.reset(world);
    if(!super.onBlockPlacedByCheck(world, pos, state, placer, stack)) return;
    world.setBlockState(pos, world.getBlockState(pos).with(POWERED, false), 1|2);
    notifyNeighbours(world, pos, state,  te, true);
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
  {
    if(world.isRemote || ((config & SWITCH_CONFIG_PROJECTILE_SENSE)==0) || (!(entity instanceof IProjectile))) return;
    if(state.get(POWERED)) {
      if((config & SWITCH_CONFIG_PROJECTILE_SENSE_OFF)==0) return;
    } else {
      if((config & SWITCH_CONFIG_PROJECTILE_SENSE_ON)==0) return;
    }
    onSwitchActivated(world, pos, state, null, null);
  }

  @Override
  public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  {
    TileEntitySwitch te = getTe(world, pos);
    if((te==null) || world.isRemote) return true;
    te.click_config(null, false); // reset double click tracking
    ClickInteraction ck = ClickInteraction.get(state, world, pos, player, hand, hit);
    if(ck.touch_configured) {
      if(te.activation_config(state, player, ck.x, ck.y)) {
        ModResources.BlockSoundEvents.DEFAULT_SWITCH_CONFIGCLICK.play(world, pos);
      }
      return true;
    } else if(ck.wrenched) {
      if(te.click_config(this, false)) {
        ModResources.BlockSoundEvents.DEFAULT_SWITCH_CONFIGCLICK.play(world, pos);
        ModAuxiliaries.playerStatusMessage(player, te.configStatusTextComponentTranslation((BlockSwitch) state.getBlock()));
      }
      return true;
    } else if(
      (!ModConfig.without_rightclick_item_switchconfig) &&
        (  ((ck.item==Items.REDSTONE) && (((BlockSwitch)state.getBlock()).config & SWITCH_CONFIG_PULSETIME_CONFIGURABLE) != 0)
          || (ck.item==Items.ENDER_PEARL)
          || (ck.item==ModContent.SWITCH_LINK_PEARL)
        )
    ) {
      onBlockClicked(state, world, pos, player);
      return true;
    } else if((config & (SWITCH_CONFIG_BISTABLE|SWITCH_CONFIG_PULSE))==0) {
      return true;
    } else {
      return onSwitchActivated(world, pos, state, player, hit.getFace());
    }
  }

  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
  {
    if(world.isRemote) return;
    final TileEntitySwitch te = getTe(world, pos);
    if(te == null) return;
    final Item item_held = (player.inventory.getCurrentItem()!=null) ? (player.inventory.getCurrentItem().getItem()) : (Items.AIR);
    ClickInteraction ck = ClickInteraction.get(state, world, pos, player);
    if(ck.wrenched) {
      // Switch output config using an accepted wrench with a single click
      if(te.click_config(this, false)) {
        ModAuxiliaries.playerStatusMessage(player, te.configStatusTextComponentTranslation((BlockSwitch) state.getBlock()));
        return;
      }
    } else if((ck.item==Items.REDSTONE) && (((BlockSwitch)state.getBlock()).config & SWITCH_CONFIG_PULSETIME_CONFIGURABLE) != 0) {
      // Exact pulse time configuration
      if(!ModConfig.without_pulsetime_config) {
        te.configured_on_time(ck.item_count * 2); // 0.1s per stack item -> 2 .. 128 ticks off time config
        ModAuxiliaries.playerStatusMessage(player, te.configStatusTextComponentTranslation((BlockSwitch)state.getBlock()));
        return;
      }
    } else if((ck.dye >= 0) && (((BlockSwitch)state.getBlock()).config & SWITCH_CONFIG_COLOR_TINT_SUPPORT) != 0) {
      // Switch color tinting
      if((!ModConfig.without_color_tinting) && (ck.dye <= 15)) {
        te.color_tint(ck.dye);
        world.markAndNotifyBlock(pos, null, state, state, 1|2|4|16);
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.tinting", null, new Object[]{ModAuxiliaries.localizable("switchconfig.tinting." + ModAuxiliaries.DyeColorFilters.nameByIndex[ck.dye & 0xf], null)}));
        return;
      }
    } else if(ck.item==Items.ENDER_PEARL) {
      // Switch link target assignment
      if(!ModConfig.without_switch_linking) {
        final BlockSwitch block = (BlockSwitch)state.getBlock();
        if((block.config & SWITCH_CONFIG_LINK_TARGET_SUPPORT) == 0) {
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.target_assign.error_notarget", null));
          ModResources.BlockSoundEvents.SWITCHLINK_CANNOT_LINK_THAT.play(world, pos);
        } else {
          ItemStack link_stack = ItemSwitchLinkPearl.createFromEnderPearl(world, pos, player);
          if((link_stack==null) || (link_stack.getTag()==null)) {
            ModResources.BlockSoundEvents.SWITCHLINK_CANNOT_LINK_THAT.play(world, pos);
          } else {
            link_stack.getTag().putLong("cdtime", world.getGameTime()); // see E_SELF_ASSIGN branch for SWITCH_LINK_PEARL
            player.inventory.setInventorySlotContents(player.inventory.currentItem, link_stack);
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.target_assign.ok", null));
            ModResources.BlockSoundEvents.SWITCHLINK_LINK_TARGET_SELECTED.play(world, pos);
          }
        }
        return;
      }
    } else if((ck.item==ModContent.SWITCH_LINK_PEARL) && (player.inventory!=null) && (item_held==ModContent.SWITCH_LINK_PEARL)) {
      // Link config at source switch or assignemnt of target switch
      if(!ModConfig.without_switch_linking) {
        switch(te.assign_switchlink(world, pos, player.inventory.getCurrentItem())) {
          case OK:
            // Link target was assigned to ths switch
            player.inventory.getCurrentItem().shrink(1);
            ModResources.BlockSoundEvents.SWITCHLINK_LINK_SOURCE_SELECTED.play(world, pos);
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.source_assign.ok", null));
            return;
          case E_SELF_ASSIGN: {
            // Multi-left-clicking a target switch will reconfigure the pearl for that switch.
            // Because of the item use config, the click event drops in rather fast (100ms approx), too fast
            // for most users - therefore a cooldown timestamp is additionally saved in the item, not affecting
            // the link itself. No elegance here but should work reliably.
            final long cdtime = player.inventory.getCurrentItem().getTag().getLong("cdtime");
            if(Math.abs(world.getGameTime()-cdtime) < 7) return;
            ItemSwitchLinkPearl.getCycledRelay(player.inventory.getCurrentItem(), world, pos);
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.relayconfig.confval" + Integer.toString(ItemSwitchLinkPearl.SwitchLink.fromItemStack(player.inventory.getCurrentItem()).relay()), null));
            player.inventory.getCurrentItem().getTag().putLong("cdtime", world.getGameTime());
            return;
          }
          case E_NOSOURCE:
            // That switch cannot be a link source (not
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.source_assign.error_nosource", null));
            break;
          case E_ALREADY_LINKED:
            // Switch has already a pearl with that location
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.source_assign.error_alreadylinked", null));
            break;
          case E_TOO_FAR:
            // Too far away from the target switch
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.source_assign.error_toofaraway", null));
            break;
          case E_FAILED:
            // General error, e.g. blockstate could not be determined, not a switch at that location, etc.
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.source_assign.error_failed", null));
            break;
        }
        ModResources.BlockSoundEvents.SWITCHLINK_LINK_SOURCE_FAILED.play(world, pos);
      }
      return;
    }
    if((item_held==Items.AIR) && te.click_config(this, true)) {
      // left double click config
      ModResources.BlockSoundEvents.DEFAULT_SWITCH_CONFIGCLICK.play(world, pos);
      ModAuxiliaries.playerStatusMessage(player, te.configStatusTextComponentTranslation((BlockSwitch)state.getBlock()));
      return;
    }
    if((config & (SWITCH_CONFIG_LCLICK_RESETTABLE))!=0) {
      // left click disabling pulse switches
      te.on_timer_reset();
      if(!state.get(POWERED)) return;
      world.setBlockState(pos, state.with(POWERED, false), 1|2);
      power_off_sound.play(world, pos);
    }
    if(((config & SWITCH_CONFIG_LINK_RELAY)==0) && (!te.nooutput())) {
      notifyNeighbours(world, pos, state, te, false);
    }
  }

  @Override
  public int tickRate(IWorldReader world)
  { return base_tick_rate; }

  @Override
  @SuppressWarnings("deprecation")
  public void tick(BlockState state, World world, BlockPos pos, Random random)
  {
    if((world.isRemote) || (!state.get(POWERED))) return; // scheduler tick only allowed when currently active.
    final TileEntitySwitch te = getTe(world, pos);
    if((te!=null) && (te.on_time_remaining() > 0)) { te.reschedule_block_tick(); return; }
    world.setBlockState(pos, (state=state.with(POWERED, false)));
    power_off_sound.play(world, pos);
    if((config & SWITCH_CONFIG_LINK_RELAY)==0) notifyNeighbours(world, pos, state, te, false);
    if((config & BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
      if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_DEACTIVATE)) {
        ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
      }
    }
  }

  @Override
  public boolean hasTileEntity(BlockState state)
  { return true; }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new TileEntitySwitch(ModContent.TET_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------

  public TileEntitySwitch getTe(IWorldReader world, BlockPos pos)
  {
    final TileEntity te = world.getTileEntity(pos);
    return (te instanceof TileEntitySwitch) ? ((TileEntitySwitch)te) : (null);
  }

  @Override
  public AxisAlignedBB getUnrotatedBB(BlockState state)
  { return ((state==null) || (state.getBlock()!=this) || (!state.get(POWERED))) ? super.getUnrotatedBB() : unrotated_bb_powered; }

  @Override
  public boolean isWallMount()
  { return ((config & (SWITCH_CONFIG_WALLMOUNT)) != 0); }

  @Override
  public boolean isLateral()
  { return ((config & (SWITCH_CONFIG_LATERAL)) != 0); }

  @Override
  public boolean isCube()
  { return ((config & (SWITCH_CONFIG_WALLMOUNT|SWITCH_CONFIG_LATERAL)) == 0); }

  public boolean onLinkRequest(final ItemSwitchLinkPearl.SwitchLink link, long req, final World world, final BlockPos pos, @Nullable final PlayerEntity player)
  {
    if((config & (SWITCH_CONFIG_BISTABLE|SWITCH_CONFIG_PULSE))==0) return false; // this override only allows manual switches
    TileEntitySwitch te = getTe(world, pos);
    if((te==null) || (!te.check_link_request(link))) return false;
    return onSwitchActivated(world, pos, world.getBlockState(pos), player, null);
  }

  /**
   * Neighbour blocks notification depending on the switch config. Assuming that the cpu time invested
   * here is less than invoking neighbour updates of blocks not affected by the switch outputs.
   */
  protected void notifyNeighbours(World world, BlockPos pos, BlockState state, @Nullable TileEntitySwitch te, boolean force)
  {
    if((!force) && (te!=null) && (te.nooutput())) {
      // Nothing to notify about
      return;
    } else if(net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(world, pos, state, java.util.EnumSet.allOf(Direction.class), false).isCanceled()) {
      // Canceled, don't invoke world.neighborChanged() directly as this would circumvent this check
      return;
    } else if((config & SWITCH_CONFIG_SIDES_CONFIGURABLE)!=0) {
      // Notify only blocks where the output side is enabled
      if((te==null)) return;
      final long disabled_sides = te.enabled_sides();
      for(Direction facing: Direction.values()) {
        if((disabled_sides & ((1l<<getAbsoluteFacing(state, facing).getIndex()) << SWITCH_DATA_SIDE_ENABLED_SHIFT)) == 0) continue;
        if(net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(world, pos, state, java.util.EnumSet.of(facing), false).isCanceled()) continue;
        world.neighborChanged(pos.offset(facing), this, pos);
        if(force || !te.weak()) world.notifyNeighborsOfStateExcept(pos.offset(facing), this, facing.getOpposite());
      }
    } else if((!isLateral()) && (isWallMount())) {
      // Switches attached to a wall block to the back side
      final Direction wall = state.get(FACING).getOpposite();
      world.neighborChanged(pos.offset(wall), this, pos);
      if(force || !te.weak()) {
        world.notifyNeighborsOfStateExcept(pos.offset(wall), this, wall.getOpposite());
      }
    } else if((isLateral()) && (!isWallMount())) {
      // Switches attached to the floor and output to the front and floor (contact switches)
      final Direction wall = state.get(FACING);
      world.neighborChanged(pos.offset(wall), this, pos);
      world.neighborChanged(pos.down(), this, pos);
      if(force || !te.weak()) {
        world.notifyNeighborsOfStateExcept(pos.offset(wall), this, wall.getOpposite());
        world.notifyNeighborsOfStateExcept(pos.down(), this, Direction.UP);
      }
    } else if((isLateral()) && (isWallMount())) {
      // Switches attached to the floor and output to the front (trapdoor switches)
      final Direction wall = state.get(FACING);
      world.neighborChanged(pos.offset(wall), this, pos);
      if(force || !te.weak()) world.notifyNeighborsOfStateExcept(pos.offset(wall), this, wall.getOpposite());
    } else {
      // notify all around
      world.notifyNeighborsOfStateChange(pos, this);
      if(force || !te.weak()) {
        world.notifyNeighborsOfStateExcept(pos.down(), this, Direction.UP);
        world.notifyNeighborsOfStateExcept(pos.up(), this, Direction.DOWN);
        world.notifyNeighborsOfStateExcept(pos.east(), this, Direction.WEST);
        world.notifyNeighborsOfStateExcept(pos.west(), this, Direction.EAST);
        world.notifyNeighborsOfStateExcept(pos.north(), this, Direction.SOUTH);
        world.notifyNeighborsOfStateExcept(pos.south(), this, Direction.NORTH);
      }
    }
  }

  /**
   * Used in getStrongPower() and getWeakPower()
   */
  private int getPower(BlockState state, IBlockReader world, BlockPos pos, Direction side, boolean strong)
  {
    // Transitional code, will be solved entirely with side config later
    if(((config & SWITCH_CONFIG_LINK_RELAY)!=0) || (!(world instanceof World))) {
      // Relays have only inputs, not outputs.
      return 0;
    } else if((config & SWITCH_CONFIG_CONTACT)!=0) {
      // from specific contact plate code
      if(!((side == (state.get(FACING).getOpposite())) || ((side == Direction.UP) && (!isWallMount())))) return 0;
      final TileEntitySwitch te = getTe((World)world, pos);
      return (te==null) ? 0 : te.power(state, strong);
    } else if(((config & SWITCH_CONFIG_SIDES_CONFIGURABLE)==0) ) {
      // from normal wall mounted switches
      boolean is_main_direction = (side == ((isLateral()) ? ((state.get(FACING)).getOpposite()) : (state.get(FACING))));
      final TileEntitySwitch te = getTe((World)world, pos);
      if(te==null) return 0;
      if(!is_main_direction && (strong || te.weak())) return 0;
      return te.power(state, strong);
    } else {
      // new code that will be applied for all later
      final TileEntitySwitch te = getTe((World)world, pos);
      if(te==null) return 0;
      //if((te.enabled_sides() & ((1l<<getAbsoluteFacing(state, side.getOpposite()).getIndex()) << SWITCH_DATA_SIDE_ENABLED_SHIFT)) == 0) return 0;
      return te.power(state, strong);
    }
  }

  /**
   * Basic switch activation functionality. Independent of player interaction. Can also be invoked by
   * links or other local or remote mechanisms.
   */
  protected boolean onSwitchActivated(World world, BlockPos pos, BlockState state, @Nullable PlayerEntity player, @Nullable Direction facing)
  {
    if(world.isRemote) return true;
    final TileEntitySwitch te = getTe(world, pos);
    if(te == null) return true;
    boolean was_powered = state.get(POWERED);
    if((config & (SWITCH_CONFIG_BISTABLE|SWITCH_CONFIG_SENSOR_BLOCKDETECT))!=0) {
      world.setBlockState(pos, (state=state.cycle(POWERED)), 1|2);
      if(was_powered) power_off_sound.play(world, pos); else power_on_sound.play(world, pos);
    } else {
      world.setBlockState(pos, state.with(POWERED, true), 1|2);
      power_on_sound.play(world, pos);
    }
    if((config & SWITCH_CONFIG_LINK_RELAY)==0) {
      notifyNeighbours(world, pos, state, te, false);
    }
    if((config & SWITCH_CONFIG_PULSE)!=0) {
      if((config & SWITCH_CONFIG_PULSE_EXTENDABLE)==0) te.on_timer_reset();
      te.on_timer_extend();
      te.reschedule_block_tick();
    }
    if(((config & (SWITCH_CONFIG_LINK_SOURCE_SUPPORT))!=0) && ((config & (SWITCH_CONFIG_BISTABLE|SWITCH_CONFIG_PULSE|SWITCH_CONFIG_SENSOR_BLOCKDETECT))!=0)) {
      if(!was_powered) {
        // Manual switches fire link requests when changing from unpowered to powered,
        // no matter if they are inverted or not.
        if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_ACTIVATE)) {
          ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
        }
      } else if((config & SWITCH_CONFIG_PULSE) == 0) {
        if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_DEACTIVATE)) {
          ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
        }
      }
    }
    return true;
  }

  @Override
  protected void onRsBlockDestroyed(BlockState state, World world, BlockPos pos, boolean isUpdateEvent)
  {
    final TileEntitySwitch te = getTe(world, pos);
    if(te!=null) { te.unlink_all(true); te.nooutput(true); }
    if(isUpdateEvent) world.removeBlock(pos, false);
    notifyNeighbours(world, pos, state,  te, true);
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity
   */
  public static class TileEntitySwitch extends RsTileEntity
  {
    protected static final int max_pulse_time = 200;
    protected long pulse_off_deadline_ = 0;     // pulse switch-off deadline, world ticks
    protected int scd_ = 0; // serialised configuration data
    protected int svd_ = 0; // serialised value data
    protected long click_config_time_lastclicked_ = 0; // tracking of double left clicks (needed for configurable timing)
    protected long click_config_last_cycled_ = 0;      // used to omit cycling the first config click (only show current config)
    protected long last_link_request_ = 0; // used to lock multiple link request to this entity in the same tick
    protected ArrayList<ItemSwitchLinkPearl.SwitchLink> links_ = null; // plugged link pearls

    public TileEntitySwitch(TileEntityType<?> te_type)
    { super(te_type); }

    public TileEntitySwitch()
    { super(ModContent.TET_SWITCH); }

    @Override
    public void writeNbt(CompoundNBT nbt, boolean updatePacket)
    {
      nbt.putInt("scd", scd_);
      nbt.putInt("svd", svd_);
      if(updatePacket) return; // needing only server save at the moment
      if((links_==null) || (links_.isEmpty())) {
        if(nbt.contains("links")) nbt.remove("links");
      } else {
        ListNBT tl = new ListNBT();
        for(ItemSwitchLinkPearl.SwitchLink e:links_) tl.add(e.toNbt());
        nbt.put("links", tl);
      }
    }

    @Override
    public void readNbt(CompoundNBT nbt, boolean updatePacket)
    {
      int previous_scd = scd_;
      int previous_svd = svd_;
      scd_ = nbt.getInt("scd");
      svd_ = nbt.getInt("svd");
      if(!updatePacket) { // Server data load operation
        if(scd_==0) reset(); // assumption that chunk data are broken
        if(!nbt.contains("links")) {
          if(links_!=null) links_.clear();
        } else {
          // Save link pearl data.
          ListNBT tl = nbt.getList("links", nbt.getId()); // getId(): tag type byte of CompoundNBT, which are saved.
          ArrayList<ItemSwitchLinkPearl.SwitchLink> links = new ArrayList<>();
          for(INBT e:tl) {
            ItemSwitchLinkPearl.SwitchLink lnk = ItemSwitchLinkPearl.SwitchLink.fromNbt((CompoundNBT)e);
            links.add(lnk);
          }
          links_ = links;
        }
      } else { // Client network packet reading
        if(
          ((svd_ & SWITCH_DATA_SVD_COLOR_MASK) != (previous_svd & SWITCH_DATA_SVD_COLOR_MASK)) ||
          ((scd_ & SWITCH_DATA_POWERED_POWER_MASK) != (previous_scd & SWITCH_DATA_POWERED_POWER_MASK))
        ) {
          if((getWorld() != null) && (getWorld().isRemote)) {
            //world.markForRerender(getPos()); //world.setBlockState(pos, state, 16|32);
          }
        }
      }
    }

    @Override
    public int color_tint()
    { return ((svd_ & ((int)SWITCH_DATA_SVD_COLOR_MASK)) >> SWITCH_DATA_SVD_COLOR_SHIFT); }

    public int svd()
    { return svd_; }

    public int scd()
    { return scd_; }

    public int configured_on_time()
    { return ((svd_ & SWITCH_DATA_SVD_ACTIVE_TIME_MASK) >> 0); }

    public void configured_on_time(int t)
    { svd_ = (svd_ & (~SWITCH_DATA_SVD_ACTIVE_TIME_MASK)) | ((t & SWITCH_DATA_SVD_ACTIVE_TIME_MASK) << 0); }

    public int on_power()
    { return ((scd_ & ((int)SWITCH_DATA_POWERED_POWER_MASK)) >> 0); }

    public int off_power()
    { return ((scd_ & ((int)SWITCH_DATA_UNPOWERED_POWER_MASK)) >> 8); }

    public boolean inverted()
    { return ((scd_ & ((int)SWITCH_DATA_INVERTED)) != 0); }

    public boolean weak()
    { return ((scd_ & ((int)SWITCH_DATA_WEAK)) != 0); }

    public boolean nooutput()
    { return ((scd_ & ((int)SWITCH_DATA_NOOUTPUT)) != 0); }

    public long enabled_sides()
    { return (scd_ & (SWITCH_DATA_SIDE_ENABLED_MASK)); }

    public void color_tint(int tint)
    { svd_ = (svd_ & ~((int)SWITCH_DATA_SVD_COLOR_MASK)) | ( (tint<<SWITCH_DATA_SVD_COLOR_SHIFT) & SWITCH_DATA_SVD_COLOR_MASK); }

    public void on_power(int p)
    { scd_ = (scd_ & ~((int)SWITCH_DATA_POWERED_POWER_MASK)) | (int)(((p<0)?0:((p>15)?(15):(p)) & ((int)SWITCH_DATA_POWERED_POWER_MASK))<<0); }

    public void off_power(int p)
    { scd_ = (scd_ & ~((int)SWITCH_DATA_UNPOWERED_POWER_MASK)) | ((int)(((p<0)?0:((p>15)?(15):(p)) & 0x000f)<<8) & ((int)SWITCH_DATA_UNPOWERED_POWER_MASK)); }

    public void inverted(boolean val)
    { if(val) scd_ |= ((int)SWITCH_DATA_INVERTED); else scd_ &= ~((int)SWITCH_DATA_INVERTED); }

    public void weak(boolean val)
    { if(val) scd_ |= ((int)SWITCH_DATA_WEAK); else scd_ &= ~((int)SWITCH_DATA_WEAK); }

    public void nooutput(boolean val)
    { if(val) scd_ |= ((int)SWITCH_DATA_NOOUTPUT); else scd_ &= ~((int)SWITCH_DATA_NOOUTPUT); }

    public void enabled_sides(long mask)
    { scd_ = (scd_ & ~((int)SWITCH_DATA_SIDE_ENABLED_MASK)) | ((int)(mask & SWITCH_DATA_SIDE_ENABLED_MASK)); }

    public void reset()
    { reset(null); }

    /**
     * Resets internal entity values to the defaults, the serialised config data (scd) are
     * set to the defaults of the block config accordingly.
     */
    public void reset(IWorldReader world)
    {
      pulse_off_deadline_ = 0;
      click_config_time_lastclicked_ = 0;
      svd_ = 0;
      try {
        // If the world is not yet available or the block not loaded let it run with the head into the wall and say 0.
        final int current_scd = scd_;
        if((world instanceof World) && (world.isAreaLoaded(getPos(), 1))) {
          scd_ = (int)((((BlockSwitch)(world.getBlockState(getPos()).getBlock())).config) & SWITCH_DATA_ENTITY_DEFAULTS_MASK);
          if(current_scd != scd_) markDirty();
        } else {
          scd_ = 15;
        }
      } catch(Exception e) {
        scd_ = 15; // set the on-power to default 15, but no other settings.
      }
    }

    /**
     * Returns the current power depending on the block settings.
     */
    public int power(BlockState state, boolean strong)
    { return nooutput() ? (0) : ((strong && weak()) ? (0) : ( (inverted() == state.get(POWERED)) ? off_power() : on_power() )); }

    /**
     * Schedules the off_timer_ value ahead.
     */
    public void reschedule_block_tick()
    {
      int t = on_time_remaining();
      if(t <= 0) return;
      final Block block = getBlockState().getBlock();
      if(!world.getPendingBlockTicks().isTickScheduled(pos, block)) {
        world.getPendingBlockTicks().scheduleTick(pos, block, t);
      }
    }

    public int on_time_remaining()
    {
      long dt = Math.max(0, pulse_off_deadline_ - world.getGameTime());
      return (dt > max_pulse_time) ? 0 : (int)dt; // 0 if something messed with the total world time.
    }

    public void on_timer_reset()
    { on_timer_reset(0); }

    public void on_timer_reset(int preset_ticks)
    { pulse_off_deadline_ = world.getGameTime() + ((Math.min(preset_ticks, max_pulse_time) > 0) ? (preset_ticks) : (0)); }

    public void on_timer_extend()
    {
      int t = on_time_remaining();
      if(configured_on_time() >= base_tick_rate) t = configured_on_time();
      else if(t > (90)) t  = max_pulse_time;
      else if(t > (45)) t  = 100;
      else if(t > (15)) t  = 50;
      else if(t > ( 1)) t  = 30;
      else t = default_pulse_on_time;
      on_timer_reset(t);
    }

    /**
     * Configuration via block right clicking, x and z are normalised
     * coords of the xy position clicked on the main facing, values
     * 0 to 16. Shall return true if the block has to be updated and
     * re-rendered.
     */
    public boolean activation_config(BlockState state, @Nullable PlayerEntity player, double x, double y)
    { return false; }

    /**
     * Switch output configuration, normally invoked on double-left-click empty handed, or
     * on single click with an accepted wrench item in the main hand.
     */
    public boolean click_config(@Nullable BlockSwitch block, boolean needsDoubleClick)
    {
      if(block==null) { click_config_time_lastclicked_ = 0; return false; }

      // Check double click
      final long t = System.currentTimeMillis();
      if(needsDoubleClick) {
        boolean dblclicked = ((t - click_config_time_lastclicked_) > 0) && ((t - click_config_time_lastclicked_) < ModConfig.config_left_click_timeout);
        click_config_time_lastclicked_ = dblclicked ? 0 : t;
        if(!dblclicked) return false; // not double clicked
      }
      boolean multiclicked = ((t-click_config_last_cycled_) > 0) && ((t-click_config_last_cycled_) < 3000);
      click_config_last_cycled_ = t;
      if(!multiclicked) return true; // first click shall only show the unchanged status.

      // Settings are changed by cycling through available states.
      if((block.config & (SWITCH_CONFIG_INVERTABLE|SWITCH_CONFIG_WEAKABLE))==(SWITCH_CONFIG_INVERTABLE|SWITCH_CONFIG_WEAKABLE)) {
        switch(((weak() ? 1:0) | (inverted() ? 2:0)) | (nooutput() ? 4:0)) {
          case  0: weak(true);  inverted(false); nooutput(false); break;
          case  1: weak(false); inverted(true);  nooutput(false); break;
          case  2: weak(true);  inverted(true);  nooutput(false); break;
          case  3: weak(false); inverted(false); nooutput(true);  break;
          default: weak(false); inverted(false); nooutput(false); break;
        }
      } else if((block.config & SWITCH_CONFIG_WEAKABLE) != 0) {
        switch((weak() ? 1:0) | (nooutput() ? 2:0)) {
          case  0: weak(true);  inverted(false); nooutput(false); break;
          case  1: weak(false); inverted(false); nooutput(true);  break;
          default: weak(false); inverted(false); nooutput(false); break;
        }
      } else if((block.config & SWITCH_CONFIG_INVERTABLE) != 0) {
        switch((inverted() ? 1:0) | (nooutput() ? 2:0)) {
          case  0: weak(false); inverted(true);  nooutput(false); break;
          case  1: weak(false); inverted(false); nooutput(true);  break;
          default: weak(false); inverted(false); nooutput(false); break;
        }
      }
      if((block.config & (SWITCH_DATA_WEAK|SWITCH_CONFIG_WEAKABLE))==(SWITCH_DATA_WEAK)) {
        if(!weak()) weak(true); // strong/weak not settable, and always weak forced.
      }
      if((ModConfig.without_switch_nooutput) || ((block.config & SWITCH_CONFIG_LINK_RELAY)!=0)) {
        nooutput(false); // override last option, where not weak and not inverted is already set.
      }
      markDirty();
      // Not using block.notifyNeighbours() as output config may/will have changed from e.g. strong
      // to weak, so indirectly powered blocks would not be notified.
      world.notifyNeighborsOfStateChange(pos, block);
      for(Direction f:Direction.values()) world.notifyNeighborsOfStateChange(pos.offset(f), block);
      return true;
    }

    /**
     * Localisable switch status, normally called on double clicking a switch.
     */
    public TranslationTextComponent configStatusTextComponentTranslation(BlockSwitch block)
    {
      StringTextComponent separator = (new StringTextComponent(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
      TranslationTextComponent status = ModAuxiliaries.localizable("switchconfig.options", TextFormatting.RESET);
      boolean statusset = false;
      if((on_power() < 15) || (off_power()>0)) {
        if((block == null) || ((block.config & (SWITCH_CONFIG_AUTOMATIC|SWITCH_CONFIG_LINK_RELAY))==0)) {
          // power only for non-auto-switches
          statusset = true;
          status.appendSibling(ModAuxiliaries.localizable("switchconfig.options.output_power", TextFormatting.RED, new Object[]{on_power()}));
        }
      }
      if(nooutput()) {
        if(statusset) status.appendSibling(separator.deepCopy()); statusset = true;
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options.no_output", TextFormatting.DARK_AQUA));
      } else if(!inverted()) {
        if(statusset) status.appendSibling(separator.deepCopy()); statusset = true;
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options." + (weak() ? "weak" : "strong"), TextFormatting.DARK_AQUA));
      } else {
        if(statusset) status.appendSibling(separator.deepCopy()); statusset = true;
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options." + (weak() ? "weakinverted" : "stronginverted"), TextFormatting.DARK_AQUA));
      }
      if(configured_on_time() > 0) {
        if(statusset) status.appendSibling(separator.deepCopy());
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options.pulsetime", TextFormatting.GOLD, new Object[]{
          Double.toString( ((double)(configured_on_time()))/20 ),
          Integer.toString(configured_on_time())
        }));
      }
      return status;
    }

    public boolean has_links()
    { return (links_!=null) && (!links_.isEmpty()); }

    public static long linktime()
    { return System.currentTimeMillis(); } // not using ticks because there are scenarios where the world time is disabled.

    /**
     * Called when link pearl requests are sent to the addressed switch. This can be via
     * player pearl usage, from link pearls in switches. The method is used to ensure on
     * the called-side that no recursion or loop lock or other unwanted effects occur.
     * Returns boolean success.
     */
    public boolean check_link_request(final ItemSwitchLinkPearl.SwitchLink link)
    {
      final long t = linktime();
      if((world.isRemote) || (last_link_request_ == t)) return false; // not in the same tick, people could try to link A to B and B to A.
      last_link_request_ = t;
      final BlockState state = world.getBlockState(pos);
      if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return false;
      return ((((BlockSwitch)state.getBlock()).config & (SWITCH_CONFIG_LINK_TARGET_SUPPORT))!=0);
    }

    /**
     * Called when a link pearl shall be placed in a switch to make it a link source.
     */
    enum SwitchLinkAssignmentResult {OK, E_SELF_ASSIGN, E_ALREADY_LINKED, E_TOO_FAR, E_NOSOURCE, E_FAILED}
    SwitchLinkAssignmentResult assign_switchlink(final World world, final BlockPos sourcepos, final ItemStack stack)
    {
      if(stack==null) return SwitchLinkAssignmentResult.E_FAILED;
      final ItemSwitchLinkPearl.SwitchLink link = ItemSwitchLinkPearl.SwitchLink.fromItemStack(stack);
      if((world==null) || (!link.valid) || (!sourcepos.equals(getPos()))) return SwitchLinkAssignmentResult.E_FAILED;
      final BlockState state = world.getBlockState(sourcepos);
      if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return SwitchLinkAssignmentResult.E_FAILED;
      if(link.target_position.equals(sourcepos)) return SwitchLinkAssignmentResult.E_SELF_ASSIGN;
      if((((BlockSwitch)state.getBlock()).config & SWITCH_CONFIG_LINK_SOURCE_SUPPORT)==0) return SwitchLinkAssignmentResult.E_NOSOURCE;
      if(link.isTooFar(sourcepos)) return SwitchLinkAssignmentResult.E_TOO_FAR;
      if(links_==null) links_ = new ArrayList<>();
      for(ItemSwitchLinkPearl.SwitchLink lnk: links_) {
        if(lnk.target_position.equals(link.target_position)) return SwitchLinkAssignmentResult.E_ALREADY_LINKED;
      }
      links_.add(link);
      markDirty();
      return SwitchLinkAssignmentResult.OK;
    }

    /**
     * Removes all links, returns a stack of link pearls placed in the switch,
     * optionally drops that stack in the world at the switch position.
     */
    public ArrayList<ItemStack> unlink_all(boolean drop)
    {
      ArrayList<ItemStack> stacks = new ArrayList<>();
      if((world.isRemote) || (links_==null)) return stacks;
      for(ItemSwitchLinkPearl.SwitchLink e:links_) stacks.add(e.toSwitchLinkPearl());
      links_.clear();
      if(drop) { for(ItemStack e:stacks) world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), e)); }
      return stacks;
    }

    /**
     * Invokes the link targets of all pearls plugged into this switch, returns
     * true if all link requests succeeded.
     */
    public boolean activate_links(final int req)
    {
      if(ModConfig.without_switch_linking) return true;
      last_link_request_ = linktime();
      if(links_==null) return true;
      int n_fails = 0;
      for(ItemSwitchLinkPearl.SwitchLink lnk:links_) {
        switch(lnk.request(req, world, pos, null)) {
          case OK:
          case NOT_MATCHED:
            break;
          default:
            ++n_fails;
        }
      }
      return (n_fails==0);
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Mod specific user activation/click calculations
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Data class with static factory function to enable unified block activation
   * click coordinates with respect to the device (UI) interface facing, normalised
   * to 0..15. On `wrenched==true` the face was clicked with an valid wrench. If
   * `touch_configured==false`, the block was not clicked on the main display
   * facing side, and remaining instance data are not valid. If clicked with a
   * redstone stack or dye, the corresponding data values will be non-default.
   */
  protected static final class ClickInteraction
  {
    public boolean touch_configured = false;
    public boolean wrenched = false;
    public Item item = Items.AIR;
    public int item_count = 0;
    public int dye = -1;
    public double x = 0;
    public double y = 0;

    @Override
    public String toString()
    {
      return "{x:" + Double.toString(x) + ",y:" + Double.toString(y) + ",touch_configured:" + Boolean.toString(touch_configured)
        + ",wrenched:" + Boolean.toString(wrenched) + ",item_count:" + Integer.toString(item_count) + ",dye:" + Integer.toString(dye) + "}";
    }

    /**
     * Returns a ClickInteraction object, where x and y are assigned to the coordinates clicke with respect
     * to the blocks bounding box. x/y are normalised to 16. `touch_configured` true when the player clicked
     * on a block surface that faces the player.
     */
    private static ClickInteraction touch(ClickInteraction ck, BlockState state, Direction facing, float x, float y, float z)
    {
      final BlockSwitch block = (BlockSwitch)(state.getBlock());
      // Touch config check
      double xo=0, yo=0;
      if((block.isCube()) || ((block.isWallMount()) && (!block.isLateral()))) {
        // UI facing the player in horizontal direction
        if(!block.isCube()) {
          if(facing != state.get(FACING)) return ck;
        } else {
          if(facing != state.get(FACING).getOpposite()) return ck;
        }
        switch(facing.getIndex()) {
          case 0: xo = 1-x; yo = 1-z; break; // DOWN
          case 1: xo = 1-x; yo = z  ; break; // UP
          case 2: xo = 1-x; yo = y  ; break; // NORTH
          case 3: xo = x  ; yo = y  ; break; // SOUTH
          case 4: xo = z  ; yo = y  ; break; // WEST
          case 5: xo = 1-z; yo = y  ; break; // EAST
        }
        final AxisAlignedBB aa = block.getUnrotatedBB();
        xo = Math.round(((xo-aa.minX) * (1.0/(aa.maxX-aa.minX)) * 15.5) - 0.25);
        yo = Math.round(((yo-aa.minY) * (1.0/(aa.maxY-aa.minY)) * 15.5) - 0.25);
      } else if(block.isLateral()) {
        // Floor mounted UI facing up
        if(facing != Direction.UP) return ck;
        facing = state.get(FACING);
        switch(facing.getIndex()) {
          case 0: xo =   x; yo =   z; break; // DOWN
          case 1: xo =   x; yo =   z; break; // UP
          case 2: xo =   x; yo = 1-z; break; // NORTH
          case 3: xo = 1-x; yo =   z; break; // SOUTH
          case 4: xo = 1-z; yo = 1-x; break; // WEST
          case 5: xo =   z; yo =   x; break; // EAST
        }
        final AxisAlignedBB aa = block.getUnrotatedBB();
        xo = 0.1 * Math.round(10.0 * (((xo-aa.minX) * (1.0/(aa.maxX-aa.minX)) * 15.5) - 0.25));
        yo = 0.1 * Math.round(10.0 * (((yo-(1.0-aa.maxZ)) * (1.0/(aa.maxZ-aa.minZ)) * 15.5) - 0.25));
      } else {
        return ck;
      }
      ck.x = ((xo > 15.0) ? (15.0) : ((xo < 0.0) ? 0.0 : xo));
      ck.y = ((yo > 15.0) ? (15.0) : ((yo < 0.0) ? 0.0 : yo));
      ck.touch_configured = true;
      return ck;
    }

    // Block.onBlockActivated() wrapper.
    public static ClickInteraction get(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
      float x = (float)(hit.getHitVec().getX() - Math.floor(hit.getHitVec().getX()));
      float y = (float)(hit.getHitVec().getY() - Math.floor(hit.getHitVec().getY()));
      float z = (float)(hit.getHitVec().getZ() - Math.floor(hit.getHitVec().getZ()));
      return get(world, pos, state, player, hand, hit.getFace(), x,y,z);
    }

    // Block.onBlockClicked() wrapper
    public static ClickInteraction get(BlockState state, World world, BlockPos pos, PlayerEntity player)
    {
      return get(world, pos, state, player, null, null, 0, 0, 0);
    }

    /**
     *
     */
    public static ClickInteraction get(World world, BlockPos pos, @Nullable BlockState state, PlayerEntity player, @Nullable Hand hand, @Nullable Direction facing, float x, float y, float z)
    {
      ClickInteraction ck = new ClickInteraction();
      if((world==null) || (pos==null)) return ck;
      if(state==null) state = world.getBlockState(pos);
      if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return ck;
      final BlockSwitch block = (BlockSwitch)(state.getBlock());
      final ItemStack item = player.getHeldItemMainhand();
      if(item == null) return ck;
      if(item.getItem() == Items.REDSTONE) {
        ck.item = Items.REDSTONE;
        ck.item_count = item.getCount();
      } else if(item.getItem() == Items.ENDER_PEARL) {
        ck.item = Items.ENDER_PEARL;
        ck.item_count = item.getCount();
      } else if(item.getItem() == ModContent.SWITCH_LINK_PEARL) {
        ck.item = ModContent.SWITCH_LINK_PEARL;
        ck.item_count = item.getCount();
    // @todo DyeUtils
    //} else if(DyeUtils.isDye(item)) {
    //  ck.item = Items.DYE;
    //  ck.dye = DyeUtils.rawMetaFromStack(item);
    //  if(ck.dye > 15) ck.dye = 15;
      } else if(item.getItem() != Items.AIR) {
        ck.wrenched = (("," + ModConfig.accepted_wrenches + ",").contains("," + item.getItem().getRegistryName().getPath() + ","));
        if(ck.wrenched) return ck;
      }
      if(((block.config & SWITCH_CONFIG_TOUCH_CONFIGURABLE)!=0) && (ck.item != ModContent.SWITCH_LINK_PEARL) && (ck.item != Items.ENDER_PEARL)) {
        return touch(ck, state, facing, x,y,z);
      } else {
        return ck;
      }
    }
  }

}
