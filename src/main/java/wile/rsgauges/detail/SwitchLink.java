/*
 * @file SwitchLink.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Wireless switching event transmission.
 */
package wile.rsgauges.detail;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import wile.rsgauges.ModConfig;
import wile.rsgauges.ModContent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * Class representing the link functionality of the switch link pearls.
 * It has no purpose without this item, for which it is embedded in the item class.
 */
public class SwitchLink
{
  public interface ISwitchLinkable
  {
    boolean switchLinkHasTargetSupport(World world, BlockPos pos);

    boolean switchLinkHasSourceSupport(World world, BlockPos pos);

    boolean switchLinkHasAnalogSupport(World world, BlockPos pos);

    void switchLinkUnlink(SwitchLink link);

    void switchLinkInit(SwitchLink link);

    default ImmutableList<LinkMode> switchLinkGetSupportedTargetModes()
    { return ImmutableList.of(LinkMode.AS_STATE, LinkMode.ACTIVATE, LinkMode.DEACTIVATE, LinkMode.TOGGLE, LinkMode.INV_STATE); }

    SwitchLink.RequestResult switchLinkTrigger(SwitchLink link);

    Optional<Integer> switchLinkOutputPower(World world, BlockPos pos);

    Optional<Integer> switchLinkInputPower(World world, BlockPos pos);

    Optional<Integer> switchLinkComparatorInput(World world, BlockPos pos);
  }

  public enum LinkMode
  {
    AS_STATE(0x0),
    ACTIVATE(0x1),
    DEACTIVATE(0x2),
    TOGGLE(0x3),
    INV_STATE(0x4);
    private static final LinkMode[] VALUES = values();
    private static final LinkMode[] BY_INDEX = Arrays.stream(values()).sorted(Comparator.comparingInt(v->v.index_)).toArray(LinkMode[]::new);
    private final int index_;
    LinkMode(int i)          { index_ = i; }
    public int index()       { return index_; }
    static LinkMode fromInt(int val) { return BY_INDEX[MathHelper.clamp((int)val, 0, VALUES.length-1)]; }
    public long toInt()      { return index(); }
  }

  public enum RequestResult { OK, NOT_MATCHED, INVALID_LINKDATA, TOO_FAR, TARGET_GONE, REJECTED }

  public final BlockPos target_position;
  public final String block_name;         // intentionally not Block, as one could not be registered anymore.
  public final boolean valid;
  private long config;
  public int source_power = 0;
  public BlockPos source_position = BlockPos.ZERO;
  @Nullable public World world;
  @Nullable public PlayerEntity player;

  public SwitchLink()
  { target_position = BlockPos.ZERO; block_name = ""; config = 0; valid = false; }

  public SwitchLink(BlockPos pos, String name, long cfg)
  {
    target_position = pos;
    block_name = name;
    config = cfg;
    valid = (!block_name.isEmpty()) && (pos.toLong()!=0);
  }

  @Override
  public String toString()
  { return "SwitchLink{pos=" + target_position.toString() + ", block='" + block_name + "', config=" + Long.toString(config)+ "}"; }

  public LinkMode mode()
  { return LinkMode.fromInt((int)(config & 0xfL)); }

  public SwitchLink mode(LinkMode rm)
  { config = (config & ~0xfL)|(rm.index()); return this; }

  public static SwitchLink fromNbt(final CompoundNBT nbt)
  { return (nbt==null) ? (new SwitchLink()) : (new SwitchLink(BlockPos.fromLong(nbt.getLong("p")), nbt.getString("b"), nbt.getLong("t"))); }

  public static SwitchLink fromItemStack(ItemStack stack)
  { return ((stack==null) || (stack.isEmpty()) || (stack.getItem()!=ModContent.SWITCH_LINK_PEARL)) ? (new SwitchLink()) : (fromNbt(stack.getTag())); }

  public static SwitchLink fromTargetPosition(final World world, final BlockPos pos)
  {
    if(pos==null) return new SwitchLink();
    final BlockState state = world.getBlockState(pos);
    if((state==null) || (!(state.getBlock() instanceof ISwitchLinkable))) return new SwitchLink();
    if(!((ISwitchLinkable)state.getBlock()).switchLinkHasTargetSupport(world, pos)) return new SwitchLink();
    return new SwitchLink(pos, state.getBlock().getRegistryName().toString(), 0);
  }

  public static SwitchLink fromPlayerActiveItem(World world, PlayerEntity player)
  {
    if((player==null) || (world.isRemote()) || (player.inventory==null) || (player.inventory.getCurrentItem()==null)) return new SwitchLink();
    if(player.inventory.getCurrentItem().getItem()!=ModContent.SWITCH_LINK_PEARL) return null;
    return SwitchLink.fromNbt(player.inventory.getCurrentItem().getTag());
  }

  public CompoundNBT toNbt()
  {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putString("b", block_name);
    nbt.putLong("t", config);
    nbt.putLong("p", target_position.toLong());
    return nbt;
  }

  public ItemStack toSwitchLinkPearl()
  {
    ItemStack stack = new ItemStack(ModContent.SWITCH_LINK_PEARL);
    stack.setCount(1);
    stack.setTag(toNbt());
    return stack;
  }

