
## Gauges and Switches (MC1.15.1)

Mod sources for Minecraft version 1.15.2.

- Description, credits, and features: Please see the readme in the repository root.

- Compiled mod distribution channel is curseforge: https://www.curseforge.com/minecraft/mc-mods/redstone-gauges-and-switches/files.

----
## Version history

    - v1.2.5-b2  [F] Added placement checks to circumvent crashes (PR#33 by IceDragon200).

    - v1.2.5-b1  [M] Modified global switch power placement default setting to strong.
                 [F] Fixed gauge/indicator placement (issue #27, thx Bobcat64)

                 -------------------------------------------------------------------
    - v1.2.4     [R] Release build v1.2.4. Release-to-release changes:
                     * Gauges are comparator output sensitive.
                     * Comparator Switch mode "Redstone Signal Strength" added.
                     * Config handling update.
                     * Bug fixes.
                 -------------------------------------------------------------------

    - v1.2.4-b2  [!] Forge update, requires Forge 1.15.2-31.2.20.
                 [F] Gauges explicitly tell adjacent blocks that they can connect Redstone.
                 [F] Fixed Indicator/Siren placement crash (issue #24, thx Jamzs3).

    - v1.2.4-b1  [A] Gauges are also comparator output sensitive and can directly
                     show e.g. how full chests are.
                 [F] Industrial Machine Switch texture size fixed.
                 [U] Forge/mappings updated.
                 [M] JEI integration updated.
                 [M] Models updated to circumvent too dark GUI representations.
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

    - v1.2.2-b2   [A] Added Elevator Button.

    - v1.2.2-b1   [A] Added Valve Switch (idea and model by Jack M).

                  -------------------------------------------------------------------
    - v1.2.1      [R] Release build v1.2.1. Release-to-release changes:
                      * Industrial Comparator Switch added.
                  -------------------------------------------------------------------

    - v1.2.1-b1   [A] Added Industrial Comparator Switch.

    - v1.2.0-b2   [A] Added GIT version logging.
                  [M] Lang en_us: Sensitive Glass block names adapted (issue #15, thx Dimentive).

    - v1.2.0-b1   [A] Added JEI integration.
                  [M] Model "vanillafication" rework, part 2.
                  [D] Removed obsolete switches (registry not affected).
                  [M] Recipes simplified, recipe conditions unified.
                  [M] Code updates for version compatibility.
                  [A] All block are waterloggable (including Sensitive glass).

    - v1.1.10-b4  [F] Removed tag reference to minecraft:dirts, which prevented any
                      recipes from being craftable.

    - v1.1.10-b3  [A] Initial port.

----
