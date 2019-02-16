/*
 * @file BlockAutoSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wile.rsgauges.items.ItemSwitchLinkPearl;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDimmerSwitch extends BlockSwitch
{
  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

  public BlockDimmerSwitch(String registryName, AxisAlignedBB unrotatedBB, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound, @Nullable Material material)
  { super(registryName, unrotatedBB, null, config|0xff, powerOnSound, powerOffSound, material); }

  @Override
  public int tickRate(World world)
  { return 200; }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
  {}

  @Override
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, FACING, POWERED, POWER); }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
  { TileEntitySwitch te = getTe(world, pos); return (te==null) ? (state) : (state.withProperty(POWER, te.on_power())); }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  {
    if(world.isRemote || (!(state.getBlock() instanceof BlockDimmerSwitch))) return true;
    TileEntitySwitch te = getTe(world, pos);
    if((te==null) || (state==null)) return true;
    te.click_config(null, false);
    ClickInteraction ck = ClickInteraction.get(world, pos, state, player, hand, facing, hitX, hitY, hitZ, true);
    boolean was_powered = te.on_power()!=0;
    if(ck.touch_configured) {
      int p = (int)ck.y;
      if(p != te.on_power()) {
        te.on_power(p);
        p = te.on_power();
        ModAuxiliaries.playerStatusMessage(player,
          ModAuxiliaries.localizable("switchconfig.dimmerswitch.output_power", TextFormatting.RED, new Object[]{p})
        );
        final int state_p = state.getValue(POWER);
        if(state_p!=p) {
          world.setBlockState(pos, state.withProperty(POWER, p).withProperty(POWERED, p>0), 1|2);
          notifyNeighbours(world, pos, state, te, false);
          te.markDirty();
        }
        if(was_powered && (p==0)) power_off_sound.play(world, pos); else power_on_sound.play(world, pos);
        if(((was_powered) == (p==0)) && (((config & SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0)) ) {
          if(!was_powered) {
            // Fire link requests when changing from unpowered to powered.
            if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_ACTIVATE)) {
              ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
            }
          }
        }
      }
      return true;
    } else if(ck.wrenched) {
      if(te.click_config(this, false)) {
        ModAuxiliaries.playerStatusMessage(player, te.configStatusTextComponentTranslation((BlockSwitch) state.getBlock()));
      }
    }
    return true;
  }
}
