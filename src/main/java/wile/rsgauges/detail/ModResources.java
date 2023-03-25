/*
 * @file ModResources.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Common extended functionality dealing with resource
 * files and corresponding settings/usage options.
 */
package wile.rsgauges.detail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.libmc.detail.Registries;
import wile.rsgauges.libmc.detail.SidedProxy;

public class ModResources
{
  public static RegistryObject<SoundEvent> createSoundEvent(String name)
  {
    return Registries.sound_deferred_register.register(name, () -> new SoundEvent(new ResourceLocation(ModRsGauges.MODID, name)));
  }

  /**
   * Sounds
   */
  public static RegistryObject<SoundEvent> ALARM_SIREN_SOUND;

  /**
   * Block sound player class used in the code, additionally specifying
   * playback parameters for the sound.
   */
  @SuppressWarnings("unused")
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

    public void play(Level world, BlockPos pos) {
      if(!(world instanceof ServerLevel)) {
        world.playSound(SidedProxy.getPlayerClientSide(), pos, se_, SoundSource.BLOCKS, volume_, pitch_);
      } else {
        float volume = Math.min(volume_, 1f);
        world.playSound(null, pos, se_, SoundSource.BLOCKS, volume, pitch_);
        if(volume_ > 1.1f) {
          for(Direction dir: Direction.values()) {
            world.playSound(null, pos.relative(dir, 15), se_, SoundSource.BLOCKS, volume, pitch_);
          }
        }
      }
    }
  }

  public static final class BlockSoundEvents
  {
    // Switch default sounds
    public static final BlockSoundEvent DEFAULT_SWITCH_MUTE          = new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0f, 1f);
    public static final BlockSoundEvent DEFAULT_SWITCH_ACTIVATION    = new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.3f, 0.92f);
    public static final BlockSoundEvent DEFAULT_SWITCH_DEACTIVATION  = new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.3f, 0.82f);
    public static final BlockSoundEvent DEFAULT_SWITCH_CONFIGCLICK   = new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f);
    // Switch link sounds
    public static final BlockSoundEvent SWITCHLINK_CANNOT_LINK_THAT      = new ModResources.BlockSoundEvent(SoundEvents.ENDERMAN_SCREAM, 0.2f, 2.5f);
    public static final BlockSoundEvent SWITCHLINK_LINK_TARGET_SELECTED  = new ModResources.BlockSoundEvent(SoundEvents.ENDERMAN_TELEPORT, 0.2f, 2.0f);
    public static final BlockSoundEvent SWITCHLINK_LINK_SOURCE_SELECTED  = new ModResources.BlockSoundEvent(SoundEvents.ENDERMAN_TELEPORT, 0.2f, 2.0f);
    public static final BlockSoundEvent SWITCHLINK_LINK_SOURCE_FAILED    = SWITCHLINK_CANNOT_LINK_THAT;
    public static final BlockSoundEvent SWITCHLINK_LINK_PEAL_USE_SUCCESS = new ModResources.BlockSoundEvent(SoundEvents.ENDERMAN_AMBIENT, 0.1f, 4f);
    public static final BlockSoundEvent SWITCHLINK_LINK_PEAL_USE_FAILED  = new ModResources.BlockSoundEvent(SoundEvents.ENDERMAN_HURT, 0.1f, 2.0f);
  }
}
