
## Gauges and Switches (MC1.12.2)

Mod sources for Minecraft version 1.12.2.

- Description, credits, and features: Please see the readme in the repository root.

- Compiled mod distribution channel is curseforge: https://www.curseforge.com/minecraft/mc-mods/redstone-gauges-and-switches/.

----
## Version history

    - v1.2.1-b1  [A] Added Industrial Comparator Switch.

                 -------------------------------------------------------------------
    - v1.2.0     [R] Release build v1.2.0.
                 -------------------------------------------------------------------

    - v1.2.0-b2  [M] Lang en_us: Sensitive Glass block names adapted (issue #15, thx Dimentive).

    - v1.2.0-b1  [!] Recipes, models and feature set changed. If you have a
                     modpack with tweaked recipes, then do not update.
                 [A] Added JEI integration.
                 [M] Model "vanillafication" rework, part 2.
                 [D] Removed obsolete switches (registry not affected).
                 [M] Recipes simplified, recipe conditions unified.
                 [M] Code updates for version compatibility.

                 -------------------------------------------------------------------
    - v1.1.10    [R] Release build v1.1.10. Release-to-release changes:
                     * Switch output behaviour is by default like vanilla buttons/levers.
                     * Switch touch config improved.
                     * Compat fixes.
                     * Lang updates.
                     * Model/texture improvements.
                 -------------------------------------------------------------------

    - v1.1.10-b4 [D] Removed obsolete JitModel bakery.
                 [M] Obsolete hard-coded ambient occlusion setting removed.

    - v1.1.10-b3 [M] Strong power output of levers and buttons also output weak power
                      to all sides (like vanilla levers).
                 [M] Touch config for automatic switches (small buttons on the devices)
                      are not restricted anymore to using the empty hand.

    - v1.1.10-b2 [M] Global parent definition in model files updated.
                 [M] Language file updates.

    - v1.1.10-b1 [F] Compat fix: Indicator-powered check modified to support accepting
                     direct strong power from blocks which normally cannot provide power.
                 [M] Made metallic switch textures slightly lighter.

                 -------------------------------------------------------------------
    - v1.1.9     [R] Release build v1.1.9. Release-to-release changes:
                     * Knock switches added.
                 -------------------------------------------------------------------

    - v1.1.9-b1  [A] Knock switches (detect breaking/clicking adjacent blocks) released.

                 -------------------------------------------------------------------
    - v1.1.8     [R] Release build v1.1.8. Release-to-release changes:
                     * Industrial series model overhaul.
                     * Language file updates.
                 -------------------------------------------------------------------
                 [M] Lang file zh_cn updated by scikirbypoke (PR#10).

    - v1.1.8-b2  [M] Industrial trapdoor (active) model position fixed.

    - v1.1.8-b1  [M] Industrial door contact mat model changed.
                 [M] Industrial sensitive contact mat model changed.
                 [M] Industrial shock sensitive contact mat model changed.
                 [M] Industrial cased link receiver models changed.
                 [M] Industrial block detector model changed.
                 [M] Industrial trapdoor switch models changed.
                 [M] Industrial dimmer model changed.

                 -------------------------------------------------------------------
    - v1.1.7     [R] Release build v1.1.7. Release-to-release changes:
                     * Entity detectors have configurable on-time.
                     * Pulse time config and switch linking with right click.
                     * Switch scheduling performance.
                     * Model improvments and refractoring.
                     * JEI integration.
                 -------------------------------------------------------------------

    - v1.1.7-b3  [M] Added right-click switch configuration with Redstone Dust,
                     Ender Pearl or Switch Link Pearl. (can be opt'ed out in the
                     config if needed). Tackles issue #9.

    - v1.1.7-b2  [A] Entity detectors have now a configurable on-time (redstone stack
                     config like pulse switches).
                 [M] Pulse switch scheduling adapted to compensate for world time
                     alterations (issue #9).

    - v1.1.7-b1  [M] Locations/names of all models adapted and model permutations
                     separated from Forge blockstate format for compliant 1.14 porting.
                 [A] JEI integration skel added for extended config options.
                 [M] Industrial bistable switch models/textures updated.

                 -------------------------------------------------------------------
    - v1.1.6     [R] Release build v1.1.6
                 -------------------------------------------------------------------
                 [M] Model display rotations/translations of all blocks for GUI,
                     1st/3rd person improved.

                 -------------------------------------------------------------------
    - v1.1.5     [R] Release build based on v1.1.5-b1
                 -------------------------------------------------------------------

    - v1.1.5-b1  [M] Lang files ch_cn updated by scikirbypoke.

                 -------------------------------------------------------------------
    - v1.1.4     [R] Release build based on v1.1.4-b1:
                     * Linking issue fixed.
                     * Rain sensor fixed.
                     * Optifine fixes.
                 -------------------------------------------------------------------

    - v1.1.4-b1  [F] Fixed linking issue when the game time is stopped (thanks
                     to mrossi80).
                 [F] Fixed rain sensor trigger condition.
                 [F] Fixed optifine gauge model issues (thanks mike linn).

                 -------------------------------------------------------------------
    - v1.1.3     [R] Release build.
                 -------------------------------------------------------------------
                 [F] Glass ambient occlusion fixed.

    - v1.1.3-b1  [F] Fixed cyclic switch destruction issue.
                 [A] Entity detector switches can now also trigger on objects/items.
                 [A] Tile entity name data fixer added (for version transitions).

                 -------------------------------------------------------------------
    - v1.1.2     [R] Release based on v1.1.2-b2.
                 -------------------------------------------------------------------

    - v1.1.2-b2  [M] Indicator blinking is client side only (using texture animations,
                     it was overdue to keep this away from the server).
                 [A] Added gauges color tinting support (left click with dye like switches).
                 [A] Added industrial, color tintable, white LED indicator (dye left click).
                 [A] Added rustic flag semaphore indicator.

    - v1.1.2-b1  [A] Added Cased switch link receiver (bistable, silent, full-block).
                 [A] Added Cased pulse switch link receiver (pulse, silent, full-block).
                 [A] Manual dimmer switch added (industrial vertical slide, 0 to 15).
                 [M] Switches and gauge do not get washed off by fluids.
                 [M] Faint config click sound added (applies to all switches).

                 -------------------------------------------------------------------
    - v1.1.1     [R] Release based on v1.1.1-b2.
                 -------------------------------------------------------------------

    - v1.1.1-b2  [M] Recipe of glass gauge changed to be crafted with a glass block.
                 [A] Block detection switch added (configurable range, category filter,
                     match threshold, debounce, output power). Filters are e.g. "solid",
                     "fluid", "air", "stone", "soil", "wooden", "plant", "crop",
                     "mature crop", and other ore dict based categories. Recipe similar
                     to the entity detection switch.
                 [M] German language file updated.

    - v1.1.1-b1  [F] Fixed block notification issue when destroying contact plates
                     while standing on them.
                 [M] Performance for redstone block updates of switches improved
                     (update of affected blocks only).
                 [A] Subsystem for full block switches with configurable output
                     sides implemented.
                 [A] Accepted wrenches (default: redstone torch) allow to configure
                     switch outputs with a single (activation) right click in addition
                     to empty handed left-double-click. (Feature added for creative
                     mode and configuration of many switches).

                 -------------------------------------------------------------------
    - v1.1.0     [R] Release based on v1.1.0-b4.
                 -------------------------------------------------------------------

    - v1.1.0-b4  [A] Added recipe system based opt-out (intended to facilitate
                     mod packing); added optionally omitting registration of
                     opt'ed out features (for trouble shooting and development).
                 [A] Added with-experimental opt-in config.
                 [A] Added mod specific registration and config based recipe condition.
                 [M] Indicator state range reduced (RAM/performance optimisation).
                 [M] Internal changes to prepare future mod features (see repository).

    - v1.1.0-b3  [A] Added manual/description tooltips for all blocks and items
                     of the mod (CTRL-SHIFT when mouse hovering in the inventory
                     or JEI).

    - v1.1.0-b2  [A] Added switch link relay (redstone power based link activation).
                 [A] Added switch link pulse relay (reacts on redstone rising edge).
                 [A] Added switch link receiver (silent bistable switch device).
                 [A] Added switch link pulse receiver (silent pulse switch device).
                 [A] Added yellow and red power plants (yes, flowers).

    - v1.1.0-b1  [U] Forge dependency update from v14.23.3.2655 to v14.23.5.2768,
                     gradle updated to v3.3, mappings update to version stable_39.
                 [!] Forge deprecation fixes, THIS MAY AFFECT EXISTING SWITCHES.
                 [!] Structured config file implemented. THIS WILL RESET YOUR
                     MOD CONFIG TO THE DEFAULTS.
                 [A] Version update check (Forge update json file format).
                 [A] Mod jar signing implemented.
                 [M] Version naming compliancy (removed 'mc' prefix in version strings).
                 [M] Forge naming convention refractoring of classes.

                 -------------------------------------------------------------------
    - v1.0.5     [R] Release based on v1.0.5b2.
                 -------------------------------------------------------------------

    - v1.0.5-b2  [F] Debounced link pearl cycling when creating a link pearl
                     from an Ender pearl.

                 [A] Added inverse output state switch linking feature.

    - v1.0.5-b1  [A] Added switch linking subsystem.
                 [A] Added switch link pearl, used to link and remote activate
                     switches. Created by left-clicking a switch with an Ender
                     pearl.
                 [A] Added switch linking of pulse and bi-stable switches. Both
                     can be link sources and link targets.
                 [A] Added switch linking of contact switches. All can be link
                     sources, the shock-sensitive trapdoor switch can be source
                     and target (only the most insensitive one).
                 [A] Added switch linking of automatic switches. All can be link
                     sources, except the interval timer, which can be a link
                     target.
                 [A] Added switch link trigger configurations: default trigger,
                     source activation trigger, source deactivation trigger,
                     and source toggle trigger.
                 [M] Interval timer start condition consistency fixed (from
                     standby to run mode the switch resets internal values).
                 [F] Interval timer output inversion corrected to be not affected
                     by standby/run state.

                 -------------------------------------------------------------------
    - v1.0.4     [R] Release based on v1.0.4b2.
                 -------------------------------------------------------------------

    - v1.0.4-b2  [A] Added industrial shock sensitive contact mat (pulses when
                     something falls upon).
                 [A] Added rustic shock sensitive contact plate (pulses when
                     fallen upon).
                 [A] Added industrial and rustic trapdoor switch (opens and pulses
                     when fallen upon).
                 [A] Added industrial and rustic high sensitive trapdoor switch
                     (opens and pulses when fallen upon or walked on, except
                     when sneaking).
                 [A] Added industrial and rustic counter trapdoor switch (pulses
                     when items or any entities fall through).

    - v1.0.4-b1  [A] Added switch status message overlay (replaces chat messages).
                 [A] Added industrial shock sensor contact mat (fall-on detector).
                 [A] Added rustic full size contact plate.
                 [M] Double click switch config shows the current config
                     at the first double click, after that the config is cycled.
                     Added this because the standard use case is to first inspect
                     the config and then optionally change it.
                 [M] Block-pop-off-check for gauges and switches check for
                     AIR and any liquid now, not just for air, water, lava.
                 [M] Light values of indicators reduced (LEDs to 2), only
                     dynamic on client, constant on server. Reduces server re-
                     calculations, but has the drawback that light-level-overlay
                     shows not the same values on the client as used on the
                     server.
                 [M] Improved local light sensor autoswitch: Initial threshold
                     values 7(on) and 6(off), fitting to spawn risk values. If on-
                     and off-threshold are the same, the switch now activates on
                     exactly that light level.
                 [F] Redstone dust tracks don't show connections to wall-mount
                     switches.
                 [M] Switches are attachable to pistons, and pop off when pushed.
                 [A] Implemented localisation support (some open tasks left, like
                     localized day time representation of the timer clock; and
                     seconds of the interval timers are arabic numbers because
                     localisation files do not support %.02f format etc.). German
                     language file updated.
                 [M] Changed sensitive glass dynamic light level mod config to
                     constant light level config for the server, means it is opt-in
                     now, not opt-out. Reason is to priorise correct light-level-
                     overlay over performance tuning.

                 -------------------------------------------------------------------
    - v1.0.3     [R] Release based on v1.0.3b6.
                 -------------------------------------------------------------------

    - v1.0.3-b6  [A] Added inverted redstone sensitive glass (light emitting).
                 [A] Added touch configurable glass contact plate.
                 [A] Added glass infrared motion detector.
                 [A] Added glass linear laser detector.
                 [A] Added glass daytime switch.
                 [A] Added glass interval timer switch.
                 [M] Enabled glass switch color teaching (dye-left-click-tinting).
                 [M] Updated language files (Chinese prepared, still contains
                     English words).

    - v1.0.3-b5  [A] Added Chinese language file (thanks to sfchipan for the pull
                     request on github)
                 [A] Added German language file, too.
                 [A] Fixed server startup crash issue due to color handler event
                     subscription, which is unfortunately client side only. Thanks
                     to Dave for posting this on github (issue #6).

    - v1.0.3-b4  [A] Added glass bi-stable, pulse, and contact switches.
                 [M] Changed sensitive glass textures.
                 [M] Improved configuration based block/item block initialisation
                     (backend).
                 [A] Implemented unified switch color tinting support (backend)
                 [A] Implemented unified switch light emission support (backend)

    - v1.0.3-b3  [A] Added stained redstone sensitive glass blocks, changing from
                     clear to stained-colored, but do not emit light as the clear
                     sensitive glass block.

    - v1.0.3-b2  [A] Added mod specific creative tab.
                 [A] Added rustic floor contact switch UI texture.
                 [A] Added switch left-click option 'no output' (default disabled
                     in mod options, feature for future use).
                 [M] Improved industrial bi-stable switch 1 model.
                 [M] Changed multi-left-click config (weak/inverted): Now double-click
                     for each step.

    - v1.0.3-b1  [A] Added rustic wall gauge.
                 [A] Added exact pulse switch off-timing using redstone-dust
                     left-clicking.
                 [M] Improved industrial gauge models.

                 -------------------------------------------------------------------
    - v1.0.2     [R] Release based on v1.0.2b2.
                 -------------------------------------------------------------------

    - v1.0.2-b2  [A] Added "rustic" switch series switches.

    - v1.0.2-b1  [A] Added "old fancy" switch series.
                 [A] Added "rustic" switch series (partial).
                 [F] Fixed automatic switch iterative recipe.
                 [F] Fixed version update bug (switchtileentity.reset() recursion),
                     thanks to @tenosko.
                 [M] Changed sensitive glass recipe.

    - v1.0.1     [R] Release based on v1.0.1b3.

    - v1.0.1-b3  [A] Added local light sensor switch.
                 [A] Added rain sensor switch.
                 [A] Added lightning potential sensor switch.
                 [A] Added (day) timer clock.
                 [A] Added interval timer.
                 [M] Changed multi-left-click config default timeout from 1200ms
                     to 700ms.
                 [A] Prepared translation/localisation for text messages.

    - v1.0.1-b2  [A] Added bistable mechanical/retro style hopper blocker switch.
                 [A] Added bistable mechanical/retro style rotary lever switch.
                 [A] Added bistable mechanical/retro style lever switch.
                 [A] Added pulse mechanical/retro style pull switch.
                 [A] Added pulse mechanical/retro style push switch.
                 [A] Added pulse mechanical/retro style double pole push switch.
                 [A] Added power state dependent construction time bounding box
                     definitions for switches.
                 [M] Changed recipe order for light flip switch (to end of style
                     selection list).
                 [M] Changed ring-fenced-pulse-switch front texture.
                 [F] Fixed localisation name of arrow target pulse switch.
                 [F] Fixed texture bleeding, UV scaling, and bounding box of the
                     alarm siren.

    - v1.0.1-b1  [A] Added configurable infrared motion sensor (volume range
                     automatic switch).
                 [A] Added configurable sensor (linear range automatic switch).
                 [A] Added configurable door contact mat.
                 [A] Added sensitive floor contact mat.
                 [A] Added fenced pulse switch.
                 [A] Added arrow target pulse switch.
                 [A] Added alarm siren (sound indicator).
                 [A] Added redstone sensitive glass (color change black->white,
                     light source).
                 [A] Added feature to switches: Empty handed multi-leftclick cycles
                     through (strong -> weak -> weak inverted -> strong inverted ->
                     strong etc).
                 [A] Added: Door contact mat, infrared and laser sensor have a
                     keypad to configure range/sensitivity, entity type (all, players,
                     mobs, villagers, animals, objects, etc), entity count threshold,
                     and output redstone power.
                 [A] Added: Machine pulse switch can be activated with projectiles
                     (but not the fenced one).
                 [A] Added: ESTOP switch can be switched off with projectiles, but
                     not on.
                 [A] Added: Sensitive contact mat reacts to hitting projectiles and
                     objects dropped on.

                 -------------------------------------------------------------------
    - v1.0.0     [R] Release based on v1.0.0rc4.
                 -------------------------------------------------------------------
                 [M] Final recipe tuning (gauge and lamp indicator).

    - v1.0.0-rc4 [M] Performance improvements, startup screen config options added.
                 [F] Fix: Network client: Gauge display zero when hit or activated.

    - v1.0.0-rc3 [F] Fix: Tile entity based gauge handling to prevent display
                     glitches on network clients.

    - v1.0.0-rc2 [F] Fix: Server based gauge processing to measure indirect weak
                     power.

    - v1.0.0-rc1 [A] Initial feature complete version.

----
