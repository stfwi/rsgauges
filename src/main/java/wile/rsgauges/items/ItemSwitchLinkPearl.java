package wile.rsgauges.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wile.rsgauges.ModAuxiliaries;
import wile.rsgauges.ModConfig;
import wile.rsgauges.ModItems;
import wile.rsgauges.ModResources;
import wile.rsgauges.blocks.BlockSwitch;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSwitchLinkPearl extends RsItem
{
  public ItemSwitchLinkPearl(String registryName)
  { super(registryName); }

  @Override
  @SideOnly(Side.CLIENT)
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
  public boolean canHarvestBlock(IBlockState state, ItemStack stack)
  { return false; }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
  { return false; }

  @Override
  public int getItemBurnTime(ItemStack itemStack)
  { return 0; }

  @Override
  public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack)
  { return true; }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
  {
    final SwitchLink link = SwitchLink.fromItemStack(stack);
    if(!link.valid) return;
    final Block targetBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(link.block_name));
    if(targetBlock!=null) {
      tooltip.add(ModAuxiliaries.localizable(
        "switchlinking.switchlink_pearl.tooltip.linkedblock",
        TextFormatting.GRAY,
        new Object[]{ (new TextComponentString(targetBlock.getLocalizedName())).setStyle((new Style()).setItalic(true).setColor(TextFormatting.YELLOW) )}
      ).getFormattedText());
    }
    if(Minecraft.getMinecraft().player!=null) {
      final int distance = link.distance(Minecraft.getMinecraft().player.getPosition());
      if(distance >= 0) {
        tooltip.add(ModAuxiliaries.localizable(
          "switchlinking.switchlink_pearl.tooltip.linkeddistance",
          TextFormatting.GRAY, new Object[]{distance}).getFormattedText() + (
            (((distance <= ModConfig.zmisc.max_switch_linking_distance) || (ModConfig.zmisc.max_switch_linking_distance<=0))) ? ("")
            : (" " + ModAuxiliaries.localizable("switchlinking.switchlink_pearl.tooltip.toofaraway", TextFormatting.DARK_RED).getFormattedText())
          )
        );
      }
    }
    tooltip.add(ModAuxiliaries.localizable(
      "switchlinking.relayconfig.confval" + Integer.toString(link.relay()),
      TextFormatting.ITALIC
    ).getFormattedText());
  }

  @Override
  public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
  { if(!player.isSneaking()) { return EnumActionResult.PASS; } else { usePearl(world, player); return EnumActionResult.SUCCESS; } }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  { return EnumActionResult.SUCCESS; }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
  { usePearl(world, player); return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand)); }

  public static final void usePearl(World world, EntityPlayer player)
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

  public static final @Nullable ItemStack createFromEnderPearl(World world, BlockPos pos, EntityPlayer player)
  {
    if((world==null) || (player==null) || (pos==null)) return null;
    if((player.inventory==null) || (player.inventory.getCurrentItem()==null) || (player.inventory.getCurrentItem().getItem())!=Items.ENDER_PEARL) return null;
    final IBlockState state = world.getBlockState(pos);
    if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return null;
    final int rl = ((((BlockSwitch)state.getBlock()).config & BlockSwitch.SWITCH_CONFIG_PULSE)!=0) ? SwitchLink.SWITCHLINK_RELAY_ACTIVATE : SwitchLink.SWITCHLINK_RELAY_STATE;
    ItemStack stack = new ItemStack(ModItems.SWITCH_LINK_PEARL, player.inventory.getCurrentItem().getCount());
    stack.setTagCompound(SwitchLink.fromTargetPosition(world, pos).with_relay(rl).toNbt());
    return stack;
  }

  public static final ItemStack getCycledRelay(ItemStack stack, World world, BlockPos target_pos)
  {
    if((world==null) || (stack==null) || (stack.getItem()!=ModItems.SWITCH_LINK_PEARL)) return stack;
    final SwitchLink current_link = SwitchLink.fromItemStack(stack);
    if(!target_pos.equals(current_link.target_position)) return stack;
    final IBlockState state = world.getBlockState(current_link.target_position);
    if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return stack;
    final BlockSwitch block = (BlockSwitch)state.getBlock();
    int next = current_link.relay()+1;
    if(((block.config & BlockSwitch.SWITCH_CONFIG_PULSE)!=0) && ((next < SwitchLink.SWITCHLINK_RELAY_ACTIVATE) || (next > SwitchLink.SWITCHLINK_RELAY_TOGGLE))) next = SwitchLink.SWITCHLINK_RELAY_ACTIVATE;
    SwitchLink lnk = current_link.with_relay((next < SwitchLink.SWITCHLINK_RELAY_EOL) ? next : 0);
    if(!lnk.valid) return stack;
    stack.setTagCompound(lnk.toNbt());
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
    { target_position=BlockPos.ORIGIN; block_name=""; config=SWITCHLINK_CONFIG_DEFAULT; valid=false;}

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

    public static SwitchLink fromNbt(final NBTTagCompound nbt)
    { return (nbt==null) ? (new SwitchLink()) : (new SwitchLink(BlockPos.fromLong(nbt.getLong("p")), nbt.getString("b"), nbt.getLong("t"))); }

    public static SwitchLink fromItemStack(ItemStack stack)
    { return ((stack==null) || (stack.isEmpty()) || (stack.getItem()!=ModItems.SWITCH_LINK_PEARL)) ? (new SwitchLink()) : (fromNbt(stack.getTagCompound())); }

    public static SwitchLink fromTargetPosition(final World world, final BlockPos targetPos)
    {
      if(targetPos==null) return new SwitchLink();
      final IBlockState state = world.getBlockState(targetPos);
      if((state==null) || (!(state.getBlock() instanceof BlockSwitch))) return new SwitchLink();
      final BlockSwitch block = (BlockSwitch)state.getBlock();
      if((block.config & BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT)==0) return new SwitchLink();
      return new SwitchLink(targetPos, block.getRegistryName().toString(), SWITCHLINK_CONFIG_DEFAULT);
    }

    public static SwitchLink fromPlayerActiveItem(World world, EntityPlayer player)
    {
      if((player==null) || (world.isRemote) || (player.inventory==null) || (player.inventory.getCurrentItem()==null)) return new SwitchLink();
      if(player.inventory.getCurrentItem().getItem()!=ModItems.SWITCH_LINK_PEARL) return null;
      return SwitchLink.fromNbt(player.inventory.getCurrentItem().getTagCompound());
    }

    public NBTTagCompound toNbt()
    {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setString("b", block_name);
      nbt.setLong("t", config);
      nbt.setLong("p", target_position.toLong());
      return nbt;
    }

    public ItemStack toSwitchLinkPearl()
    {
      ItemStack stack = new ItemStack(ModItems.SWITCH_LINK_PEARL);
      stack.setCount(1);
      stack.setTagCompound(toNbt());
      return stack;
    }

    public int distance(@Nullable final BlockPos pos)
    { return ((pos==null) || (!valid)) ? -1 : (int)Math.sqrt(target_position.distanceSq(pos)); }

    public boolean isTooFar(final BlockPos pos)
    { return (((distance(pos) > ModConfig.zmisc.max_switch_linking_distance) && (ModConfig.zmisc.max_switch_linking_distance > 0))); }

    public RequestResult request(final int req, final World world, final BlockPos source_pos, final @Nullable EntityPlayer player)
    {
      // Preconditions
      if(ModConfig.optouts.without_switch_linking) return RequestResult.REJECTED;
      if(!valid) return RequestResult.INVALID_LINKDATA;
      if(isTooFar(source_pos)) return RequestResult.TOO_FAR;
      if(!world.isBlockLoaded(target_position)) return RequestResult.BLOCK_UNLOADED;
      final IBlockState target_state=world.getBlockState(target_position);
      if(target_state==null) return RequestResult.BLOCK_UNLOADED;
      final Block block = target_state.getBlock();
      if((!(block instanceof BlockSwitch)) || (!block.getRegistryName().toString().equals(block_name))) return RequestResult.TARGET_GONE;
      final BlockSwitch target_block = (BlockSwitch)block;

      // Match incoming request to the link config for requests coming from source switches
      if(player==null) {
        switch(relay()) {
          case SWITCHLINK_RELAY_STATE:
          case SWITCHLINK_RELAY_STATE_INV: {
            boolean powered = target_state.getValue(BlockSwitch.POWERED);
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
