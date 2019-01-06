
#Tasks for version update to Minecraft 1.13

### References

  - [1] https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a
  - [2] https://github.com/ModCoderPack/MCPBot-Issues/blob/master/migrations/1.12-to-1.13.md
  - [3] https://minecraft.gamepedia.com/1.13#General

### Tasks

    [ ] Java sources

      [X] Flatten block variants.
          (Ok, already done for sensitive glass, no other blocks affected).

      [X] Flatten items.
          (ok, no item affected).
      [X] Flatten item-blocks.
          (ok, auto generated from registered blocks).

      [ ] Remove final class ModBlocks::VersionTransition.

      [X] ResourceLocation must have c++ like names (snake_lowercase_underscore).
          (Ok, rl's are all based on MODID and registryname, which are all lowercase.)

      [ ] MCP renames (see [2])

        [X] client.renderer.tileentity.TileEntitySpecialRenderer 	client.renderer.tileentity.TileEntityRenderer.
            (Used in JitBakedModel, unused --> removed.)

        [ ] creativetab.CreativeTabs 	item.ItemGroup
            [2] "Blocks no longer have a creativetab, that belongs on their ItemBlocks" [...]
            "Block attributes like their material or hardness are now passed in a builder/POJO
             to the block constructor, and are final Similar for Items" [...]




      [X] Remove ItemMeshDefinition references.
          (Used in JitBakedModel, unused --> removed.)

      [ ] Replace all direct block object compare instructions (state.getBlock()==AIR) with
          ((state.isAir()), also check all ".getMaterial() == Material.AIR", there are different
          AIRs now. This will not show up an error, just not work during run time!)

      [ ] Remove getMetaFromState() and getStateFromMeta() (blockstates are implicitly stored by MC),
      [X] Remove all evaluations of integral meta data values.
          (Ok, not used anyway.)

      [X] Move tool damage into NBT data, inside a key with the name "tag". N/A.


    [ ] Blockstate JSON files:

      [ ] Rename variant "normal" to "".

      [ ] Replace model locations "rsgauges:models/$modelname" to "rsgauges:models/block/$modelname".


    [ ] File system structure:

      [X] Lowercase filesystem paths. OK, already initially done that way.

      [ ] Rename "textures/blocks" to "textures/block"

      [ ] Rename "textures/items" to "textures/item"

      [X] Check vanilla texture names.
          Ok, only mod specific textures used.

      [ ] Move recipes (workbench recipes) from "assets/rsgauges/" to "data/rsgauges/".
      [X] Move loot_tables from "assets/" to "data/". N/A
      [X] Move structures from "assets/" to "data/". N/A
      [X] Move advancements from "assets/" to "data/". N/A
      [X] Leave client-only files in "assets/".

    [ ] Lang files

      [ ] Convert all lang files to JSON. Major pain, no idea how cross referenced
          argument formatting works. Kacke.

    [ ] Recipe files

      [X] All recipe JSON files must exist (also for java recipe implementations).
          OK, only using JSON based recipes.

      [ ] Move recipe JSONs from "assets/" to "data/".

    [ ] Tags: See where tags can to come into play. E.g. [2]:
        - e.g. data/rsgauges/tags/blocks/bistable_switch.json
        - e.g. data/rsgauges/tags/blocks/sound_indicator.json
        - e.g. data/rsgauges/tags/blocks/switch_series_rustic.json

      [ ] Sensitive glass variants?
      [ ] Switch types (bistable, pulse, contact, auto ...) ?
      [ ] Gauges
      [ ] Indicators and variants led,sound,lamp ...

  [X] Commands (mod not affected)
  [X] World generation (mod not affected)
