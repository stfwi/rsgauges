
## Redstone Gauges and Switches

A [Minecraft](https://minecraft.net) (Java Edition) mod based on [`Forge`](http://www.minecraftforge.net/). The Mod adds gauges, stylish buttons,
levers, pressure plates, timers, and other devices to measure or "produce" redstone
power to the game:

### Distribution file download

Main distribution channel for this mod is CurseForge:

  - Release/beta versions: https://www.curseforge.com/minecraft/mc-mods/redstone-gauges-and-switches/files
  - All versions: https://minecraft.curseforge.com/projects/redstone-gauges-and-switches/files

----
## Version history

Mod versions are tracked in the readme files for individual Minecraft versions, and
of course in the commits of this repository. Beta and release versions that are
distributed as compiled `jar`s are tagged accordingly. Release versions are merged
into the `master` branch, while beta/alpha version tags link to `develop` branch
commits.

  - [1.12](1.12/readme.md)

  - [1.14](1.14/readme.md)

----

### Screenshots and recipes

  - [Screenshots in the documentation section here](documentation/readme.md).

  - [Recipes are described in this file](documentation/recipes.md) file.

----

### Content

- **Gauges** are small devices, which can be attached to any solid block in the game,
  and they measure the redstone power that this blocks receives from blocks around
  it. Their displays are analog or digital and quantised from 0 to 15. The
  measurement method differs a bit from how e.g. comparators or redstone lamps
  react, see the details below.

- **Indicators** are on-off displays attached to blocks, measuring like gauges (and
  are basically gauges). They are "off" if the power is zero, and "on" if the power
  is greater than zero. It depends on the indicator how the display looks like,
  some are simple LED like lights, some are blinking, etc.

- **Bi-stable switches** are basically vanilla Minecraft levers with a different
  style. They produce redstone power in the block they are attached to and are
  manually switched on and off. You can configure strong/weak power and inverted/not
  inverted by multi-left-clicking with your empty hand.

- **Pulse switches** are like vanilla Minecraft buttons with a different style. They
  also produce strong redstone power in the block they are attached to, and they
  switch off automatically after a short time. However, pulse switches can be pushed
  multiple times, extending the delay (each right-click) before switching off again.
  Single left clicking switches off directly, double-left-clicking with your empty
  hand configures weak/strong/inverted.

- **Contact mats** are floor mounted switches like pressure plates. They can be pulse
  emitting, bi-stable, etc. It depends on the mat what it supports (double-left-click
  config always, UI panel config can be entity detection and count, power strength,
  etc).

- **Automatic switches** are devices that can change their output without someone
  clicking or stepping on them. E.g. time switches, weather sensors, or motion
  detectors are in this category. Additional to their specific UI panels they also
  support double-left-click config.

- **Switch link pearls** are Ender pearl based connections between switches, allowing
  to remote activate a target switch by using the pearl in the main hand, or by
  placing them into another switch. For details see the "details" section below.

- **Redstone sensitive glass** is a more ore less transparent glass-metal-alloy
  that changes its state if it is powered. It may change color, transparency, or
  emit light - depending on the exact glass composition. You can also place redstone
  tracks on it, but because of boundary effects of the crystalline material
  structures you will not see the tracks from below.

- **Power plants** are no energy generating facilities, but artificial flowers.
  They look very similar to vanilla Minecraft flowers and produce a redstone signal
  if they are touched or something falls on them. Because artificial, they behave
  like contact plates and support all switch settings including switch linking.

----
### Mod pack integration, forking, back ports, bug reports

  - Packs: If your mod pack ***is open source as well and has no installer***,
    you don't need to ask and simply integrate this mod. The mod-config and
    naming convention of the blocks added by this mod is optimised in a way
    that pack tweaking mods can easily remove functionality, complete device
    types, or device style series from the mod pack (e.g. remove all series
    except the industrial or rustic, remove all automatic switches, etc).

  - Forking: It's MIT, you can fork the code under the same conditions.

  - Back ports: I've just started modding and like to stick with Minecraft
    versions `mc>=1.12.2`. Sorry, no back ports.

  - Bug reports: Yes, please let me know. Drop a mail or better open an issue
    for the repository.

  - Pull requests: Happily accepted. Please make sure that use the ***develop
    branch*** for pull requests. The master branch is for release versions only.
    I might merge the pull request locally if I'm ahead of the github repository,
    we will communicate this in the pull request thread then.

----

### Gauge and indicator details

Gauges measure differently as e.g. redstone lamps. They do not only react
to the (strong or weak) power they receive themselves, but also lookup the
power that the adjacent block they face receives. This prevents the gauges
from seeing indirect (weak) power from adjacent blocks, causing incorrect
display values. The behaviour is:

- If the gauge is attached to a block that can provide redstone power, such
  as a redstone block or a device of a mod that has a redstone output on the
  side, then the gauge will display the maximum of weak and strong power coming
  from the facing of that device. That is pretty much as most redstone inputs
  behave.

- If the gauge is attached to a block that cannot produce redstone power, but
  can be powered (most blocks except glass, air, fluids, etc), then the gauge
  looks what power this block receives from all sides. The maximum of weak and
  strong power is taken from each side, and then the total maximum of all sides
  is displayed.
  That means it behaves as if the gauge would be the block behind it. This
  feature allows you to place a gauge e.g. on a wall and feed the "signal"
  you want to measure to the back side of the wall. This indirect measurement
  of weak power has a little catch through - the display is a bit delayed.
  That is normally no problem, but if you need fast display reaction, take care
  that the gauge is directly powered.

Indicators measure like gauges, except that they show only if there is power
or not (on/off). If applicable to the design, indicators emit light. Sound
indicators emit sound. Some of the indicators are blinking or available in
steady and blinking (or otherwise alternating) variants.

### Switch details

Switching devices are partially styled variants of vanilla switches (buttons,
levers, pressure plates, trap wires, ...), partially blocks with functionality
that vanilla does not support. There are common features that switches of this
mod provide, and special features for switch types and individual switches:

  - **Output configuration**: Switches can be right-clicked with a redstone torch
    (or double-left-clicked empty handed) to select:
      - output strength (weak or strong power)
      - output polarity (inverted/non-inverted)
      - output inhibitation (no output)

  - **Pulse time configuration**: All switches that emit a pulse can be clicked
    with a stack of redstone dust to configure the pulse time. The pulse time is
    2 ticks per stack element, means `0.1s x stacksize` or `2ticks x stacksize`.

  - **Projectile sensitivity**: Some switches can be activated with arrows or other
    projectiles, depending on the switch design.

  - **Color tinting**: Some switches can be tinted by left-clicking with dye.

The switches will remember their configutations and tinting until being broken. This
allows to "reset" a switch by simply re-attaching it.

#### Switch types

  - **Manual bi-stable** switches are basically enhanced vanilla levers with the
    common features listed above. They only power the block that they are placed on,
    not blocks around them.

  - **Manual pulse switches** are enhanced vanilla buttons with the additional common
    mod switch features. Like bi-stable switches they power only the block that
    they are attached to. Additional to the redstone-dust pulse time config, the
    pulse time of manual pulse switches can be extended by activating them multiple
    times. The delays are in ticks: 25 (first click when off), 50, 100, 200, 400.
    Left click a pulse switch to immediately deactivate it.

  - **Contact switches** are activated by colliding. Pressure plates and trip wires
    are vanilla versions of this type. In this mod, contact switches can be more than
    floor mounted contact mats, but also wall or ceiling attached blocks. They
    normally deactivate after releasing and a defined (and configurable) pulse time.
    The trigger conditions vary and are configurable for some contact switch types:

    - **Touch configuration** is available on e.g. the upside of door contact
      mats (right click the buttons empty handed):

      - Output power  : 1 to 15
      - Entity filter : "everything", "creatures", "players", "mobs", "animals",
                        "villagers", "objects".
      - Entity count  : The number of entities that have to collide to activate
                        the switch (activation threshold).
      - Sensitivity   : Normal/high, on high sensitivity the switch also triggers
                        for entities that do not trigger vanilla pressure plates.

    - **Shock sensitivity**: There are contact mats/plates that trigger only on
      vibration or mechanical shock, e.g. when something falls upon them. High
      sensitive variants additionally trigger when walking (and not sneaking)
      on them.

    Like pressure plates, floor mounted contact switches provide power to the
    block underneath. Different from vanilla pressure plates, they do not power
    all four directions around it, but only in the directions which they are placed.
    Wall or ceiling mounted contact switches only provide power to the block
    they are attached to.

  - **Trapdoor switches** are a specialisation of contact switches. They open when
    triggered, allowing entities to fall through. Mobs walk over these trap doors
    when closed and circumvent them when opened. Trapdoor switches are mounted on
    the side of a floor block, and output power to that block when they trigger.

  - **Automatic switches**: Trigger contactless based on internal conditions or
    sensor readings, providing power only to the block they are placed on. Basic
    switch configuration (invert/weaken/no-output) also applies to auto-switches.

    - **Detector switches** are touch configurable entity detectors and available
      in two variants: Volumetric and linear. Volume sensors monitor a 180° angle
      and 2 blocks above/below, while linear detectors observe in a straight line.
      Touch config (right click the buttons empty handed) includes:

      - Output power  : 1 to 15
      - Entity filter : "creatures", "players", "mobs", "animals", "villagers",
                        "everything".
      - Entity count  : The number of entities that have to be seen to activate.
      - Sensor range  : 1 to 16. Changes the volume to observe. For linear detectors
                        this will increase the range in front of the detector, for
                        volume sensors it increases the range at left, front and
                        right (not up and down).

      These detectors do *not* see throuth walls or glass. They deactivate with a
      short delay (not configurable yet).

    - **Day timer clocks**: Switches on/off at defined day times, where the day
      starts at 06:00 at sunrise, midnight at 24:00. The touch configuration
      (right click the buttons empty handed) encompasses:
      - Output power  : 1 to 15
      - Randomiser    : 0 to 10. Randomly delays the switching time point. Higher
                        numbers will (probably) increase the delay. Default 0 = no
                        random = exact timing.
      - Off-time point: 00:00 to 23:30 in 30min steps
      - On-time point : 00:00 to 23:30 in 30min steps

    - **Interval timer**: Higher frequency on-off alternation. Configuration
      (right click the buttons empty handed):
      - Output power  : 1 to 15
      - Ramp          : 0 to 5, where 0=no ramp. If 1..5, the switch will ramp
                        up/down the output from 0 to the configured power with
                        the defined step size per 0.5s.
      - Off-time      : 0.1s to 600s (2 to 12k ticks). Step size depends on
                        value range (higher values -> higher step size).
      - On-time       : 0.1s to 600s (2 to 12k ticks).

      When placed, interval timers are in **STANDBY** mode to allow configuration
      before operation. By right clicking the on/off field (or "1/0") the switches
      are set to run mode. State indication (e.g. yellow and green LEDs) show if
      a timer is in standby or operation.

    - **Environmental sensor switches**: Trigger or switch depending on environmental
      sensor readings:

      - **Rain sensor**: Switches on when it rains ***on it***. Only the output power
        is touch configurable.

      - **Thunderstorm sensor**: Switches on when a critical potential for lightning
        strikes is measured. Touch config: output power only.

      - **Local light sensor**: Switches on or off depending on the local ("combined")
        light level at its position. Touch config:
        - Output power  : 1 to 15
        - Debouncing    : 0 to 5. Delays switching to ensure that no short light level
                          noise causes unwanted output changes.
        - Off threshold : 0 to 15, light level at witch/below the sensor switches off.
        - On threshold  : 1 to 15, light level at witch/above the sensor switches on.

        If off and on light level are identical, the sensor switches only at exactly
        this light level. Higher debouncing values mean higher switching delays.

    - **Block detection switches**: Switch on/off depending on the presence of selected
      blocks that they see (using Ender eye based rock-radar technology). A block detector
      scans a line (up to 8 blocks far) in font of it. Redstone outputs are at all sides
      except the front. The touch config is at the opposite side:

        - Radar range       : 0 to 8. How far the sensor shall look. 0 and 1 are basically
                              the same, except that range=0 will update faster (because the
                              "block update of the neighbour block" will be taken into account).
                              Normal update interval of the switch is 0.5s (10 ticks).
        - Match threshold   : 1 to range. The number of blocks that have to meet the selected
                              filter (see below) to switch the output signal on. The value is
                              clamped to the range value.
        - Output power      : 1 to 15.
        - Debouncing        : 0 to 10. Delays switching to ensure a stable detector signal.
                              Useful e.g. for fluids, which may otherwise cause jitters when
                              removed with a bucket.
        - Filter            : Category selection for block detection. The sensor will only count
                              blocks matching the selected types. Some of the criteria are at least
                              partially ore dicrionary based (see Forge oredict). Categories are:
                              "any block", "solid blocks", "fluids", "air/nothing", "plants",
                              "wooden", "stone", "glass", "clay", "water", "ore", "wood log",
                              "crop", "mature crop", "sapling", "soil", "fertile soil", "planks",
                              and "slabs".
                              Most of the strict criteria are adapted in a way that should yield
                              expected results to the player. E.g. "water" also matches ice and snow,
                              "soil" match dirt, grass, sand, and soul sand. "clay" also matches
                              terracotta, "wooden" also matches a book shelf, "stone" also matches
                              terracotta, obsidian, and quarz.
                              *Unfortunately not all blocks (mainly blocks from mods) can be matched
                              correctly for every category. It depends on the ore dictionary registration
                              and on which vanilla blocks they are based. (Means e.g. not all modded
                              blocks that look clearly made out of stone tell the game that they are
                              stone). Simply check out what's possible and maybe circumvent the problem
                              by choosing another setup and filter*.

### Switch link details

Linking switches means establishing a "wireless" remote activation of switches based
on the matter and energy teleportation capabilities of Ender pearls.

- When left-clicking a switch with an Ender pearl, this *Ender pearl* remembers the
  position, type and features of the clicked switch, and becomes a *Switch Link Pearl*.
  The clicked switch is memorised as *link target*, and will be actuated by the pearl
  in your hand. Hover the pearl in your inventory, the tool tip shows where a link
  goes to, how far you are away (and how the pearl behaves when placed in other switches,
  read on).

- This pearl will remotely activate the linked switch when squeezed a bit (manually
  used while in the main hand. You can hear that, as it whispers in Endermen tounge).

- By massively shaking the *Switch Link Pearl* in the crafting area, it loses its
  memory and becomes an ordinary *Ender pearl* again.

- Linking is only stable in a range of 48 blocks (see mod config), it will let you
  know when you are too far away. It will also complain aloud when the link is broken
  because the target switch was removed (or the chunk not loaded etc).

- By left-clicking, link pearls can also be placed in other switches, so that they
  are squeezed when that switch activates. The switch you clicked is then a *link source*
  switch. One switch can hold quite a few link pearls if needed, and the links are
  triggered in the order they are plugged into the switch. Similar to the manual use,
  the pearl will complain when something's not right. You can hear it by carefully
  listening when the switch is activated.

- Pearls in switches (*link sources*) trigger their link targets at the moment they
  they are squeezed (activated) or released (deactivated). They do not continuously
  or frequently update their links.

  - The default behaviour of a link perl is to mimic the state of its own switch,
    means when it's own source switch is activated it will try to make the target
    switch being activated as well. For the other case, if its own source switch
    deactivates, it will see that the link target switch is also deactivated. To
    make this happen the pearl checks the state of the target and either triggers
    it because the state has to be changed, or it does nothing because the target
    is already how it should be.

  Things would be boring if we could not change that, therefore you can left-click
  on the target switch to change this default trigger behaviour. That's normally
  done in one go when assigning the target with an Ender pearl, so that we define
  not only that the target switch is actuated, but also how it is actuated. You
  can left-click-cycle through four link settings:

  - 1st: The default as above. The pearl tries to set `target=source` by activating
         or not activating the target. *In technical terms RS-behaviour.*
  - 2nd: The target switch is always activated when the source switch is activated,
         no matter if the target is currently on or off. *In technical terms rising
         edge trigger.*
  - 3rd: The target switch is always activated when the source switch is deactivated,
         no matter if the target is currently on or off. *In technical terms falling
         edge trigger.*
  - 4th: The target switch is always activated when the source switch changes, no
         matter if the target is currently on or off. *In technical terms triggering
         on both edges.*

- Links support chain reaction. You can place a link from e.g. switch B into switch A,
  and then one from switch C into B. Activating A will then trigger B, which will also
  trigger C. To the naked eye triggering happens at the same time. If you manage to
  create cyclic links, such as A to B and B to A, the linking will still work - but the
  pearl of one switch will complain. Link pearls don't want to be triggered more than
  once per tick (must be some kind of exhaustion effect).

- Not all switches can be activated with link pearls - some because their activation
  mechanism is too complicated, some because link pearls don't like them for whatever
  reason. Examples are contact switches, day timer clocks, or environmental sensor
  switches. If a switch connot be a link target, the Ender pearl will already complain
  and not remembering the switch when you click it.

- In the same manner, not all switches are compatible to link pearls, and therefore
  connot be used as "link source". For example the interval timer cannot hold pearls.
  The link pearl will let you know when trying to plug it.

- Switches can have individual behaviour when being linked, each in a way that is
  sensible for that switch:

  - Bi-stable switches flip as if manually activated. If it contains link pearls itself,
    these pearls will trigger, too.

  - Pulse switches behave as if manually activated, but their pulse time ("on-time") is
    not affected by the link in a source switch. Link pearls in the target switch will
    also trigger their targets. Pulse switches cannot be switched off by links like
    bi-stable switches.

  - Contact switches normally don't allow being linked to. Only shock sensitive trapdoors
    will swing open, but the link pearls in the trapdoors will not trigger their targets.

  - Automatic switches are normally no link targets neither, too complicated for pearls.
    Only the interval timer can be activated and deactivated (STANDBY->RUN, RUN->STANDBY).

This allows some interesting setups, not only the binary counter with chain linked levers.
*Well, normally no need to say more, Minecrafters know what to do when they see something*.


### Community references

Mods covering similar features:

- [Automated Redstone (CD4017BE)](https://minecraft.curseforge.com/projects/automated-redstone) has redstone a display and an oscilloscope block.

- [More Beautiful Buttons (Kreezxil/Serj4ever57203)](https://minecraft.curseforge.com/projects/more-beautiful-buttons) support a variety of differently colored buttons.

- [MalisisSwitches (Ordinastie)](https://github.com/Ordinastie/MalisisSwitches) adds remote (wireless) switches to Minecraft.

- [Lever & Button Lights (Kreezxil)](https://github.com/kreezxil/Lever-Button-Lights) Buttons and levers that are also light sources.

- [Dazzle (quat1024)](https://github.com/quat1024/md18) has different lamp types (analog, modern, etc, different colours) also suitable for redstone power indication.

- [Random Things (Lumien)](https://github.com/lumien231/Random-Things) has "contact levers" and "contact buttons", which you can hide behind the block you want to click to use as button or lever.

- [Project Red (MrTJP)](https://minecraft.curseforge.com/projects/project-red-base) also provides indicators and switches

- The [Immersive Engineering](https://github.com/BluSunrize/ImmersiveEngineering/) low voltage switch is a stylish redstone lever, too.

Making this mod was basically a weekend project for me to check what's new in Java - well, let's just say I caught fire ;).
To learn how mods work I took a look at the following codes and resources to get started, and like to give the authors
credits accordingly: lothazar:cyclic, blusunrize:ie, vazkii:botania; the Forge smiths and documenters, thegrayghost.
