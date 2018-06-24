
# Redstone Gauges and Switches (`rsgauges`)

`rsgaues` is a small [Minecraft](https://minecraft.net) (Java Edition) mod based on
[`Forge`](http://www.minecraftforge.net/). The Mod adds devices to measure or "produce"
redstone power to the game:

- Gauges are small devices, which can be attached to any solid block in the game,
  and they measure the redstone power that this blocks receives from blocks around
  it. Their displays are analog or digital and quantised from 0 to 15. The measurement 
  method differs a bit from how e.g. comparators or redstone lamps react, see the 
  details below.

- Indicators are on-off displays attached to blocks, measuring like gauges (and are 
  basically gauges). They are "off" if the power is zero, and "on" if the power is 
  greater than zero. It depends on the indicator how the display looks like, some
  are simple LED like lights, some are blinking, etc.

- Bistable switches are basically vanilla Minecraft levers with a different style.
  They produce strong redstone power in the block they are attached to and are manually
  switched on and off. There are currently no special features different from levers
  implemented.

- Pulse switches are like vanilla Minecraft buttons with a different style.
  They also produce strong redstone power in the block they are attached to, and they
  switch off automatically after a short time. However, pulse switches can be pushed
  multiple times, extending the delay (each right-click) before switching off again.


#### Summary screenshot

- Green framed: The currently implemented gauges, five analog and one digital display.
  Topmost row: unpowered, row 2: powered by comparators behind the wall blocks (power
  12 to 7).

- Blue framed: Indicators, bottom row: powered with redstone torches under the wall
  blocks, row 3, unpowered. The alarm lamp (column 4) is always blinking when powered, 
  the small square lights have steady and blinking variants.
  
- Yellow framed: Pulse switches (well, "the" pulse switch).

- Orange framed: Bistable switches. Note that the ESTOP is inverted - it switches off
  when pushed (it's an emergency stop button after all ;) ).

![Summary of meters and switches](documentation/annotated-rsgauges-summary.png) 

#### Testing ground screenshot

Development gamesave (flat world) invoked from the IDE. Each gauge and indicator
is installed with each power level, different kind of blocks are used to attach
the gauges to, etc. Should not differ much from the way others do it.

![Screenshot of the testing grounds](documentation/annotated-testing-grounds.png)

#### Testing gameplay screenshot

Testing game with 85 other mods installed, mainly to check compatibility and recipe
collisions (and it's also simply fun). The screenshot shows the front end of a (electrical) 
Diesel generator control. Walls and the red wiring is from [Blusunrize's Immersive Engineering](https://github.com/BluSunrize/ImmersiveEngineering/), 
the door from [Nihiltres Engineer's Doors](https://minecraft.curseforge.com/projects/engineers-doors/),
wall drawings from [Chisels and bits by AlgorithmX2](https://github.com/AlgorithmX2/Chisels-and-Bits),
gauges, the ESTOP button and the indicator above the door from this mod. The voltmeter
style gauges are used to show the state of charge of buffering capacitors (IE), the ESTOP
is to force the Diesel control off (actually an assembly language programmed [Minecoprocessor by ToroCraft](https://minecraft.curseforge.com/projects/minecoprocessors/)). The LED is used to show if the
Diesel generator is currently running or not.

![Screenshot a testing game with 85 other mods installed](documentation/annotated-wile-testgame-screenshot1.png)

## Details

#### Gauges and indicators

Gauges measure a bit differently as e.g. redstone lamps. The gauges do not react 
to the (strong or weak) power they receive themselves, but lookup the power at 
the adjacent block they face. This prevents the gauges from seeing indirect (weak) 
power from adjacent blocks, causing incorrect display values. But this also has
a catch: E.g. testing with the [Automated redstone mod](https://minecraft.curseforge.com/projects/automated-redstone)
I had to realise that CD4017BE takes this issue already into account and uses
weak power for sensor outputs and the like - which the gauge does not detect. So
it's already scheduled that the gauges can be switched between weak and strong
power sensitivity.

According to the implementation, gauges have fixed model and texture definitions
(baked on startup by the Forge). Rendering is mainly triggered by block updates,
but backed up by a tile entity tick (client only, configurable sample rate, default
20 ticks) in case of packet losses or other synchronisation issues.

Indicators are gauges in the source code (same class), with a different configuration
during construction. So gauges can generally determine powers from 0 to 15, emit light,
and have alternation (blinking) features.  

#### Switches

Switches don't use tile entities, so they function pretty much like vanilla buttons
and levers. Only difference is that the pulse switch re-schedules the block update
with a higher delay when it is pressed again while being active. The delays are 
in ticks: 5 (first click when off), 10, 20, 40, 80. 

#### Style and textures

Although frequently using higher texture resolution than 16x16 and "sub pixel" model definitions
I tried to stick to the rather-square-than-circle style that makes Minecraft so neat. Hope you
don't mind.

## Community references

Mods covering similar features:

- [Automated Redstone (CD4017BE)](https://minecraft.curseforge.com/projects/automated-redstone) has redstone a display and an oscilloscope block.

- [More Beautiful Buttons (Kreezxil/Serj4ever57203)](https://minecraft.curseforge.com/projects/more-beautiful-buttons) support a variety of differently colored buttons.

- [MalisisSwitches (Ordinastie)](https://github.com/Ordinastie/MalisisSwitches) adds remote (wireless) switches to Minecraft.

- [Lever & Button Lights (Kreezxil)](https://github.com/kreezxil/Lever-Button-Lights) Buttons and levers that are also light sources.

- [Dazzle (quat1024)](https://github.com/quat1024/md18) has different lamp types (analog, modern, etc, different colours) also suitable for redstone power indication. 

- [Random Things (Lumien)](https://github.com/lumien231/Random-Things) has "contact levers" and "contact buttons", which you can hide behind the block you want to click to use as button or lever.

- The [Immersive Engineering](https://github.com/BluSunrize/ImmersiveEngineering/) low voltage switch is a stylish redstone lever, too.

