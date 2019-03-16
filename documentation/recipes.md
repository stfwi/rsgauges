
### Recipes

The blocks of this mod can be crafted from vanilla resources whilst trying to prevent
recipe collisions with other mods. To get the first device of a series (or "type", e.g.
timers, environmental sensors, etc), a 3x3 shaped crafting recipe is used:

      # # #     | "S": Style series selection ingredient (industrial, rustic ...)
      # D #     | "#": Framing ingredient (normally iron nugget)
      # S #     | "D": Device selection ingredient (gauge, switch, timer, ...)

Other devices of the same style series (or the same type) can then be obtained
using by 1x1 crafting the result, means cycling through different styles of the
same device. Reaching the last device in a list will wrap over to the first again.
This system is used for devices wherever possible. Example:

    - Industrial gauges: S=redstone dust, #=iron nugget, D=comparator

      NNN           | 1-           | 2-           |     | N-
      NCN -> gauge1 | -- -> gauge2 | -- -> gauge3 | ... | -- ->gauge1
      NRN           |              |              |     |
      ----------------------------------------------------------------
       initial 3x3  >    next      >    next      > ... >  last->first

In general, metering and switching devices are crafted like:

    - Series selection

      - Industrial  : S=redstone     #=iron nugget   D=[selects device]
      - Rustic      : S=stick        #=iron nugget   D=[selects device]
      - Old fancy   : S=gold-nugget  #=iron nugget   D=[selects device]
      - Glass       : S=glass-block  #=iron nugget   D=[selects device]

    - Device selection

      - small indicators     : D=glowstone-dust  (e.g. LEDs, small semaphores)
      - lamp indicators      : D=redstone-lamp   (e.g. alarm lamp)
      - sound indicators     : D=note-block      (e.g. alarm siren)
      - pulse switches       : D=button          (e.g. machine button)
      - bi-stable switches   : D=lever           (e.g. rustic lever)
      - contact mats/plates  : D=pressure-plate  (e.g. door contact mat)
      - trapdoor switches    : D=trap-door       (e.g. shock activated trapdoor)
      - detector switches    : D=ender-eye       (e.g. infrared motion detector)
      - timer switches       : D=clock           (e.g. day timer clock)
      - environmental sensors: D=daylight-sensor (e.g. light,rain,thunderstorm)

There are some blocks in the mod, which don't follow this system for specific
reasons:

  - Redstone sensitive glass and its variants is not per se a device, but more a
    decorative (full) block:

        N G N     | "N": iron-nugget       "#": [variant selection]
        # R #     | "R": redstone dust
        N G N     | "G": glass-block

        "#"=glowstone-block -> light emitting redstone sensitive glass
        "#"=any dye         -> tinted/stained redstone sensitive glass

        (Light emitting sensitive glass can be inverted using unshaped
        crafting with a redstone torch, like a lit redstone lamp.)

----