  public int distance(@Nullable final BlockPos pos)
  { return ((pos==null) || (!valid)) ? -1 : (int)Math.sqrt(target_position.distanceSq(pos)); }

  public boolean isTooFar(final BlockPos pos)
  { return (ModConfig.max_switch_linking_distance > 0) && (((distance(pos) > ModConfig.max_switch_linking_distance))); }

  @SuppressWarnings("deprecation")
  @Nullable
  private ISwitchLinkable target(final World world, final BlockPos source_pos)
  {
    if((ModConfig.without_switch_linking) || (!valid) || isTooFar(source_pos) || (!world.isBlockLoaded(target_position))) return null;
    final BlockState target_state = world.getBlockState(target_position);
    final Block block = target_state.getBlock();
    if((!(block instanceof ISwitchLinkable)) || (!block.getRegistryName().toString().equals(block_name))) return null;
    return (ISwitchLinkable)block;
  }

  @SuppressWarnings("deprecation")
  public RequestResult trigger(final World world, final BlockPos source_pos, final PlayerEntity player)
  {
    if(ModConfig.without_switch_linking) return RequestResult.NOT_MATCHED;
    if((!valid) || (world.isRemote())) return RequestResult.INVALID_LINKDATA;
    if(isTooFar(source_pos) || ((!world.isBlockLoaded(target_position)))) return RequestResult.TOO_FAR;
    final BlockState target_state = world.getBlockState(target_position);
    if(target_state==null) return RequestResult.TOO_FAR;
    final Block block = target_state.getBlock();
    if((!(block instanceof ISwitchLinkable)) || (!block.getRegistryName().toString().equals(block_name))) return RequestResult.TARGET_GONE;
    final ISwitchLinkable target = (ISwitchLinkable)block;
    final int p = target.switchLinkOutputPower(world, target_position).orElse(0);
    this.world = world;
    this.source_position = source_pos;
    this.player = player;
    this.source_power = (p<=0) ? (15) : (0);
    return target.switchLinkTrigger(this);
  }

  @SuppressWarnings("deprecation")
  public RequestResult trigger(final World world, final BlockPos source_pos, int power, boolean state_changed)
  {
    final ISwitchLinkable target = target(world, source_pos);
    if(target==null) return RequestResult.REJECTED;
    this.player = null;
    this.world = world;
    this.source_position = source_pos;
    this.source_power = power;
    int target_power = target.switchLinkOutputPower(world, target_position).orElse(-1);
    if(target_power<0) return RequestResult.REJECTED;
    boolean analog = target.switchLinkHasAnalogSupport(world, target_position);
    switch(mode()) {
      case AS_STATE: {
        if(analog) {
          if(target_power == power) return RequestResult.NOT_MATCHED;
        } else {
          if((target_power==0) == (power==0)) return RequestResult.NOT_MATCHED;
        }
      } break;
      case INV_STATE: {
        if(analog) {
          power = 15 - power;
          this.source_power = power;
          if(target_power == power) return RequestResult.NOT_MATCHED;
        } else {
          if((target_power==0) != (power==0)) return RequestResult.NOT_MATCHED;
        }
      } break;
      case ACTIVATE: {
        if((!state_changed) || (power==0)) return RequestResult.NOT_MATCHED;
      } break;
      case DEACTIVATE: {
        if((!state_changed) || (power!=0)) return RequestResult.NOT_MATCHED;
      } break;
      case TOGGLE: {
        if(!state_changed) return RequestResult.NOT_MATCHED;
      } break;
    }
    return target.switchLinkTrigger(this);
  }

  public void unlinkTarget(final World world, final BlockPos source_pos)
  {
    ISwitchLinkable target = target(world, this.target_position);
    if(target==null) return;
    this.world = world;
    this.source_position = source_pos;
    this.player = null;
    target.switchLinkUnlink(this);
  }

  public void initializeTarget(final World world, final BlockPos source_pos, int source_power)
  {
    ISwitchLinkable target = target(world, this.target_position);
    if(target==null) return;
    this.world = world;
    this.source_position = source_pos;
    this.source_power = source_power;
    this.player = null;
    target.switchLinkInit(this);
  }

  public static Optional<Integer> getOutputPower(World world, BlockPos pos)
  {
    BlockState state = world.getBlockState(pos);
    return (!(state.getBlock() instanceof ISwitchLinkable)) ? Optional.empty() : ((ISwitchLinkable)(state.getBlock())).switchLinkOutputPower(world, pos);
  }

  public static Optional<Integer> getInputPower(World world, BlockPos pos)
  {
    BlockState state = world.getBlockState(pos);
    return (!(state.getBlock() instanceof ISwitchLinkable)) ? Optional.empty() : ((ISwitchLinkable)(state.getBlock())).switchLinkInputPower(world, pos);
  }

  public static Optional<Integer> getComparatorInput(World world, BlockPos pos)
  {
    BlockState state = world.getBlockState(pos);
    return (!(state.getBlock() instanceof ISwitchLinkable)) ? Optional.empty() : ((ISwitchLinkable)(state.getBlock())).switchLinkComparatorInput(world, pos);
  }

}
