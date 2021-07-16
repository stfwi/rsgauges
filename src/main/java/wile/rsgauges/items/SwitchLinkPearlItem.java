package wile.rsgauges.items;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import wile.rsgauges.ModContent;
import wile.rsgauges.ModConfig;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.SwitchLink;
import wile.rsgauges.detail.SwitchLink.ISwitchLinkable;
import wile.rsgauges.libmc.detail.Overlay;
import wile.rsgauges.libmc.detail.Auxiliaries;

import javax.annotation.Nullable;
import java.util.List;


public class SwitchLinkPearlItem extends RsItem
{
  public SwitchLinkPearlItem(Item.Properties properties)
  { super(properties); }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isFoil(ItemStack stack)
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
  public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
  { return false; }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
  { return false; }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
  {
    final SwitchLink link = SwitchLink.fromItemStack(stack);
    if(Auxiliaries.Tooltip.addInformation(stack, world, tooltip, flag, (!link.valid))) return;
    if(!link.valid) return;
    final Block targetBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(link.block_name));
    if(targetBlock!=null) {
      tooltip.add(Auxiliaries.localizable(
        "switchlinking.switchlink_pearl.tooltip.linkedblock",
        TextFormatting.GRAY,
        new Object[]{ (new TranslationTextComponent(targetBlock.getDescriptionId()))
          .withStyle(TextFormatting.YELLOW)
          .withStyle(TextFormatting.ITALIC)
        }
      ));
    }
    if(Minecraft.getInstance().player!=null) {
      final int distance = link.distance(Minecraft.getInstance().player.blockPosition());
      if(distance >= 0) {
        tooltip.add(new StringTextComponent(Auxiliaries.localizable(
          "switchlinking.switchlink_pearl.tooltip.linkeddistance",
          TextFormatting.GRAY, new Object[]{distance}).getString() + (
            (((distance <= ModConfig.max_switch_linking_distance) || (ModConfig.max_switch_linking_distance<=0))) ? ("")
            : (" " + Auxiliaries.localizable("switchlinking.switchlink_pearl.tooltip.toofaraway", TextFormatting.DARK_RED).getString())
          )
        ));
      }
    }
    tooltip.add(Auxiliaries.localizable(
      "switchlinking.relayconfig.confval" + Integer.toString(link.mode().index()),
      TextFormatting.ITALIC
    ));
    super.appendHoverText(stack, world, tooltip, flag);
  }

  @Override
  public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
  {
    if(world.isClientSide() || (!player.isShiftKeyDown())) {
      return new ActionResult<>(ActionResultType.PASS, player.getItemInHand(hand));
    } else {
      usePearl(world, player);
      return new ActionResult<>(ActionResultType.CONSUME, player.getItemInHand(hand));
    }
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
  {
    if((!selected) || (!entity.level.isClientSide()) || (world.getRandom().nextDouble() > 0.3)) return;
    final SwitchLink lnk = SwitchLink.fromItemStack(stack);
    if((!lnk.valid) || (lnk.target_position.distSqr(entity.blockPosition()) > 900)) return;
    Vector3d p = Vector3d.atLowerCornerOf(lnk.target_position).add(
      ((world.getRandom().nextDouble()-0.5)*0.2),
      ((world.getRandom().nextDouble()-0.5)*0.2),
      ((world.getRandom().nextDouble()-0.5)*0.2)
    );
    Vector3d v = new Vector3d(0, ((world.getRandom().nextDouble()-0.5)*0.001), 0);
    BlockState state = world.getBlockState(lnk.target_position);
    if((state==null) || (!(state.getBlock() instanceof ISwitchLinkable)) ) return;
    p = p.add(state.getShape(world, lnk.target_position).bounds().getCenter());
    int power = ((ISwitchLinkable)(state.getBlock())).switchLinkOutputPower(world, lnk.target_position).orElse(0);
    if(power > 0) {
      world.addParticle((IParticleData)ParticleTypes.INSTANT_EFFECT, false, p.x, p.y, p.z, v.x, v.y, v.z);
    } else {
      world.addParticle((IParticleData)ParticleTypes.WITCH, false, p.x, p.y, p.z, v.x, v.y, v.z);
    }
  }

  public static final void usePearl(World world, PlayerEntity player)
  {
    switch(SwitchLink.fromPlayerActiveItem(world, player).trigger(world, player.blockPosition(), player)) {
      case OK:
        ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_SUCCESS.play(world, player.blockPosition());
        return;
      case TOO_FAR:
        Overlay.show(player, Auxiliaries.localizable("switchlinking.switchlink_pearl.use.toofaraway", TextFormatting.DARK_RED));
        break;
      case TARGET_GONE:
        Overlay.show(player, Auxiliaries.localizable("switchlinking.switchlink_pearl.use.targetgone", TextFormatting.DARK_RED));
        break;
      case NOT_MATCHED: // Can't happen here
      case INVALID_LINKDATA:
      case REJECTED:
        break;
    }
    ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, player.blockPosition());
  }


  public static final ItemStack createFromPearl(World world, BlockPos pos, PlayerEntity player)
  {
    final ItemStack stack_held = player.inventory.getSelected();
    if(stack_held.isEmpty()) return ItemStack.EMPTY;
    final ItemStack link_pearl = createForTarget(world, pos);
    if(link_pearl.isEmpty()) return ItemStack.EMPTY;
    link_pearl.getTag().putLong("cdtime", world.getGameTime());
    if(stack_held.getCount() > 1) {
      // @todo: move shrinked ender pearl stack into another slot
      link_pearl.setCount(stack_held.getCount());
    }
    return link_pearl;
  }

  public static final ItemStack createForTarget(World world, BlockPos pos)
  {
    final BlockState state = world.getBlockState(pos);
    if(!(state.getBlock() instanceof SwitchLink.ISwitchLinkable)) return ItemStack.EMPTY;
    ItemStack stack = new ItemStack(ModContent.SWITCH_LINK_PEARL);
    final SwitchLink.LinkMode mode = ((SwitchLink.ISwitchLinkable)state.getBlock()).switchLinkGetSupportedTargetModes().get(0);
    stack.setTag(SwitchLink.fromTargetPosition(world, pos).mode(mode).toNbt());
    return stack;
  }

  public static final boolean cycleLinkMode(ItemStack stack, World world, BlockPos target_pos, boolean with_click_time)
  {
    SwitchLink lnk = SwitchLink.fromItemStack(stack);
    if(!target_pos.equals(lnk.target_position)) return false;
    final BlockState state = world.getBlockState(lnk.target_position);
    if((!(state.getBlock() instanceof SwitchLink.ISwitchLinkable))) return false;
    final long t = world.getGameTime();
    if(with_click_time) {
      final long dt = Math.abs(t-stack.getTag().getLong("cdtime"));
      if(dt < 7) return true;
      if(dt > 40) { stack.getTag().putLong("cdtime", t); return true; }
    }
    final SwitchLink.ISwitchLinkable target = (SwitchLink.ISwitchLinkable)(state.getBlock());
    ImmutableList<SwitchLink.LinkMode> modes = target.switchLinkGetSupportedTargetModes();
    int index = modes.indexOf(lnk.mode())+1;
    SwitchLink.LinkMode next = modes.get((index<0)||(index>=modes.size()) ? 0 : index);
    lnk.mode(next);
    if(!lnk.valid) return false;
    stack.setTag(lnk.toNbt());
    stack.getTag().putLong("cdtime", t);
    return true;
  }

}
