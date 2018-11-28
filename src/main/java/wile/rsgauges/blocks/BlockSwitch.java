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

import wile.rsgauges.ModConfig;
import wile.rsgauges.ModItems;
import wile.rsgauges.ModAuxiliaries;
import wile.rsgauges.ModBlocks;
import wile.rsgauges.ModResources;
import wile.rsgauges.items.ItemSwitchLinkPearl;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class BlockSwitch extends RsBlock implements ModBlocks.Colors.ColorTintSupport
{
  public static final long SWITCH_DATA_POWERED_POWER_MASK       = 0x000000000000000fl;
  public static final long SWITCH_DATA_UNPOWERED_POWER_MASK     = 0x00000000000000f0l;
  public static final long SWITCH_DATA_INVERTED                 = 0x0000000000000100l;
  public static final long SWITCH_DATA_WEAK                     = 0x0000000000000200l;
  public static final long SWITCH_DATA_NOOUTPUT                 = 0x0000000000000400l;
  public static final long SWITCH_CONFIG_INVERTABLE             = 0x0000000000001000l;
  public static final long SWITCH_CONFIG_WEAKABLE               = 0x0000000000002000l;
  public static final long SWITCH_CONFIG_POWER_SETTABLE         = 0x0000000000004000l;
  public static final long SWITCH_CONFIG_BISTABLE               = 0x0000000000008000l;
  public static final long SWITCH_CONFIG_PULSE                  = 0x0000000000010000l;
  public static final long SWITCH_CONFIG_PULSE_EXTENDABLE       = 0x0000000000020000l;
  public static final long SWITCH_CONFIG_LCLICK_RESETTABLE      = 0x0000000000040000l;
  public static final long SWITCH_CONFIG_TOUCH_CONFIGURABLE     = 0x0000000000080000l;
  public static final long SWITCH_CONFIG_AUTOMATIC              = 0x0000000000100000l;
  public static final long SWITCH_CONFIG_SENSOR_VOLUME          = 0x0000000000200000l;
  public static final long SWITCH_CONFIG_SENSOR_LINEAR          = 0x0000000000400000l;
  public static final long SWITCH_CONFIG_FLOOR_MOUNT            = 0x0000000000800000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_ON    = 0x0000000001000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_OFF   = 0x0000000002000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE       = SWITCH_CONFIG_PROJECTILE_SENSE_ON|SWITCH_CONFIG_PROJECTILE_SENSE_OFF;
  public static final long SWITCH_CONFIG_HOPPER_MOUNTBALE       = 0x0000000004000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHT           = 0x0000000008000000l;
  public static final long SWITCH_CONFIG_TIMER_DAYTIME          = 0x0000000010000000l;
  public static final long SWITCH_CONFIG_SENSOR_RAIN            = 0x0000000020000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHTNING       = 0x0000000040000000l;
  public static final long SWITCH_CONFIG_SENSOR_ENVIRONMENTAL   = SWITCH_CONFIG_SENSOR_LIGHT|SWITCH_CONFIG_TIMER_DAYTIME|SWITCH_CONFIG_SENSOR_RAIN|SWITCH_CONFIG_SENSOR_LIGHTNING;
  public static final long SWITCH_CONFIG_TIMER_INTERVAL         = 0x0000000080000000l;
  public static final long SWITCH_CONFIG_TRANSLUCENT            = 0x0000000100000000l;
  public static final long SWITCH_CONFIG_PULSETIME_CONFIGURABLE = 0x0000000200000000l;
  public static final long SWITCH_CONFIG_FAINT_LIGHTSOURCE      = 0x0000000400000000l;
  public static final long SWITCH_CONFIG_COLOR_TINT_SUPPORT     = 0x0000000800000000l;
  public static final long SWITCH_CONFIG_NOT_PISTON_MOUNTBALE   = 0x0000001000000000l;
  public static final long SWITCH_CONFIG_NOT_PASSABLE           = 0x0000002000000000l;
  public static final long SWITCH_CONFIG_LATERAL_WALLMOUNT      = 0x0000004000000000l;
  public static final long SWITCH_CONFIG_SHOCK_SENSITIVE        = 0x0000008000000000l;
  public static final long SWITCH_CONFIG_HIGH_SENSITIVE         = 0x0000010000000000l;
  public static final long SWITCH_CONFIG_LINK_SOURCE_SUPPORT    = 0x0000020000000000l;
  public static final long SWITCH_CONFIG_LINK_TARGET_SUPPORT    = 0x0000040000000000l;

  public static final int SWITCH_DATA_SVD_ACTIVE_TIME_MASK      = 0x000000ff;
  public static final int SWITCH_DATA_SVD_COLOR_MASK            = 0x00000f00;
  public static final int SWITCH_DATA_SVD_COLOR_SHIFT           = 8;

  public static final int base_tick_rate = 2;
  public final long config;

  @Nullable protected final AxisAlignedBB unrotated_bb_powered;
  protected final ModResources.BlockSoundEvent power_on_sound;
  protected final ModResources.BlockSoundEvent power_off_sound;

  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public BlockSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound, @Nullable Material material)
  {
    super(registryName, unrotatedBBUnpowered, material);
    this.config = config;
    if((powerOnSound==null) && (powerOffSound==null)) {
      powerOnSound  = ModResources.BlockSoundEvents.DEFAULT_SWITCH_ACTIVATION;
      powerOffSound = ModResources.BlockSoundEvents.DEFAULT_SWITCH_DEACTIVATION;
    } else {
      if(powerOnSound==null) powerOnSound =  ModResources.BlockSoundEvents.DEFAULT_SWITCH_MUTE;
      if(powerOffSound==null) powerOffSound = ModResources.BlockSoundEvents.DEFAULT_SWITCH_MUTE;
    }
    unrotated_bb_powered = unrotatedBBPowered;
    power_on_sound = powerOnSound;
    power_off_sound = powerOffSound;
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
  }

  public BlockSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { this(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config, powerOnSound, powerOffSound, null); }

  public BlockSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config)
  { this(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config, null, null, null); }

  public BlockSwitch(String registryName, AxisAlignedBB unrotatedBB, long config)
  { this(registryName, unrotatedBB, null, config, null, null, null); }

  @Override
  public AxisAlignedBB getUnrotatedBB(IBlockState state)
  { return ((unrotated_bb_powered==null) || (state==null) || (state.getBlock()!=this) || (!state.getValue(POWERED))) ? super.getUnrotatedBB() : unrotated_bb_powered; }

  @Override
  public boolean isWallMount()
  { return ((config & (SWITCH_CONFIG_FLOOR_MOUNT)) == 0); }

  @Override
  public boolean isLateral()
  { return ((config & (SWITCH_CONFIG_FLOOR_MOUNT|SWITCH_CONFIG_LATERAL_WALLMOUNT)) != 0); }

  @Override
  public boolean hasColorMultiplierRGBA()
  { return (!ModConfig.optouts.without_switch_colortinting) && ((config & SWITCH_CONFIG_COLOR_TINT_SUPPORT) != 0); }

  @Override
  public int getColorMultiplierRGBA(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos)
  {
    if((pos==null) || (world==null)) return 0xffffffff;
    final TileEntity te = world.getTileEntity(pos);
    return (te instanceof TileEntitySwitch)  ? (ModAuxiliaries.DyeColorFilters.byIndex[((TileEntitySwitch)te).color_tint() & 0xf]) : (0xffffffff);
  }

  public boolean onLinkRequest(final ItemSwitchLinkPearl.SwitchLink link, long req, final World world, final BlockPos pos, @Nullable final EntityPlayer player)
  {
    if((config & (SWITCH_CONFIG_BISTABLE|SWITCH_CONFIG_PULSE))==0) return false; // this override only allows manual switches
    TileEntitySwitch te = getTe(world, pos);
    if((te==null) || (!te.check_link_request(link))) return false;
    return onBlockActivated(world, pos, world.getBlockState(pos), player);
  }

  ///
  /// Forge / Minecraft overloads
  ///

  @Override
  public IBlockState getStateFromMeta(int meta)
  { return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 0x7)).withProperty(POWERED, ((meta & 0x8) != 0)); }

  @Override
  public int getMetaFromState(IBlockState state)
  { return (state.getValue(FACING).getIndex() & 0x07) | ((state.getValue(POWERED)) ? 0x8 : 0x0); }

  @Override
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, FACING, POWERED); }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
  { return state; }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer()
  { return ((config & SWITCH_CONFIG_TRANSLUCENT) != 0) ? (BlockRenderLayer.TRANSLUCENT) : (BlockRenderLayer.CUTOUT); }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
  { return ((config & SWITCH_CONFIG_NOT_PASSABLE)==0) ? NULL_AABB : getBoundingBox(state, world, pos); }

  @Override
  @SuppressWarnings("deprecation")
  public int getLightValue(IBlockState state)
  { return (((config & SWITCH_CONFIG_FAINT_LIGHTSOURCE) != 0) && (ModAuxiliaries.isClientSide())) ? 1 : 0; }

  @Override
  @SuppressWarnings("deprecation")
  public boolean canProvidePower(IBlockState state)
  { return true; }

  @Override
  public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
  { return (!isWallMount()) && ((side==null) || ((side)==(EnumFacing.UP)) || ((side)==(state.getValue(FACING)))); }

  @Override
  @SuppressWarnings("deprecation")
  public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
  {
    if((!(world instanceof World)) || (side != ((isLateral()) ? ((state.getValue(FACING)).getOpposite()) : (state.getValue(FACING))))) return 0;
    final TileEntitySwitch te = getTe((World)world, pos);
    return (te==null) ? 0 : te.power(state, false);
  }

  @Override
  @SuppressWarnings("deprecation")
  public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
  {
    if((!(world instanceof World)) || (side != ((isLateral()) ? ((state.getValue(FACING)).getOpposite()) : (state.getValue(FACING))))) return 0;
    final TileEntitySwitch te = getTe((World)world, pos);
    return (te==null) ? 0 : te.power(state, true);
  }

  protected void notifyNeighbours(World world, BlockPos pos, IBlockState state)
  {
    world.notifyNeighborsOfStateChange(pos, this, false);
    if(!isLateral()) {
      world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
    } else {
      world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING)), this, false);
      world.notifyNeighborsOfStateChange(pos.down(), this, false);
    }
  }

  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
  {
    if((config & SWITCH_CONFIG_HOPPER_MOUNTBALE)==0) {
      return super.canPlaceBlockOnSide(world, pos, side);
    } else {
      return super.canPlaceBlockOnSide(world, pos, side, (block)->{
        if(((config & SWITCH_CONFIG_HOPPER_MOUNTBALE)!=0) && (block instanceof BlockHopper)) return true;
        if(((config & SWITCH_CONFIG_NOT_PISTON_MOUNTBALE)!=0) && isExceptBlockForAttachWithPiston(block)) return false;
        return true;
      }, null);
    }
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  {
    final TileEntitySwitch te = getTe(world, pos);
    if(te != null) te.reset();
    if(!super.onBlockPlacedByCheck(world, pos, state, placer, stack)) return;
    world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, false), 1|2);
    notifyNeighbours(world, pos, state);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  { return onBlockActivated(world, pos, state, player); }

  @Override
  public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
  {
    if(world.isRemote || ((config & SWITCH_CONFIG_PROJECTILE_SENSE)==0) || (!(entity instanceof IProjectile))) return;
    if(state.getValue(POWERED)) {
      if((config & SWITCH_CONFIG_PROJECTILE_SENSE_OFF)==0) return;
    } else {
      if((config & SWITCH_CONFIG_PROJECTILE_SENSE_ON)==0) return;
    }
    onBlockActivated(world, pos, state, null);
  }

  protected boolean onBlockActivated(World world, BlockPos pos, IBlockState state, @Nullable EntityPlayer player)
  {
    if(world.isRemote) return true;
    final TileEntitySwitch te = getTe(world, pos);
    if(te == null) return true;
    if((player != null) && te.click_config(null)) return true;
    boolean was_powered = state.getValue(POWERED);
    if((config & SWITCH_CONFIG_BISTABLE)!=0) {
      world.setBlockState(pos, (state=state.cycleProperty(POWERED)), 1|2);
      if(was_powered) power_off_sound.play(world, pos); else power_on_sound.play(world, pos);
    } else {
      world.setBlockState(pos, state.withProperty(POWERED, true), 1|2);
      power_on_sound.play(world, pos);
    }
    notifyNeighbours(world, pos, state);
    if((config & SWITCH_CONFIG_PULSE)!=0) {
      world.scheduleUpdate(pos, this, base_tick_rate);
      if((config & SWITCH_CONFIG_PULSE_EXTENDABLE)==0) te.off_timer_reset();
      te.off_timer_extend();
    }
    if( ((config&(SWITCH_CONFIG_LINK_SOURCE_SUPPORT))!=0) && ((config&(SWITCH_CONFIG_BISTABLE|SWITCH_CONFIG_PULSE))!=0) ) {
      if(!was_powered) {
        // Manual switches fire link requests when changing from unpowered to powered,
        // no matter if they are inverted or not.
        if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_ACTIVATE)) {
          ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
        }
      } else if((config & (SWITCH_CONFIG_BISTABLE|SWITCH_CONFIG_PULSE)) == SWITCH_CONFIG_BISTABLE) {
        if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_DEACTIVATE)) {
          ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
        }
      }
    }
    return true;
  }

  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
  {
    if((world.isRemote) || (player == null)) return;
    final IBlockState state = world.getBlockState(pos);
    if(!(state.getBlock() instanceof BlockSwitch)) return;
    final TileEntitySwitch te = getTe(world, pos);
    if(te == null) return;
    RsBlock.WrenchActivationCheck ck = RsBlock.WrenchActivationCheck.onBlockActivatedCheck(world, pos, null, player, null, null, 0, 0, 0);
    if((ck.item==Items.REDSTONE) && (((BlockSwitch)state.getBlock()).config & SWITCH_CONFIG_PULSETIME_CONFIGURABLE) != 0) {
      if(!ModConfig.optouts.without_pulsetime_config) {
        te.active_time(ck.item_count);
        ModAuxiliaries.playerStatusMessage(player, te.configStatusTextComponentTranslation((BlockSwitch)state.getBlock()));
        return;
      }
    } else if((ck.dye >= 0) && (((BlockSwitch)state.getBlock()).config & SWITCH_CONFIG_COLOR_TINT_SUPPORT) != 0) {
      if((!ModConfig.optouts.without_switch_colortinting) && (ck.dye <= 15)) {
        te.color_tint(ck.dye);
        world.markAndNotifyBlock(pos, null, state, state, 1|2|4|16);
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.tinting", null, new Object[]{ModAuxiliaries.localizable("switchconfig.tinting." + ModAuxiliaries.DyeColorFilters.nameByIndex[ck.dye & 0xf], null)}));
        return;
      }
    } else if(ck.item==Items.ENDER_PEARL) {
      if(!ModConfig.optouts.without_switch_linking) {
        final BlockSwitch block = (BlockSwitch)state.getBlock();
        if((block.config & SWITCH_CONFIG_LINK_TARGET_SUPPORT) == 0) {
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.target_assign.error_notarget", null));
          ModResources.BlockSoundEvents.SWITCHLINK_CANNOT_LINK_THAT.play(world, pos);
        } else {
          ItemStack link_stack = ItemSwitchLinkPearl.createFromEnderPearl(world, pos, player);
          if((link_stack==null) || (link_stack.getTagCompound()==null)) {
            ModResources.BlockSoundEvents.SWITCHLINK_CANNOT_LINK_THAT.play(world, pos);
          } else {
            link_stack.getTagCompound().setLong("cdtime", world.getTotalWorldTime()); // see E_SELF_ASSIGN branch for SWITCH_LINK_PEARL
            player.inventory.setInventorySlotContents(player.inventory.currentItem, link_stack);
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.target_assign.ok", null));
            ModResources.BlockSoundEvents.SWITCHLINK_LINK_TARGET_SELECTED.play(world, pos);
          }
        }
        return;
      }
    } else if((ck.item==ModItems.SWITCH_LINK_PEARL) && (player.inventory!=null) && (player.inventory.getCurrentItem()!=null) && (player.inventory.getCurrentItem().getItem())==ModItems.SWITCH_LINK_PEARL) {
      if(!ModConfig.optouts.without_switch_linking) {
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
            final long cdtime = player.inventory.getCurrentItem().getTagCompound().getLong("cdtime");
            if(Math.abs(world.getTotalWorldTime()-cdtime) < 7) return;
            ItemSwitchLinkPearl.getCycledRelay(player.inventory.getCurrentItem(), world, pos);
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.relayconfig.confval" + Integer.toString(ItemSwitchLinkPearl.SwitchLink.fromItemStack(player.inventory.getCurrentItem()).relay()), null));
            player.inventory.getCurrentItem().getTagCompound().setLong("cdtime", world.getTotalWorldTime());
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
    if(ck.wrenched && te.click_config(this)) {
      ModAuxiliaries.playerStatusMessage(player, te.configStatusTextComponentTranslation((BlockSwitch)state.getBlock())); // click-config accepted, nothing to do here.
    } else if((config & (SWITCH_CONFIG_LCLICK_RESETTABLE))!=0) {
      te.off_timer_reset();
      if(!state.getValue(POWERED)) return;
      world.setBlockState(pos, state.withProperty(POWERED, false), 1|2);
      power_off_sound.play(world, pos);
    }
    notifyNeighbours(world, pos, state);
  }

  @Override
  protected void onRsBlockDestroyed(IBlockState state, World world, BlockPos pos)
  {
    final TileEntitySwitch te = getTe(world, pos);
    if(te!=null) te.unlink_all(true);
  }

  @Override
  public int tickRate(World world)
  { return 1; }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
  {
    if(((config & SWITCH_CONFIG_BISTABLE)!=0) || (world.isRemote) || (!state.getValue(POWERED))) return;
    final TileEntitySwitch te = getTe(world, pos);
    if((te!=null) && (te.off_timer_tick() > 0) && (!world.isUpdateScheduled(pos, this)) ) { world.scheduleUpdate(pos, this, 1); return; }
    world.setBlockState(pos, (state=state.withProperty(POWERED, false)));
    power_off_sound.play(world, pos);
    notifyNeighbours(world, pos, state);
    if((config & BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
      if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_DEACTIVATE)) {
        ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
      }
    }
  }

  @Override
  public boolean hasTileEntity(IBlockState state)
  { return true; }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state)
  { return new TileEntitySwitch(); }

  public TileEntitySwitch getTe(World world, BlockPos pos)
  {
    if(world==null) return null;
    final TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof TileEntitySwitch))) return null;
    return (TileEntitySwitch)te;
  }

  /**
   * Tile entity
   */
  public static class TileEntitySwitch extends RsTileEntity {
    protected int off_timer_ = 0;
    protected int scd_ = 0; // encoded state data
    protected int svd_ = 0; // encoded value data
    protected long click_config_time_lastclicked_ = 0;
    protected long click_config_last_cycled_ = 0;
    protected long last_link_request_ = 0;
    protected ArrayList<ItemSwitchLinkPearl.SwitchLink> links_ = null;

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      nbt.setInteger("scd", scd_);
      nbt.setInteger("svd", svd_);
      if(updatePacket) return; // server save only
      if((links_==null) || (links_.isEmpty())) {
        if(nbt.hasKey("links")) nbt.removeTag("links");
      } else {
        NBTTagList tl = new NBTTagList();
        for(ItemSwitchLinkPearl.SwitchLink e:links_) tl.appendTag(e.toNbt());
        nbt.setTag("links", tl);
      }
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      int previous_svd = svd_;
      scd_ = nbt.getInteger("scd");
      svd_ = nbt.getInteger("svd");
      if(!updatePacket) {
        if(scd_==0) reset();
        if(!nbt.hasKey("links")) {
          if(links_!=null) links_.clear();
        } else {
          NBTTagList tl = nbt.getTagList("links", nbt.getId()); // getId(): tag type byte of NBTTagCompound, which are saved.
          ArrayList<ItemSwitchLinkPearl.SwitchLink> links = new ArrayList<>();
          for(NBTBase e:tl) {
            ItemSwitchLinkPearl.SwitchLink lnk = ItemSwitchLinkPearl.SwitchLink.fromNbt((NBTTagCompound)e);
            links.add(lnk);
          }
          links_ = links;
        }
      } else {
        if((svd_ & SWITCH_DATA_SVD_COLOR_MASK) != (previous_svd & SWITCH_DATA_SVD_COLOR_MASK)) {
          if((getWorld() != null) && (getWorld().isRemote)) {
            world.markBlockRangeForRenderUpdate(getPos(), getPos());
          }
        }
      }
    }

    @Override
    protected void setWorldCreate(World world)
    { reset(world); }

    public int svd()
    { return svd_; }

    public int scd()
    { return scd_; }

    public int off_timer()
    { return off_timer_; }

    public void off_timer_reset()
    { off_timer_ = 0; }

    public int off_timer_tick()
    { return  ((--off_timer_ <= 0) ? 0 : off_timer_); }

    public int active_time()
    { return ((svd_ & SWITCH_DATA_SVD_ACTIVE_TIME_MASK) >> 0); }

    public void active_time(int t)
    { svd_ = (svd_ & (~SWITCH_DATA_SVD_ACTIVE_TIME_MASK)) | ((t & SWITCH_DATA_SVD_ACTIVE_TIME_MASK) << 0); }

    public void off_timer_reset(int preset)
    { if(preset > 0) off_timer_ = preset; else off_timer_ = 0; }

    public void off_timer_extend()
    {
      if(active_time() > 0) off_timer_ = (active_time()*base_tick_rate)-1;
      else if(off_timer_ > (190/base_tick_rate)) off_timer_  = 400/base_tick_rate;
      else if(off_timer_ > ( 90/base_tick_rate)) off_timer_  = 200/base_tick_rate;
      else if(off_timer_ > ( 30/base_tick_rate)) off_timer_  = 100/base_tick_rate;
      else if(off_timer_ > (  1/base_tick_rate)) off_timer_  =  60/base_tick_rate;
      else off_timer_ = 40/base_tick_rate;
    }

    public int color_tint()
    { return ((svd_ & ((int)SWITCH_DATA_SVD_COLOR_MASK)) >> SWITCH_DATA_SVD_COLOR_SHIFT); }

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

    public void reset()
    { reset(getWorld()); }

    public void reset(World world)
    {
      off_timer_ = 0;
      click_config_time_lastclicked_ = 0;
      svd_ = 0;
      try {
        // If the world is not yet available or the block not loaded let it run with the head
        // into the wall and say 0.
        final int current_scd = scd_;
        scd_ = (int)((((BlockSwitch)(world.getBlockState(getPos()).getBlock())).config) & 0x0fff);
        if((scd_ & 0xff)==0) scd_ |= 15; // implicitly set on-power if not set yet
        if(current_scd != scd_) markDirty();
      } catch(Exception e) {
        // set the on-power to default 15, but no other settings.
        scd_ = 15;
      }
    }

    /**
     * Returns the current power depending on the block settings.
     */
    public int power(IBlockState state, boolean strong)
    { return nooutput() ? (0) : ((strong && weak()) ? (0) : ( (inverted() == state.getValue(POWERED)) ? off_power() : on_power() )); }

    /**
     * Configuration via block right clicking, x and z are normalised
     * coords of the xy position clicked on the main facing, values
     * 0 to 16. Shall return true if the block has to be updated and
     * re-rendered.
     */
    public boolean activation_config(@Nullable BlockSwitch block, @Nullable EntityPlayer player, double x, double y)
    { return false; }

    /**
     * Left double click configuration
     */
    public boolean click_config(@Nullable BlockSwitch block)
    {
      if(block==null) { click_config_time_lastclicked_ = 0; return false; }

      // Check double click
      {
        final long t = System.currentTimeMillis();
        boolean multiclicked = ((t-click_config_time_lastclicked_) > 0) && ((t-click_config_time_lastclicked_) < ModConfig.tweaks.config_left_click_timeout);
        click_config_time_lastclicked_ = multiclicked ? 0 : t;
        if(!multiclicked) return false; // not double clicked
        multiclicked = ((t-click_config_last_cycled_) > 0) && ((t-click_config_last_cycled_) < 3000);
        click_config_last_cycled_ = t;
        if(!multiclicked) return true; // first double click shall only show the unchanged status.
      }

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
      if(ModConfig.optouts.without_switch_nooutput) nooutput(false); // override last option, where not weak and not inverted is already set.
      markDirty();
      return true;
    }

    /**
     * Localisable switch status, normally called on double clicking a switch.
     */
    public TextComponentTranslation configStatusTextComponentTranslation(BlockSwitch block)
    {
      TextComponentString separator = (new TextComponentString(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
      TextComponentTranslation status = ModAuxiliaries.localizable("switchconfig.options", TextFormatting.RESET);
      boolean statusset = false;
      if((on_power() < 15) || (off_power()>0)) {
        if((block == null) || ((block.config & SWITCH_CONFIG_AUTOMATIC)==0)) {
          // power only for non-auto-switches
          statusset = true;
          status.appendSibling(ModAuxiliaries.localizable("switchconfig.options.output_power", TextFormatting.RED, new Object[]{on_power()}));
        }
      }
      if(nooutput()) {
        if(statusset) status.appendSibling(separator.createCopy()); statusset=true;
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options.no_output", TextFormatting.DARK_AQUA));
      } else if(!inverted()) {
        if(statusset) status.appendSibling(separator.createCopy()); statusset=true;
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options." + (weak() ? "weak" : "strong"), TextFormatting.DARK_AQUA));
      } else {
        if(statusset) status.appendSibling(separator.createCopy()); statusset=true;
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options." + (weak() ? "weakinverted" : "stronginverted"), TextFormatting.DARK_AQUA));
      }
      if(active_time() > 0) {
        if(statusset) status.appendSibling(separator.createCopy()); statusset=true;
        status.appendSibling(ModAuxiliaries.localizable("switchconfig.options.pulsetime", TextFormatting.GOLD, new Object[]{
          Double.toString( (((double)(base_tick_rate) * active_time()))/20 ),
          Integer.toString(base_tick_rate * active_time())
        }));
      }
      return status;
    }

    public boolean has_links()
    { return (links_!=null) && (!links_.isEmpty()); }

    /**
     * Called when link pearl requests are sent to the addressed switch. This can be via
     * player pearl usage, from link pearls in switches. The method is used to ensure on
     * the called-side that no recursion or loop lock or other unwanted effects occur.
     * Returns boolean success.
     */
    public boolean check_link_request(final ItemSwitchLinkPearl.SwitchLink link)
    {
      final long t = world.getWorldTime();
      if((world.isRemote) || (last_link_request_ == t)) return false; // not in the same tick, people could try to link A to B and B to A.
      last_link_request_ = t;
      final IBlockState state = world.getBlockState(pos);
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
      final IBlockState state = world.getBlockState(sourcepos);
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
      if(drop) { for(ItemStack e:stacks) world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), e)); }
      return stacks;
    }

    public boolean activate_links(final int req)
    {
      if(ModConfig.optouts.without_switch_linking) return true;
      last_link_request_ = world.getWorldTime();
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
}
