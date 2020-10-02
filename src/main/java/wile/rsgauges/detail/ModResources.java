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

import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.libmc.detail.SidedProxy;

import java.util.LinkedList;

public class ModResources
{
  private static LinkedList<SoundEvent> created_sounds_ = new LinkedList<>();

  private static SoundEvent createSoundEvent(String name)
  {
    final ResourceLocation rl = new ResourceLocation(ModRsGauges.MODID, name);
    SoundEvent se = new SoundEvent(rl).setRegistryName(rl);
    created_sounds_.push(se);
    return se;
  }

  public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event)
  {
    for(SoundEvent se:created_sounds_) {event.getRegistry().register(se);}
    // created_sounds_.clear();
  }

  /**
   * Sounds
   */
  public static final SoundEvent ALARM_SIREN_SOUND = createSoundEvent("alarm_siren_sound");

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

    public void play(World world, BlockPos pos) {
      if(SidedProxy.isClientSide()) {
        world.playSound(SidedProxy.getPlayerClientSide(), pos, se_, SoundCategory.BLOCKS, volume_, pitch_);
      } else {
        world.playSound(null, pos, se_, SoundCategory.BLOCKS, volume_, pitch_);
      }
    }
  }

  public static final class BlockSoundEvents
  {
    // Switch default sounds
    public static final BlockSoundEvent DEFAULT_SWITCH_MUTE          = new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0f, 1f);
    public static final BlockSoundEvent DEFAULT_SWITCH_ACTIVATION    = new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.3f, 0.92f);
    public static final BlockSoundEvent DEFAULT_SWITCH_DEACTIVATION  = new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.3f, 0.82f);
    public static final BlockSoundEvent DEFAULT_SWITCH_CONFIGCLICK   = new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f);
    // Switch link sounds
    public static final BlockSoundEvent SWITCHLINK_CANNOT_LINK_THAT      = new ModResources.BlockSoundEvent(SoundEvents.ENTITY_ENDERMAN_SCREAM, 0.2f, 2.5f);
    public static final BlockSoundEvent SWITCHLINK_LINK_TARGET_SELECTED  = new ModResources.BlockSoundEvent(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.2f, 2.0f);
    public static final BlockSoundEvent SWITCHLINK_LINK_SOURCE_SELECTED  = new ModResources.BlockSoundEvent(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.2f, 2.0f);
    public static final BlockSoundEvent SWITCHLINK_LINK_SOURCE_FAILED    = SWITCHLINK_CANNOT_LINK_THAT;
    public static final BlockSoundEvent SWITCHLINK_LINK_PEAL_USE_SUCCESS = new ModResources.BlockSoundEvent(SoundEvents.ENTITY_ENDERMAN_AMBIENT, 0.1f, 4f);
    public static final BlockSoundEvent SWITCHLINK_LINK_PEAL_USE_FAILED  = new ModResources.BlockSoundEvent(SoundEvents.ENTITY_ENDERMAN_HURT, 0.1f, 2.0f);
  }
}
