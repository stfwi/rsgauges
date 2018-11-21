/**
 * @file ModResources.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Common extended functionality dealing with resource
 * files and corresponding settings/usage options.
**/
package wile.rsgauges;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.LinkedList;

public class ModResources
{
  @ObjectHolder("rsgauges:alarm_siren_sound")
  public static final SoundEvent alarm_siren_sound = SoundRegistry.createSoundEvent("alarm_siren_sound");

  /**
   * Registry event handling for the sounds listed above.
   */
  @Mod.EventBusSubscriber(modid=ModRsGauges.MODID)
  public static final class SoundRegistry
  {
    private static LinkedList<SoundEvent> created_sounds_ = new LinkedList<SoundEvent>();

    public static SoundEvent createSoundEvent(String name)
    {
      final ResourceLocation rl = new ResourceLocation(ModRsGauges.MODID, name);
      SoundEvent se = new SoundEvent(rl).setRegistryName(rl);
      created_sounds_.push(se);
      return se;
    }

    @SubscribeEvent
    public static void onRegistryEvent(RegistryEvent.Register<SoundEvent> event)
    {
      for(SoundEvent se:created_sounds_) {event.getRegistry().register(se);}
      created_sounds_.clear();
    }
  }

  /**
   * Block sound player class used in the code, additionally specifying
   * playback parameters for the sound.
   */
  public static final class BlockSoundEvent
  {
    final SoundEvent se_;
    final float volume_, pitch_;
    public BlockSoundEvent(SoundEvent se, float volume, float pitch) { se_=se; volume_=volume; pitch_=pitch; }
    public BlockSoundEvent(SoundEvent se, float volume) { this(se, volume, 1f); }
    public BlockSoundEvent(SoundEvent se) { this(se, 1f, 1f); }
    public SoundEvent sound() { return se_; }
    public float volume() { return volume_; }
    public float pitch() { return pitch_; }
    public void play(World world, BlockPos pos) { world.playSound((EntityPlayer)null, pos, se_, SoundCategory.BLOCKS, volume_, pitch_); }
  }
}
