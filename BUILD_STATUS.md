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

## Dedicated-server fix v24

- Removed obsolete client-only item predicate signatures from common item classes so static item registration no longer resolves `net.minecraft.client.multiplayer.ClientLevel` on a dedicated server.
- Updated teapot tooltip/content decoding to use the registry provider supplied by the common item API instead of accessing the client `Minecraft` singleton.
- Recompiled 12 affected item class files as Java 25 bytecode and replaced them in the branded JAR.
- Verification: 316 packaged classes; zero `net/minecraft/client` references in item classes or other non-client/non-compat/non-datagen classes; original icon, cover, and NeoForge metadata retained.
- Deliverable: `outputs/kaleidoscope_cookery-1.4.1-neoforge26.1.2.86+mc26.1.2-dedicated-server-fix-v24.jar`.
- Deliverable SHA-256: `F055A4D6D5546BFA5441D8CB764C04EEE21664D3E77447372A5960B1EF1283CB`.

## Recipe, armor, and food component fix v25

- Verified and repackaged the shawarma-spit shaped recipe (`ICI` / `ICI`, campfire and chains) together with its survival recipe-book advancement.
- Added Minecraft 26 equipment definitions and humanoid, baby-humanoid, and leggings texture paths for the complete farmer armor set.
- Ported the straw hat and flowered straw hat to a dedicated Minecraft 26 client armor model while preserving their separate 64x64 textures.
- Added Minecraft 26 food and consumable components to the baozi plate, shengjian-mantou plate, and all registered tea cups. Deterministic status effects are shown in tooltips and all configured effects are applied by the vanilla consumable pipeline.
- Targeted compilation succeeded against Minecraft 26.1.2 / NeoForge 26.1.2.86 with Java 25 bytecode. The final archive contains 4,980 entries and all updated JSON files parse successfully.
- Deliverable: `outputs/kaleidoscope_cookery-1.4.1-neoforge26.1.2.86+mc26.1.2-recipe-armor-food-fix-v25.jar`.
- Deliverable SHA-256: `2BD5A24EA385E8B62AF3F2E136F11240BDE2F1AE351198804F1244B2DB0A6E73`.

## Recipe, tea tooltip, and straw-hat crash fix v26

- Diagnosed the missing shawarma-spit recipe from the supplied game log: Minecraft 26.1.2 removed `minecraft:chain` and registers the ingredient as `minecraft:iron_chain`. The recipe now uses the live registry ID and preserves the required `ICI` / `ICI` pattern.
- Removed the v25 food, buff, and effect components from the baozi plate and shengjian-mantou plate as requested.
- Tea cups keep their original maxim/flavour description and effect tooltip, while Minecraft 26's consumable component applies the configured tea effect without adding hunger or saturation food properties.
- Diagnosed the straw-hat entity crash as a missing baked HumanoidModel child. Rebuilt the model from Minecraft 26's complete humanoid mesh, then replaced only the head geometry. A direct bake-and-constructor test reports `STRAW_HAT_MODEL_OK`.
- Targeted Java 25 compilation succeeded. The final archive contains 4,980 entries and a package-level recipe/model verification reports `V26_JAR_OK`.
- Deliverable: `outputs/kaleidoscope_cookery-1.4.1-neoforge26.1.2.86+mc26.1.2-recipe-tea-hat-fix-v26.jar`.
- Deliverable SHA-256: `4B041943C35519B39533FFFD81A7D16D75A18C43A7505F8D91F5ACA3F35719E8`.

## Tea tooltip, straw-hat fit, and dough animation fix v27

- Ported tea tooltips to Minecraft 26's current five-argument `appendHoverText` API. All tea cups now expose their translated maxim/flavour line and deterministic status-effect duration without adding hunger or saturation properties.
- Raised the straw-hat mesh by 3.5 model units to remove the crown/back-of-head clipping shown in the supplied screenshot. Direct model bake and construction still reports `STRAW_HAT_MODEL_OK`.
- Corrected the raw-dough item-definition timing. The migrated definition had accidentally multiplied elapsed use time by 0.1 and compared it to the old custom-property thresholds. It now reproduces the original stages at 1, 10, 20, and 30 use ticks, ending with the original raw-noodle conversion at tick 30.
- Targeted Java 25 compilation succeeded; updated item-definition and retained shawarma recipe JSON pass package verification.
- Deliverable: `outputs/kaleidoscope_cookery-1.4.1-neoforge26.1.2.86+mc26.1.2-tea-hat-dough-fix-v27.jar`.
- Deliverable SHA-256: `D744720D93C15CC34CC610E646AB56CF073617A81115B24F707BC496D2B6DE82`.

## KubeJS 8 compatibility update 1.2.10 (2026-08-26)

- Restored the optional KubeJS integration and migrated its recipe schemas to KubeJS 8.0.4 for Minecraft 26.1.2.
- Added the current KubeJS plugin declaration in `kubejs.plugins.txt` and kept KubeJS optional in NeoForge metadata, so the core mod still starts without KubeJS installed.
- Updated stockpot and pot recipe schemas to the current list and optional-ingredient APIs.
- Added the official KubeJS Maven repository together with compile-only and development-runtime dependencies for KubeJS 8.0.4 and Rhino 91.
- Verification: Gradle build succeeded; a development server discovered the Kaleidoscope Cookery KubeJS plugin, loaded 2,063 recipes, and reported zero failed recipes.
- Deliverables: `outputs/kaleidoscope_cookery_mc26.1.2_1.2.10.jar` and `outputs/kaleidoscope_cookery_mc26.1.2_1.2.10-sources.jar`.
