# Build status

- Current target: Minecraft 26.1.2 / NeoForge 26.1.2.86 / Java 25 bytecode / ModDevGradle 2.0.143 / Gradle 9.6.1.
- Core source result: 268 classes are packaged. The original core classes, four item-model property classes, and thirteen targeted Minecraft 26 block-entity renderer/model classes were compiled against the locally cached Minecraft 26.1.2 / NeoForge 26.1.2.86 API.
- NeoForge 26.1.2.86 metadata: generated successfully from the current Gradle configuration. The JAR requires NeoForge `[26.1.2.86,)` and Minecraft `[26.1.2]`.
- Compatibility verification: compared the local 26.1.2.86 and 26.1.2.94 NeoForge source/userdev artifacts. The compiled mod contains no bytecode references to the NeoForge APIs added or changed between those builds.
- Crash fixes: supplies the mandatory Minecraft 26 registry IDs before every static and dynamic block/item constructor, and avoids constructing a bowl-conversion `ItemStack` before item components are bound.
- Runtime result: the user confirmed that the registration-fix build starts successfully in the game.
- Resource/model fix: adds all 213 Minecraft 26 item-definition entry files, updates the resource-pack format from 48 to Minecraft 26.1.2's format 84, and replaces the removed `neoforge:separate_transforms` loader used by cold-cut ham slices, the fruit basket, and the teapot with Minecraft 26 display-context selectors.
- Dynamic item-model fix: migrates the seven remaining legacy `overrides` wrappers (`kitchen_shovel`, `oil_pot`, `recipe_item`, `raw_dough`, `steamer`, `stockpot_lid`, and `transmutation_lunch_bag`) to Minecraft 26 condition/range-dispatch item definitions. Custom NeoForge conditional properties preserve their oil, recipe, and contents state; the stockpot lid and raw dough use the matching vanilla using-item/use-duration properties. Also adds the missing English and Chinese `recipe_item` name.
- Cooking renderer fix: adds Minecraft 26 render-state implementations for the chopping board, millstone, shawarma spit, wok/pot, and stockpot. The chopping board displays its current ingredient, the millstone body and contents are restored, and cooking ingredients remain visible in the shawarma spit and pots.
- Recipe migration: converts all 518 recipe JSON files and 1,869 ingredient references to Minecraft 26's string/tag syntax. Twenty-seven no-carrier stockpot recipes now use the mod's internal sentinel, and stockpot takeout recognizes that sentinel without requiring a physical carrier.
- Pack metadata fix: declares the Minecraft 26 resource/data pack range from resource format 84.0 through data format 101.1, removing the missing `min_format`/`max_format` warning.
- Deliverable: `build-local/libs/kaleidoscope_cookery-1.4.1-neoforge26.1.2.86+mc26.1.2-cooking-render-recipe-fix-v5.jar`.
- Deliverable SHA-256: `0827BB9D581572948E4050BA4997EAC968D2F5DC7294FBF61341B0523C947172`.

## Verification limits

- A fresh Gradle regeneration of the Minecraft/NeoForge 26.1.2.86 patched development artifact reached NFRT successfully, but the sandbox blocked NFRT from downloading Mojang's launcher manifest.
- Gradle's original `compileJava` invocation compiled the core classes without Java errors, then its post-compilation input snapshot was blocked from reading `typetools-0.6.3.jar` by the Windows sandbox. Targeted compatibility classes were compiled separately against local 26.1.2.86 artifacts; JDK 26 emits a Windows zip-file cleanup exception only after producing valid Java 25 class files. Gradle's final `processResources jar` run reported `BUILD SUCCESSFUL`.
- The v5 JAR contains 213 item definitions, 518 migrated recipes, and 268 compiled classes. All 3,127 packaged JSON/metadata documents parse successfully, no recipe contains the removed object-style ingredient syntax or an empty carrier array, and all new/updated classes use Java class-file version 69. In-game visual and interaction verification remains for the user to perform.

## Deferred modules

The current core-first build still excludes the Minecraft 26 client-render migration, optional third-party integrations, data generators, and several world-generation/loot hooks. Their source remains in the project for later migration.
