package wile.rsgauges.items;

import net.minecraft.item.Item;
import net.minecraft.util.text.*;
import net.minecraftforge.registries.ForgeRegistries;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModConfig;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.blocks.BlockSwitch;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSwitchLinkPearl extends RsItem
{
  public ItemSwitchLinkPearl(Item.Properties properties)
  { super(properties); }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack)
  { return false; }

  @Override
  public boolean isEnchantable(ItemStack stack)
  { return false; }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book)
  { return false; }

  @Override
  public boolean showDurabilityBar(ItemStack stack)
  { return false; }

  @Override
  public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
  { return false; }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
  { return false; }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
  {
    final SwitchLink link = SwitchLink.fromItemStack(stack);
    if(ModAuxiliaries.Tooltip.addInformation(stack, world, tooltip, flag, (!link.valid))) return;
    if(!link.valid) return;
    final Block targetBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(link.block_name));
    if(targetBlock!=null) {
      tooltip.add(ModAuxiliaries.localizable(
        "switchlinking.switchlink_pearl.tooltip.linkedblock",
        TextFormatting.GRAY,
        new Object[]{ (new TranslationTextComponent(targetBlock.getTranslationKey())).setStyle((new Style()).setItalic(true).setColor(TextFormatting.YELLOW) )}
      ));
    }
    if(Minecraft.getInstance().player!=null) {
      final int distance = link.distance(Minecraft.getInstance().player.getPosition());
      if(distance >= 0) {
        tooltip.add(new StringTextComponent(ModAuxiliaries.localizable(
          "switchlinking.switchlink_pearl.tooltip.linkeddistance",
          TextFormatting.GRAY, new Object[]{distance}).getFormattedText() + (
            (((distance <= ModConfig.max_switch_linking_distance) || (ModConfig.max_switch_linking_distance<=0))) ? ("")
            : (" " + ModAuxiliaries.localizable("switchlinking.switchlink_pearl.tooltip.toofaraway", TextFormatting.DARK_RED).getFormattedText())
          )
        ));
      }
    }
    tooltip.add(ModAuxiliaries.localizable(
      "switchlinking.relayconfig.confval" + Integer.toString(link.relay()),
      TextFormatting.ITALIC
    ));
    super.addInformation(stack, world, tooltip, flag);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
  {
    if(world.isRemote() || (!player.func_225608_bj_()/*isSneaking()*/)) {
      return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
    } else {
      usePearl(world, player);
      return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }
  }

  public static final void usePearl(World world, PlayerEntity player)
  {
    switch(SwitchLink.fromPlayerActiveItem(world, player).request(SwitchLink.SWITCHLINK_RELAY_ACTIVATE, world, player.getPosition(), player)) {
      case OK:
        ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_SUCCESS.play(world, player.getPosition());
        return;
      case TOO_FAR:
      case BLOCK_UNLOADED:
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.switchlink_pearl.use.toofaraway", TextFormatting.DARK_RED));
        break;
      case TARGET_GONE:
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchlinking.switchlink_pearl.use.targetgone", TextFormatting.DARK_RED));
        break;
      case NOT_MATCHED: // Can't happen here
      case INVALID_LINKDATA:
      case REJECTED:
        break;
    }
    ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, player.getPosition());
  }

  public static final @Nullable ItemStack createFromEnderPearl(World world, BlockPos pos, PlayerEntity player)
  {
    if((world==null) || (player==null) || (pos==null)) return null;
    if((player.inventory==null) || (player.inventory.getCurrentItem()==null) || (player.inventory.getCurrentItem().getItem())!=Items.ENDER_PEARL) return null;
    final BlockState state = world.getBlockState(pos);
    if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return null;
    final int rl = ((((BlockSwitch)state.getBlock()).config & BlockSwitch.SWITCH_CONFIG_PULSE)!=0) ? SwitchLink.SWITCHLINK_RELAY_ACTIVATE : SwitchLink.SWITCHLINK_RELAY_STATE;
    ItemStack stack = new ItemStack(ModContent.SWITCH_LINK_PEARL, player.inventory.getCurrentItem().getCount());
    stack.setTag(SwitchLink.fromTargetPosition(world, pos).with_relay(rl).toNbt());
    return stack;
  }

  public static final ItemStack getCycledRelay(ItemStack stack, World world, BlockPos target_pos)
  {
    if((world==null) || (stack==null) || (stack.getItem()!=ModContent.SWITCH_LINK_PEARL)) return stack;
    final SwitchLink current_link = SwitchLink.fromItemStack(stack);
    if(!target_pos.equals(current_link.target_position)) return stack;
    final BlockState state = world.getBlockState(current_link.target_position);
    if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return stack;
    final BlockSwitch block = (BlockSwitch)state.getBlock();
    int next = current_link.relay()+1;
    if(((block.config & BlockSwitch.SWITCH_CONFIG_PULSE)!=0) && ((next < SwitchLink.SWITCHLINK_RELAY_ACTIVATE) || (next > SwitchLink.SWITCHLINK_RELAY_TOGGLE))) next = SwitchLink.SWITCHLINK_RELAY_ACTIVATE;
    SwitchLink lnk = current_link.with_relay((next < SwitchLink.SWITCHLINK_RELAY_EOL) ? next : 0);
    if(!lnk.valid) return stack;
    stack.setTag(lnk.toNbt());
    return stack;
  }

  /**
   * Class representing the link functionality of the switch link pearls.
   * It has no purpose without this item, for which it is embedded in the item class.
   */
  public static class SwitchLink
  {
    public static final long SWITCHLINK_CONFIG_DEFAULT          = 0x0000000000000000;
    public static final long SWITCHLINK_CONFIG_RELAY_MASK       = 0x0000000000000007;
    public static final int  SWITCHLINK_RELAY_STATE             = 0x0;
    public static final int  SWITCHLINK_RELAY_ACTIVATE          = 0x1;
    public static final int  SWITCHLINK_RELAY_DEACTIVATE        = 0x2;
    public static final int  SWITCHLINK_RELAY_TOGGLE            = 0x3;
    public static final int  SWITCHLINK_RELAY_STATE_INV         = 0x4;
    public static final int  SWITCHLINK_RELAY_EOL               = 0x5;

    public enum RequestResult { OK, NOT_MATCHED, INVALID_LINKDATA, TOO_FAR, BLOCK_UNLOADED, TARGET_GONE, REJECTED }
    public final BlockPos target_position;
    public final String block_name;         // intentionally not Block, as this one could not be registered.
    public final long config;
    public final boolean valid;

    public SwitchLink()
    { target_position=BlockPos.ZERO; block_name=""; config=SWITCHLINK_CONFIG_DEFAULT; valid=false;}

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

    public int relay()
    { final int r=(int)(config & SWITCHLINK_CONFIG_RELAY_MASK); return (r<SWITCHLINK_RELAY_EOL) ? r : 0; }

    public SwitchLink with_relay(int conf)
    { return new SwitchLink(target_position, block_name, (config & ~SWITCHLINK_CONFIG_RELAY_MASK)|(conf & SWITCHLINK_CONFIG_RELAY_MASK)); }

    public static SwitchLink fromNbt(final CompoundNBT nbt)
    { return (nbt==null) ? (new SwitchLink()) : (new SwitchLink(BlockPos.fromLong(nbt.getLong("p")), nbt.getString("b"), nbt.getLong("t"))); }

    public static SwitchLink fromItemStack(ItemStack stack)
    { return ((stack==null) || (stack.isEmpty()) || (stack.getItem()!=ModContent.SWITCH_LINK_PEARL)) ? (new SwitchLink()) : (fromNbt(stack.getTag())); }

    public static SwitchLink fromTargetPosition(final World world, final BlockPos targetPos)
    {
      if(targetPos==null) return new SwitchLink();
      final BlockState state = world.getBlockState(targetPos);
      if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return new SwitchLink();
      final BlockSwitch block = (BlockSwitch)state.getBlock();
      if((block.config & BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT)==0) return new SwitchLink();
      return new SwitchLink(targetPos, block.getRegistryName().toString(), SWITCHLINK_CONFIG_DEFAULT);
    }

    public static SwitchLink fromPlayerActiveItem(World world, PlayerEntity player)
    {
      if((player==null) || (world.isRemote) || (player.inventory==null) || (player.inventory.getCurrentItem()==null)) return new SwitchLink();
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
    { return (((distance(pos) > ModConfig.max_switch_linking_distance) && (ModConfig.max_switch_linking_distance > 0))); }

    @SuppressWarnings("deprecation")
    public RequestResult request(final int req, final World world, final BlockPos source_pos, final @Nullable PlayerEntity player)
    {
      // Preconditions
      if(ModConfig.without_switch_linking) return RequestResult.REJECTED;
      if(!valid) return RequestResult.INVALID_LINKDATA;
      if(isTooFar(source_pos)) return RequestResult.TOO_FAR;
      if(!world.isBlockLoaded(target_position)) return RequestResult.BLOCK_UNLOADED;
      final BlockState target_state=world.getBlockState(target_position);
      if(target_state==null) return RequestResult.BLOCK_UNLOADED;
      final Block block = target_state.getBlock();
      if((!(block instanceof BlockSwitch)) || (!block.getRegistryName().toString().equals(block_name))) return RequestResult.TARGET_GONE;
      final BlockSwitch target_block = (BlockSwitch)block;

      // Match incoming request to the link config for requests coming from source switches
      if(player==null) {
        switch(relay()) {
          case SWITCHLINK_RELAY_STATE:
          case SWITCHLINK_RELAY_STATE_INV: {
            boolean powered = target_state.get(BlockSwitch.POWERED);
            if(relay()==SWITCHLINK_RELAY_STATE_INV) powered = !powered;
            if(req == SWITCHLINK_RELAY_ACTIVATE) {
              if(powered) return RequestResult.NOT_MATCHED;
            } else if(req == SWITCHLINK_RELAY_DEACTIVATE) {
              if(!powered) return RequestResult.NOT_MATCHED;
              // Special case for all pulse switches. The default behaviour is
              // to activate when rising edge, but not explicitly deactivate
              // on falling edges. This opens some more usage options for the
              // player, and is probably also what the player expects as default,
              // if anything can be actually expected with these link mechanisms.
              if((target_block.config & BlockSwitch.SWITCH_CONFIG_PULSE)!=0) return RequestResult.NOT_MATCHED;
            }
          } break;
          case SWITCHLINK_RELAY_ACTIVATE:
            if(req != SWITCHLINK_RELAY_ACTIVATE) return RequestResult.NOT_MATCHED;
            break;
          case SWITCHLINK_RELAY_DEACTIVATE:
            if(req != SWITCHLINK_RELAY_DEACTIVATE) return RequestResult.NOT_MATCHED;
            break;
          case SWITCHLINK_RELAY_TOGGLE:
            if((req != SWITCHLINK_RELAY_ACTIVATE) && (req != SWITCHLINK_RELAY_DEACTIVATE)) return RequestResult.NOT_MATCHED;
            break;
        }
      }
      // Invoke delegated handling in the target switch
      return (target_block.onLinkRequest(this, req, world, target_position, player)) ? (RequestResult.OK) : (RequestResult.REJECTED);
    }

  }
}
