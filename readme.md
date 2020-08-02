
## Gauges and Switches (MC1.14.4)

Mod sources for Minecraft version 1.14.4.

- Description, credits, and features: Please see the readme in the repository root.

- Compiled mod distribution channel is curseforge: https://www.curseforge.com/minecraft/mc-mods/redstone-gauges-and-switches/files.

----
## Version history

    ~ v1.2.5-b1  [M] Modified global switch power placement default setting to strong.

                 -------------------------------------------------------------------
    - v1.2.4     [R] Release build v1.2.4. Release-to-release changes:
                     * Gauges are comparator output sensitive.
                     * Comparator Switch mode "Redstone Signal Strength" added.
                     * Config handling update.
                     * Bug fixes.
                 -------------------------------------------------------------------

    - v1.2.4-b2  [F] Gauges explicitly tell adjacent blocks that they can connect Redstone.
                 [F] Fixed Indicator/Siren placement crash (issue #24, thx Jamzs3).

    - v1.2.4-b1  [A] Gauges are also comparator output sensitive and can directly
                     show e.g. how full chests are.
                 [F] Industrial Machine Switch texture size fixed.
                 [M] Config handling updated.
                 [!] Mod config moved from 'common' to 'server' config, in case
                     of applied setting please double check.

                 -------------------------------------------------------------------
    - v1.2.3     [R] Release build v1.2.3. Release-to-release changes:
                     * Door Sensor added.
                     * Recipe fixes, lang files updated.
                 -------------------------------------------------------------------

    - v1.2.3-b3  [M] Block Category Detector: Fixed forge tag lookup (issue #21, thx Nodlehs).
                 [M] Comparator Switch recipe fixed (thx @itzbahb).
                 [M] En lang files typo fixes (thx @itzbahb).

    - v1.2.3-b2  [F] Fixed switch link max distance config (issue #20, thx Rictu5).

    - v1.2.3-b1  [A] Added Door Sensor switch.
                 [M] Lang file zh_cn updated (PR#19, scikirbypoke).

                 -------------------------------------------------------------------
    - v1.2.2     [R] Release build v1.2.2. Release-to-release changes:
                     * Valve Switch added.
                     * Elevator Button added.
                     * Minor fixes.
                 -------------------------------------------------------------------

    - v1.2.2-b2  [A] Elevator Switch added.

    - v1.2.2-b1  [A] Added Valve Switch (idea and model by Jack Mazz).

                 -------------------------------------------------------------------
    - v1.2.1     [R] Release build v1.2.1. Release-to-release changes:
                     * Industrial Comparator Switch added.
                 -------------------------------------------------------------------

    - v1.2.1-b1  [A] Added Industrial Comparator Switch.

                 -------------------------------------------------------------------
    - v1.2.0     [R] Release build v1.2.0.
                 -------------------------------------------------------------------

    - v1.2.0-b2  [F] Fixed JEI integration warning if nothing is opt'ed out (thx @SDUBZ).
                 [A] Added GIT version logging.
                 [M] Lang en_us: Sensitive Glass block names adapted (issue #15, thx Dimentive).

    - v1.2.0-b1  [A] Added JEI integration.
                 [M] All Forge blockstates transformed to vanilla format.
                 [M] Model "vanillafication" rework, part 2.
                 [D] Removed obsolete switches (registry not affected).
                 [M] Recipes simplified, recipe conditions unified.
                 [M] Code updates for version compatibility.
                 [A] All block are waterloggable (including Sensitive glass).

    - v1.1.10-b3  [M] Strong power output of levers and buttons also output weak power
                      to all sides (like vanilla levers).
                  [M] Touch config for automatic switches (small buttons on the devices)
                      are not restricted anymore to using the empty hand.

    - v1.1.10-b2  [M] Global parent definition in model files updated.
                  [M] Language file updates.

    - v1.1.10-b1  [F] Indicator-powered check modified to support accepting direct strong
                      power from blocks which normally cannot provide power.
                  [M] Made metallic switch textures slightly lighter.

    - v1.1.9-b2   [F] Fixed switch link relay input bug (issue #14, thx wieselkatz!).
                  [F] Fixed optional-recipe condition for know switches.

    - v1.1.9-b1   [U] Updated to Forge 1.14.4-28.1.69/20190719-1.14.3.

    - v1.1.8-b4   [U] Updated to Forge 1.14.4-28.1.44/20190719-1.14.3.

    - v1.1.8-b3   [U] Updated to Forge 1.14.4-28.1.10/20190719-1.14.3.
                  [A] Knock switches (seismic adjacent block click detectors) added.

    - v1.1.8-b2   [U] Updated to Forge 1.14.4-28.0.81/20190719-1.14.3, recipe
                      condition processing adapted accordingly.

    - v1.1.8-b1   [F] Fixed contact switch sound issue.
                  [A] Added right-click switch configuration with Redstone Dust,
                      Ender Pearl or Switch Link Pearl.
                  [M] Industrial door contact mat model changed.
                  [M] Industrial sensitive contact mat model changed.
                  [M] Industrial shock sensitive contact mat model changed.
                  [M] Industrial cased link receiver models changed.
                  [M] Industrial block detector model changed.
                  [M] Industrial trapdoor switch models changed.
                  [M] Industrial dimmer model changed.

    - v1.1.7-b2   [A] Entity detectors have configurable pulse time.
                  [M] Industrial bistable switch model changes ported
                      from 1.12.

    - v1.1.6-b1   [A] Color tint support of gauges, glass switches,
                      and sensitive glass ported.
                  [M] Block detector switch patterns support tags.

    - v1.1.5-b1   [A] Initial 1.12.2 to 1.14.4 port of all
                      features except color tinting and
                      Forge ore dictionary dependent blocks.

----
