/*
 * @file DataFixing.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Game data corrections/adaptions to new mod versions.
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModRsGauges;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.common.util.ModFixs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.datafix.FixTypes;
import com.google.common.collect.Maps;
import java.util.Map;

public class DataFixing
{
  private static final int RSGAGUES_DATA_VERSION = 1;
  private static final Map<String,String> TE_ID_RENAMES = Maps.<String,String>newHashMap();

  static {
    TE_ID_RENAMES.put("minecraft:rsgauges_gauge_entity", "rsgauges:gauge_entity");
    TE_ID_RENAMES.put("minecraft:rsgauges_pulseswitch_entity", "rsgauges:switch_entity");
    TE_ID_RENAMES.put("minecraft:rsgauges_contactswitch_entity", "rsgauges:contactswitch_entity");
    TE_ID_RENAMES.put("minecraft:rsgauges_autoswitch_entity", "rsgauges:autoswitch_entity");
    TE_ID_RENAMES.put("minecraft:rsgauges_detectorswitch_entity", "rsgauges:detectorswitch_entity");
    TE_ID_RENAMES.put("minecraft:rsgauges_sensorswitch_entity", "rsgauges:sensorswitch_entity");
    TE_ID_RENAMES.put("minecraft:rsgauges_timerswitch_entity", "rsgauges:timerswitch_entity");
  }

  /**
   * Invoked in the Mod init() event.
   */
  public static void registerDataFixers()
  {
    final ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(ModRsGauges.MODID, RSGAGUES_DATA_VERSION);
    modFixs.registerFix(FixTypes.BLOCK_ENTITY, new TileEntityIdFixes());
  }

  /**
   * Fixed old undercored registry names to mod scoped registry names
   * (minscraft:rsgauges_an_entity --> rsgauges:an_entity).
   *
   * Unfortunately this appears not to work if the old TEs are not
   * registered. Also chunk fixing did not do, and using a data walker
   * neither. This is the method how TE names are fixed in vanilla,
   * so this is my choice.
   */
  public static class TileEntityIdFixes implements IFixableData
  {
    @Override
    public int getFixVersion()
    { return 0; } // no data set yet at all. Tested, triggers correctly when NBT key region[r].chunk[y,z].ForceVersionData.rsgauges not existing.

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
      final String new_id = TE_ID_RENAMES.get(compound.getString("id"));
      if(new_id != null) { compound.setString("id", new_id); }
      return compound;
    }
  }

}
