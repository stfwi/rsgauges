/*
 * @file BlockComparatorSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Auto switch specialised for day timer clocks.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.ModContent;
import wile.rsgauges.ModConfig;
import wile.rsgauges.blocks.EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import net.minecraft.util.Direction;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.*;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class ComparatorSwitchBlock extends AutoSwitchBlock
{
  public ComparatorSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public ComparatorSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
  {
    if(!isAffectedByNeigbour(state, world, pos, fromPos)) return;
    final SwitchTileEntity te = getTe(world, pos);
    if(!(te instanceof ComparatorSwitchTileEntity)) return;
    ((ComparatorSwitchTileEntity)te).block_updated();
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new ComparatorSwitchTileEntity(ModContent.TET_COMPARATOR_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public static class ComparatorSwitchTileEntity extends EnvironmentalSensorSwitchTileEntity implements ITickableTileEntity
  {
    public ComparatorSwitchTileEntity(TileEntityType<?> te_type)
    { super(te_type); }

    public ComparatorSwitchTileEntity()
    { super(ModContent.TET_COMPARATOR_SWITCH); }

    private interface Acquisition { int sample(World world, BlockPos pos, BlockState state, Direction side); }

    private static final Acquisition acquisitions[] = {
      // 0: Comparator input override
      (world, pos, state, side) -> {
        return (!state.hasComparatorInputOverride()) ? (-1) : (state.getComparatorInputOverride(world, pos));
      },
      // 1: Used inventory slots
      (world, pos, state, side) -> {
        final TileEntity te = world.getTileEntity(pos);
        if(te==null) {
          return -1;
        } else if(te instanceof IInventory) {
          IInventory inventory = (IInventory)te;
          final int size = inventory.getSizeInventory();
          int n = 0;
          for(int i=0; i<size; ++i) n += inventory.getStackInSlot(i).isEmpty() ? 0 : 1;
          return (int)Math.round(((double)n*15)/(double)size);
        } else {
          final IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
          if(handler == null) return -1;
          final int size = handler.getSlots();
          if(size == 0) return 0;
          int n = 0;
          for(int i=0; i<size; ++i) n += handler.getStackInSlot(i).isEmpty() ? 0 : 1;
          return (int)Math.round(((double)n*15)/(double)size);
        }
      },
      // 2: Free inventory slots
      (world, pos, state, side) -> {
        final TileEntity te = world.getTileEntity(pos);
        if(te==null) {
          return -1;
        } else if(te instanceof IInventory) {
          IInventory inventory = (IInventory)te;
          final int size = inventory.getSizeInventory();
          int n = 0;
          for(int i = 0; i < size; ++i) n += inventory.getStackInSlot(i).isEmpty() ? 1 : 0;
          return (int)Math.round(((double)n*15)/(double)size);
        } else {
          final IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
          if(handler == null) return -1;
          final int size = handler.getSlots();
          if(size == 0) return 0;
          int n = 0;
          for(int i=0; i<size; ++i) n += handler.getStackInSlot(i).isEmpty() ? 1 : 0;
          return (int)Math.round(((double)n*15)/(double)size);
        }
      },
      // 3: Redstone signal
      (world, pos, state, side) -> {
        return world.getRedstonePower(pos, side);
      }
    };

    private int acquisition_mode()
    { return debounce(); }

    private void acquisition_mode(int mode)
    { debounce(MathHelper.clamp(mode, 0, acquisitions.length-1)); }

    public void block_updated()
    { if(update_timer_ > 1) update_timer_ = 1; }

    @Override
    public void reset(IWorldReader world)
    {
      super.reset(world);
      on_power(15);
      threshold0_on(1);
      threshold0_off(0);
      debounce(0);
    }

    @Override
    public boolean activation_config(BlockState state, @Nullable PlayerEntity player, double x, double y)
    {
      if(state == null) return false;
      final SwitchBlock block = (SwitchBlock)state.getBlock();
      final int direction = (y >= 9) ? (1) : ((y <= 6) ? (-1) : (0));
      final int field = ((x>=2) && (x<=3.95)) ? (1) : (
        ((x>=4.25) && (x<=7)) ? (2) : (
          ((x>=8) && (x<=10)) ? (3) : (
            ((x>=11) && (x<=13)) ? (4) : (0)
          )));
      if((direction==0) || (field==0)) return false;
      switch(field) {
        case 1: {
          double v = threshold0_on()+(direction);
          if(v < 1) v = 1; else if(v > 15) v = 15;
          threshold0_on(v);
          if(threshold0_on() < threshold0_off()) threshold0_off(threshold0_on());
          break;
        }
        case 2: {
          double v = threshold0_off()+(direction);
          if(v < 0) v = 0; else if(v > 14) v = 14;
          threshold0_off(v);
          if(threshold0_off() > threshold0_on()) threshold0_on(threshold0_off());
          break;
        }
        case 3: {
          acquisition_mode(acquisition_mode() + direction);
          break;
        }
        case 4: {
          on_power(on_power() + direction);
          break;
        }
      }
      if(on_power() < 1) on_power(1);
      {
        StringTextComponent separator = (new StringTextComponent(" | ")); separator.mergeStyle(TextFormatting.GRAY);
        ArrayList<Object> tr = new ArrayList<>();
        tr.add(ModAuxiliaries.localizable("switchconfig.comparator_switch.threshold_on", TextFormatting.BLUE, new Object[]{(int)threshold0_on()}));
        tr.add(separator.deepCopy().append(ModAuxiliaries.localizable("switchconfig.comparator_switch.threshold_off", TextFormatting.YELLOW, new Object[]{(int)threshold0_off()})));
        tr.add(separator.deepCopy().append(ModAuxiliaries.localizable("switchconfig.comparator_switch.output_power", TextFormatting.RED, new Object[]{(int)on_power()})));
        tr.add(separator.deepCopy().append(ModAuxiliaries.localizable("switchconfig.comparator_switch.mode"+((int)acquisition_mode()), TextFormatting.DARK_GREEN, new Object[]{})));
        tr.add(separator.deepCopy().append(ModAuxiliaries.localizable("switchconfig.comparator_switch.output_power", TextFormatting.RED, new Object[]{(int)on_power()})));
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.comparator_switch", TextFormatting.RESET, tr.toArray()));
      }
      markDirty();
      return true;
    }

    @Override
    public int power(BlockState state, boolean strong)
    { return (acquisition_mode()==3) ? (0) : (super.power(state, strong)); }

    @Override
    public void tick()
    {
      if(ModConfig.without_environmental_switch_update) return;
      if((!hasWorld()) || (getWorld().isRemote) || (--update_timer_ > 0)) return;
      if(update_interval_ < 4) update_interval_ = 4;
      update_timer_ = update_interval_ + (int)(Math.random()*2); // sensor timing noise using rnd
      BlockState state = getBlockState();
      if((!(state.getBlock() instanceof ComparatorSwitchBlock))) return;
      boolean active = state.get(POWERED);
      final Direction facing = state.get(FACING);
      final BlockPos adjacent_pos = getPos().offset(facing.getOpposite());
      final BlockState adjacent_state = getWorld().getBlockState(adjacent_pos);
      acquisition_mode(acquisition_mode()); // just to ensure that no nbt loading or so may cause exceeding the container limits.
      final int value = MathHelper.clamp(acquisitions[acquisition_mode()].sample(world, adjacent_pos, adjacent_state, facing), -1, 15);
      if(value < 0) {
        active = false;
        update_timer_ = 20;
      } else {
        // switch value evaluation
        int measurement = 0;
        if(threshold0_off() >= threshold0_on()) {
          // Use exact light value match
          measurement += (value==threshold0_on()) ? 1 : -1;
        } else {
          // Standard balanced threshold switching.
          if(value >= threshold0_on()) measurement = 1;
          if(value <= threshold0_off()) measurement = -1; // priority off
        }
        debounce_counter_ = debounce_counter_ + measurement;
        if(debounce_counter_ <= 0) {
          active = false; debounce_counter_ = 0;
        } else if(debounce_counter_ >= 2) {
          active = true; debounce_counter_ = 2;
        }
      }
      // state setting
      updateSwitchState(state, (ComparatorSwitchBlock)(state.getBlock()), active, 0);
    }
  }
}
