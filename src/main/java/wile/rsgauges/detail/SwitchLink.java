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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
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
    boolean switchLinkHasTargetSupport(Level world, BlockPos pos);

    boolean switchLinkHasSourceSupport(Level world, BlockPos pos);

    boolean switchLinkHasAnalogSupport(Level world, BlockPos pos);

    void switchLinkUnlink(SwitchLink link);

    void switchLinkInit(SwitchLink link);

    default ImmutableList<LinkMode> switchLinkGetSupportedTargetModes()
    { return ImmutableList.of(LinkMode.AS_STATE, LinkMode.ACTIVATE, LinkMode.DEACTIVATE, LinkMode.TOGGLE, LinkMode.INV_STATE); }

    SwitchLink.RequestResult switchLinkTrigger(SwitchLink link);

    Optional<Integer> switchLinkOutputPower(Level world, BlockPos pos);

    Optional<Integer> switchLinkInputPower(Level world, BlockPos pos);

    Optional<Integer> switchLinkComparatorInput(Level world, BlockPos pos);
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
    static LinkMode fromInt(int val) { return BY_INDEX[Mth.clamp((int)val, 0, VALUES.length-1)]; }
    public long toInt()      { return index(); }
  }

  public enum RequestResult { OK, NOT_MATCHED, INVALID_LINKDATA, TOO_FAR, TARGET_GONE, REJECTED }

  public final BlockPos target_position;
  public final String block_name;         // intentionally not Block, as one could not be registered anymore.
  public final boolean valid;
  private long config;
  public int source_analog_power = 0;
  public int source_digital_power = 0;
  public BlockPos source_position = BlockPos.ZERO;
  @Nullable public Level world;
  @Nullable public Player player;

  public SwitchLink()
  { target_position = BlockPos.ZERO; block_name = ""; config = 0; valid = false; }

  public SwitchLink(BlockPos pos, String name, long cfg)
  {
    target_position = pos;
    block_name = name;
    config = cfg;
    valid = (!block_name.isEmpty()) && (pos.asLong()!=0);
  }

  @Override
  public String toString()
  { return "SwitchLink{pos=" + target_position.toString() + ", block='" + block_name + "', config=" + Long.toString(config)+ "}"; }

  public LinkMode mode()
  { return LinkMode.fromInt((int)(config & 0xfL)); }

  public SwitchLink mode(LinkMode rm)
  { config = (config & ~0xfL)|(rm.index()); return this; }

  public static SwitchLink fromNbt(final CompoundTag nbt)
  { return (nbt==null) ? (new SwitchLink()) : (new SwitchLink(BlockPos.of(nbt.getLong("p")), nbt.getString("b"), nbt.getLong("t"))); }

  public static SwitchLink fromItemStack(ItemStack stack)
  { return ((stack==null) || (stack.isEmpty()) || (stack.getItem()!=ModContent.SWITCH_LINK_PEARL)) ? (new SwitchLink()) : (fromNbt(stack.getTag())); }

  public static SwitchLink fromTargetPosition(final Level world, final BlockPos pos)
  {
    if(pos==null) return new SwitchLink();
    final BlockState state = world.getBlockState(pos);
    if((state==null) || (!(state.getBlock() instanceof ISwitchLinkable))) return new SwitchLink();
    if(!((ISwitchLinkable)state.getBlock()).switchLinkHasTargetSupport(world, pos)) return new SwitchLink();
    return new SwitchLink(pos, ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString(), 0);
  }

  public static SwitchLink fromPlayerActiveItem(Level world, Player player)
  {
    if((player==null) || (world.isClientSide()) || (player.getInventory()==null) || (player.getInventory().getSelected()==null)) return new SwitchLink();
    if(player.getInventory().getSelected().getItem()!=ModContent.SWITCH_LINK_PEARL) return null;
    return SwitchLink.fromNbt(player.getInventory().getSelected().getTag());
  }

  public CompoundTag toNbt()
  {
    CompoundTag nbt = new CompoundTag();
    nbt.putString("b", block_name);
    nbt.putLong("t", config);
    nbt.putLong("p", target_position.asLong());
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
  { return ((pos==null) || (!valid)) ? -1 : (int)Math.sqrt(target_position.distSqr(pos)); }

  public boolean isTooFar(final BlockPos pos)
  { return (ModConfig.max_switch_linking_distance > 0) && (((distance(pos) > ModConfig.max_switch_linking_distance))); }

  @SuppressWarnings("deprecation")
  @Nullable
  private ISwitchLinkable target(final Level world, final BlockPos source_pos)
  {
    if((ModConfig.without_switch_linking) || (!valid) || isTooFar(source_pos) || (!world.hasChunkAt(target_position))) return null;
    final BlockState target_state = world.getBlockState(target_position);
    final Block block = target_state.getBlock();
    if((!(block instanceof ISwitchLinkable)) || (!ForgeRegistries.BLOCKS.getKey(block).toString().equals(block_name))) return null;
    return (ISwitchLinkable)block;
  }

  @SuppressWarnings("deprecation")
  public RequestResult trigger(final Level world, final BlockPos source_pos, final Player player)
  {
    if(ModConfig.without_switch_linking) return RequestResult.NOT_MATCHED;
    if((!valid) || (world.isClientSide())) return RequestResult.INVALID_LINKDATA;
    if(isTooFar(source_pos) || ((!world.hasChunkAt(target_position)))) return RequestResult.TOO_FAR;
    final BlockState target_state = world.getBlockState(target_position);
    if(target_state==null) return RequestResult.TOO_FAR;
    final Block block = target_state.getBlock();
    if((!(block instanceof final ISwitchLinkable target)) || (!ForgeRegistries.BLOCKS.getKey(block).toString().equals(block_name))) return RequestResult.TARGET_GONE;
    final int p = target.switchLinkOutputPower(world, target_position).orElse(0);
    this.world = world;
    this.source_position = source_pos;
    this.player = player;
    this.source_analog_power = (p<=0) ? (15) : (0);
    this.source_digital_power = this.source_analog_power;
    return target.switchLinkTrigger(this);
  }

  @SuppressWarnings("deprecation")
  public RequestResult trigger(final Level world, final BlockPos source_pos, int analog_power, int digital_power, boolean state_changed)
  {
    final ISwitchLinkable target = target(world, source_pos);
    if(target==null) return RequestResult.REJECTED;
    this.player = null;
    this.world = world;
    this.source_position = source_pos;
    this.source_analog_power = analog_power;
    this.source_digital_power = digital_power;
    int target_power = target.switchLinkOutputPower(world, target_position).orElse(-1);
    if(target_power<0) return RequestResult.REJECTED; // no target support
    boolean analog = target.switchLinkHasAnalogSupport(world, target_position);
    switch (mode()) {
      case AS_STATE -> {
        if(analog) {
          if(target_power == analog_power) return RequestResult.NOT_MATCHED;
        } else {
          if((!state_changed) || ((target_power == 0) == (digital_power == 0))) return RequestResult.NOT_MATCHED;
        }
      }
      case INV_STATE -> {
        if(analog) {
          analog_power = 15 - analog_power;
          this.source_analog_power = analog_power;
          if(target_power == analog_power) return RequestResult.NOT_MATCHED;
        } else {
          if((!state_changed) || ((target_power == 0) != (digital_power == 0))) return RequestResult.NOT_MATCHED;
        }
      }
      case ACTIVATE -> {
        if((!state_changed) || (digital_power == 0)) return RequestResult.NOT_MATCHED;
      }
      case DEACTIVATE -> {
        if((!state_changed) || (digital_power != 0)) return RequestResult.NOT_MATCHED;
      }
      case TOGGLE -> {
        if(!state_changed) return RequestResult.NOT_MATCHED;
      }
    }
    return target.switchLinkTrigger(this);
  }

  public void unlinkTarget(final Level world, final BlockPos source_pos)
  {
    ISwitchLinkable target = target(world, this.target_position);
    if(target==null) return;
    this.world = world;
    this.source_position = source_pos;
    this.player = null;
    target.switchLinkUnlink(this);
  }

  public void initializeTarget(final Level world, final BlockPos source_pos, int analog_power, int digital_power)
  {
    ISwitchLinkable target = target(world, this.target_position);
    if(target==null) return;
    this.world = world;
    this.source_position = source_pos;
    this.source_analog_power = analog_power;
    this.source_digital_power = digital_power;
    this.player = null;
    target.switchLinkInit(this);
  }

  public static Optional<Integer> getOutputPower(Level world, BlockPos pos)
  {
    BlockState state = world.getBlockState(pos);
    return (!(state.getBlock() instanceof ISwitchLinkable)) ? Optional.empty() : ((ISwitchLinkable)(state.getBlock())).switchLinkOutputPower(world, pos);
  }

  public static Optional<Integer> getInputPower(Level world, BlockPos pos)
  {
    BlockState state = world.getBlockState(pos);
    return (!(state.getBlock() instanceof ISwitchLinkable)) ? Optional.empty() : ((ISwitchLinkable)(state.getBlock())).switchLinkInputPower(world, pos);
  }

  public static Optional<Integer> getComparatorInput(Level world, BlockPos pos)
  {
    BlockState state = world.getBlockState(pos);
    return (!(state.getBlock() instanceof ISwitchLinkable)) ? Optional.empty() : ((ISwitchLinkable)(state.getBlock())).switchLinkComparatorInput(world, pos);
  }

}
