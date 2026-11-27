package cookery.regression;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.*;
import com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.KilledTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod("cookery_regression")
public final class CookeryRegression {
    private static final List<String> report = new ArrayList<>();
    private static final List<ItemStack> deathDrops = new ArrayList<>();
    private static LivingEntity subject;

    public CookeryRegression() {
        NeoForge.EVENT_BUS.addListener(CookeryRegression::drops);
        NeoForge.EVENT_BUS.addListener(CookeryRegression::run);
    }

    private static void drops(LivingDropsEvent event) {
        if (event.getEntity() == subject) {
            for (var item : event.getDrops()) deathDrops.add(item.getItem().copy());
        }
    }

    private static Identifier id(String value) { return Identifier.parse(value); }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    private static void log(String value) {
        report.add(value);
        System.out.println("COOKERY_REGRESSION " + value);
    }

    private static void run(ServerStartedEvent event) {
        var server = event.getServer();
        if (!server.isDedicatedServer()) return;
        try {
            ServerLevel level = server.overworld();
            FakePlayer player = FakePlayerFactory.getMinecraft(level);
            player.setPos(0.5, 100, 0.5);
            level.getChunk(0, 0);
            check(level.getGameRules().get(GameRules.MOB_DROPS), "Mob drops must be enabled");
            String[] knives = {"iron", "gold", "diamond", "netherite"};
            String[] mobs = {"pig", "piglin", "piglin_brute", "hoglin", "zombified_piglin", "zoglin"};
            int kills = 0;
            for (String mob : mobs) {
                for (String knife : knives) {
                    int min = Integer.MAX_VALUE, max = 0;
                    for (int i = 0; i < 8; i++) {
                        int oil = kill(level, player, mob, knife(knife), false);
                        int rolls = mob.equals("pig") || mob.equals("zombified_piglin") ? 1 : 2;
                        check(oil >= rolls && oil <= 2 * rolls, mob + "/" + knife + " oil=" + oil);
                        min = Math.min(min, oil); max = Math.max(max, oil); kills++;
                    }
                    log("DROP " + mob + "/" + knife + " 8 kills oil=" + min + ".." + max);
                }
                check(kill(level, player, mob, new ItemStack(Items.IRON_SWORD), false) == 0, mob + " sword yielded oil");
                check(kill(level, player, mob, ItemStack.EMPTY, false) == 0, mob + " empty hand yielded oil");
                player.setItemSlot(EquipmentSlot.OFFHAND, knife("iron"));
                check(kill(level, player, mob, new ItemStack(Items.IRON_SWORD), false) == 0, mob + " offhand knife yielded oil");
                player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                check(kill(level, player, mob, knife("iron"), true) == 0, mob + " environmental death yielded oil");
                kills += 4;
            }
            int lootingTotal = 0, lootingMax = 0;
            for (int i = 0; i < 64; i++) {
                ItemStack enchanted = knife("iron");
                enchanted.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), 3);
                int oil = kill(level, player, "pig", enchanted, false);
                check(oil >= 1 && oil <= 5, "Looting III amount out of bounds: " + oil);
                lootingTotal += oil; lootingMax = Math.max(lootingMax, oil); kills++;
            }
            check(lootingMax > 2, "Looting III did not increase oil drops");
            log("LOOTING_III 64 pigs total=" + lootingTotal + " max=" + lootingMax);
            testAdvancement(level, player);
            testRecipes(level);
            testVersion15Content();
            testVersion15Recipes(level);
            testVersion151(level, player);
            if (net.neoforged.fml.ModList.get().isLoaded("kubejs")) testKubeJS(level);
            testTrades(level);
            testRecipeRecord(level);
            if (net.neoforged.fml.ModList.get().isLoaded("farmersdelight")) testFarmersDelight(level);
            testVillageKitchens(level);
            testGrassAndChestLoot(level, player);
            testSickleHarvest(level, player);
            testTeaSeedPlanting(level, player);
            Version160Regression.verify(level, player);
            Version170Regression.verify(level, player);
            log("VERSION_1_7_0 linked shelves, 256-stack persistence, recipe dispense and syrup batch extraction passed");
            log("VERSION_1_6_0 dispenser, multi-output save migration, lunch bag, millstone and enchantments passed");
            for (DyeColor color : DyeColor.values()) {
                check(CarpetColor.getColorByCarpet(CarpetColor.getCarpetByColor(color)) == color, "Carpet " + color);
            }
            log("CARPET 16 colors round trip passed");
            log("PASS actual death tests=" + kills);
        } catch (Throwable error) {
            log("FAIL " + error);
            error.printStackTrace();
        } finally {
            try { Files.write(Path.of("cookery-regression-result.txt"), report); }
            catch (Exception error) { error.printStackTrace(); }
            server.halt(false);
        }
    }

    private static void testRecipeRecord(ServerLevel level) {
        var original = new com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem.RecipeRecord(
            List.of(new ItemStack(Items.PORKCHOP), ItemStack.EMPTY, new ItemStack(Items.SUGAR, 2)),
            new ItemStack(Items.COOKED_PORKCHOP), id("kaleidoscope_cookery:pot"), false);
        var ops = RegistryOps.create(JsonOps.INSTANCE, level.registryAccess());
        var codec = com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem.RecipeRecord.CODEC;
        var json = codec.encodeStart(ops, original).getOrThrow();
        var decoded = codec.parse(ops, json).getOrThrow();
        check(original.equals(decoded), "Recipe paper JSON lost data or empty slots");
        var wire = com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem.RecipeRecord.STREAM_CODEC;
        var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
        try {
            wire.encode(buf, original);
            check(original.equals(wire.decode(buf)) && buf.readableBytes() == 0, "Recipe paper network round trip");
        } finally { buf.release(); }
        log("RECIPE_RECORD legacy JSON/network round trips including empty input slots passed");
    }

    private static void testKubeJS(ServerLevel level) {
        String[] types = {"pot", "flex_pot", "stockpot", "flex_stockpot", "millstone", "chopping_board", "steamer", "teapot", "bamboo_tray"};
        for (String type : types) {
            var holder = level.getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id("kubejs:cookery_regression/" + type)));
            check(holder.isPresent(), "Missing scripted recipe helper: " + type);
            check(BuiltInRegistries.RECIPE_SERIALIZER.getKey(holder.get().value().getSerializer()).equals(id("kaleidoscope_cookery:" + type)), "Wrong scripted recipe type: " + type);
        }
        log("KUBEJS all 9 typed recipe helpers loaded and passed matching/serialization tests");
    }

    private static void testVersion15Content() {
        String[] items = {"bamboo_tray", "tieguanyin_tea_bag", "biluochun_tea_bag", "oolong_tea_bag",
            "sakura_fubuki_tea_bag", "barley_tea_bag", "butter_tea_bag", "clay_pot_milk_tea",
            "tea_banner", "tea_seed", "long_bench", "red_lantern", "eight_immortals_table",
            "fresh_tea_leaves", "dried_tea_leaves", "tea_egg", "mystery_tea", "butter_tea"};
        for (String item : items) {
            check(BuiltInRegistries.ITEM.containsKey(id("kaleidoscope_cookery:" + item)), "Missing 1.5 item " + item);
        }
        String[] blocks = {"bamboo_tray", "clay_pot_milk_tea", "tea_banner", "tea_tree", "long_bench",
            "red_lantern", "eight_immortals_table", "mystery_tea", "butter_tea"};
        for (String block : blocks) {
            check(BuiltInRegistries.BLOCK.containsKey(id("kaleidoscope_cookery:" + block)), "Missing 1.5 block " + block);
        }
        check(BuiltInRegistries.RECIPE_SERIALIZER.containsKey(id("kaleidoscope_cookery:bamboo_tray")), "Missing bamboo tray serializer");
        check(com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager.containsSoupBase(id("minecraft:milk")), "Missing milk soup base");
        log("VERSION_1_5 all 18 items, 9 blocks, bamboo tray recipes and milk soup base registered");
    }


    private static void testVersion151(ServerLevel level, FakePlayer player) {
        var pos = new net.minecraft.core.BlockPos(10, 110, 10);
        var facing = net.minecraft.core.Direction.EAST;
        player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        for (String name : List.of("table_oak", "eight_immortals_table")) {
            var block = BuiltInRegistries.BLOCK.getValue(id("kaleidoscope_cookery:" + name));
            var state = block.defaultBlockState();
            var shape = state.getShape(level, pos).bounds();
            var collision = state.getCollisionShape(level, pos).bounds();
            check(shape.minY == 0 && shape.maxY == 1, name + " selection shape");
            check(collision.minY == (name.equals("table_oak") ? 14.0 : 12.0) / 16.0
                && collision.maxY == 1, name + " collision shape");
        }
        var empty = (com.github.ysbbbbbb.kaleidoscopecookery.block.drink.EmptyCupBlock) com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks.EMPTY_CUP.get();
        var hit = new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos),
            net.minecraft.core.Direction.UP, pos, false);
        int variants = 0;
        for (var candidate : BuiltInRegistries.BLOCK) {
            if (!(candidate instanceof com.github.ysbbbbbb.kaleidoscopecookery.block.drink.TeacupBlock tea)) continue;
            for (int count = 1; count <= tea.getMaxCount(); count++) {
                var initial = empty.defaultBlockState()
                    .setValue(com.github.ysbbbbbb.kaleidoscopecookery.block.drink.EmptyCupBlock.CUP_COUNT, count)
                    .setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, facing);
                level.setBlock(pos.below(), net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(), 3);
                level.setBlock(pos, initial, 3);
                var stack = new ItemStack(tea, 2);
                player.setItemSlot(EquipmentSlot.MAINHAND, stack);
                empty.useItemOn(stack, initial, level, pos, player, net.minecraft.world.InteractionHand.MAIN_HAND, hit);
                var result = level.getBlockState(pos);
                if (count < tea.getMaxCount()) {
                    check(result.is(tea) && result.getValue(tea.getCupCountProperty()) == count + 1
                        && result.getValue(tea.getTeaCountProperty()) == 1
                        && result.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) == facing,
                        "Tea cup stacking state " + tea + "/" + count);
                    check(stack.getCount() == 1, "Tea cup stacking consumption");
                } else {
                    check(result.equals(initial) && stack.getCount() == 2, "Full cup stack must reject tea");
                }
            }
            var single = tea.defaultBlockState().setValue(tea.getCupCountProperty(), 1)
                .setValue(tea.getTeaCountProperty(), 1);
            level.setBlock(pos, single, 3);
            player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            tea.useItemOn(ItemStack.EMPTY, single, level, pos, player, net.minecraft.world.InteractionHand.MAIN_HAND, hit);
            check(level.getBlockState(pos).isAir(), "Last tea cup must remove block");
            variants++;
        }
        check(variants >= 7, "Tea variants not registered");
        level.setBlock(pos, com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks.TEAPOT.get().defaultBlockState(), 3);
        var pot = (com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity) level.getBlockEntity(pos);
        check(pot != null && pot.takeTeapot(level, player) && level.getBlockState(pos).isAir(), "Server teapot pickup");
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        log("VERSION_1_5_1 table selection/collision, tea stacking limits/consumption (" + variants + " variants), cup removal and server teapot pickup passed");
    }

    private static void testVersion15Recipes(ServerLevel level) {
        Map<String, String> teaIngredients = Map.of(
            "barley_tea", "kaleidoscope_cookery:barley_tea_bag",
            "biluochun", "kaleidoscope_cookery:biluochun_tea_bag",
            "oolong", "kaleidoscope_cookery:oolong_tea_bag",
            "sakura_fubuki", "kaleidoscope_cookery:sakura_fubuki_tea_bag",
            "tieguanyin", "kaleidoscope_cookery:tieguanyin_tea_bag",
            "flower_tea", "minecraft:lilac");
        Map<String, String> obsoleteIngredients = Map.of(
            "barley_tea", "minecraft:wheat_seeds",
            "biluochun", "minecraft:nautilus_shell",
            "oolong", "minecraft:dragon_breath",
            "sakura_fubuki", "minecraft:pink_petals",
            "tieguanyin", "minecraft:iron_nugget",
            "flower_tea", "minecraft:dandelion");
        for (var entry : teaIngredients.entrySet()) {
            var holder = level.getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE,
                id("kaleidoscope_cookery:teapot/" + entry.getKey()))).orElseThrow();
            check(holder.value() instanceof TeapotRecipe, "Wrong teapot recipe type " + entry.getKey());
            var recipe = (TeapotRecipe) holder.value();
            var expected = BuiltInRegistries.ITEM.getValue(id(entry.getValue()));
            var obsolete = BuiltInRegistries.ITEM.getValue(id(obsoleteIngredients.get(entry.getKey())));
            check(recipe.ingredientCount() == 1, "Tea recipe count " + entry.getKey() + "=" + recipe.ingredientCount());
            check(recipe.ingredient().test(new ItemStack(expected)), "Tea recipe rejected 1.5 ingredient " + entry.getKey());
            check(!recipe.ingredient().test(new ItemStack(obsolete)), "Tea recipe still accepts obsolete ingredient " + entry.getKey());
        }
        for (int count = 1; count <= 4; count++) {
            var holder = level.getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE,
                id("kaleidoscope_cookery:stockpot/tea_egg_count_" + count))).orElseThrow();
            check(holder.value() instanceof StockpotRecipe, "Wrong tea egg recipe type " + count);
            check(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.isEmptyIngredient(
                ((StockpotRecipe) holder.value()).carrier()), "Tea egg recipe unexpectedly requires a bowl " + count);
        }
        log("VERSION_1_5_RECIPES all tea-bag inputs/counts and carrier-free tea eggs passed");
    }

    private static void testFarmersDelight(ServerLevel level) {
        var recipes = com.github.ysbbbbbb.kaleidoscopecookery.util.RecipeLookup.byType(level.recipeAccess(),
            vectorwing.farmersdelight.common.registry.ModRecipeTypes.COOKING.get());
        check(!recipes.isEmpty(), "No Farmers Delight cooking recipes loaded");
        int tested = 0;
        for (var holder : recipes) {
            var original = holder.value();
            var transformed = com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight.CookingPotCompat.transformRecipe(holder);
            check(ItemStack.matches(original.getResultItem(), transformed.value().result()), "FD transformed output " + holder.id());
            check(transformed.value().time() == original.getCookTime(), "FD cooking time " + holder.id());
            var inputs = original.getIngredients().stream().map(BaseRecipe::displayStack).toList();
            check(inputs.stream().noneMatch(ItemStack::isEmpty), "FD input tag empty " + holder.id());
            var soup = com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.DEFAULT_SOUP_BASE;
            var input = new com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput(inputs, soup);
            var event = new com.github.ysbbbbbb.kaleidoscopecookery.api.event.StockpotMatchRecipeEvent.Post(level, null, input,
                com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.EMPTY_ID);
            NeoForge.EVENT_BUS.post(event);
            check(event.getOutput() != null && !event.getOutput().value().result().isEmpty(), "FD fallback failed " + holder.id());
            check(event.getOutput().value().matches(input, level), "FD fallback doesn't match " + holder.id());
            var preserved = new com.github.ysbbbbbb.kaleidoscopecookery.api.event.StockpotMatchRecipeEvent.Post(level, null, input, id("kaleidoscope_cookery:existing"));
            NeoForge.EVENT_BUS.post(preserved);
            check(preserved.getOutput() == null, "FD replaced an existing Cookery recipe");
            if (original.getOutputContainer().isEmpty()) {
                check(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.isEmptyIngredient(transformed.value().carrier()), "FD empty carrier");
            } else {
                check(transformed.value().carrier().test(original.getOutputContainer()), "FD carrier differs " + holder.id());
            }
            tested++;
        }
        log("FARMERS_DELIGHT transformed recipes, fallback matches, carrier/time and native recipe priority passed=" + tested);
    }

    private static ItemStack knife(String material) {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(id("kaleidoscope_cookery:" + material + "_kitchen_knife")));
    }

    private static void testTrades(ServerLevel level) {
        var profession = com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager.CHEF;
        int total = 0, recipePapers = 0;
        int[] expected = {0, 6, 7, 4, 14, 1};
        for (int rank = 1; rank <= 5; rank++) {
            var villager = (net.minecraft.world.entity.npc.villager.Villager) BuiltInRegistries.ENTITY_TYPE
                .getValue(id("minecraft:villager")).create(level, EntitySpawnReason.COMMAND);
            villager.setVillagerData(villager.getVillagerData().withProfession(profession).withLevel(rank));
            var set = level.registryAccess().lookupOrThrow(Registries.TRADE_SET)
                .getOrThrow(profession.get().tradeSetsByLevel().get(rank)).value();
            check(set.getTrades().size() == expected[rank], "Trade count rank " + rank);
            var params = new net.minecraft.world.level.storage.loot.LootParams.Builder(level)
                .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, villager.position())
                .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY, villager)
                .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, net.minecraft.util.Unit.INSTANCE)
                .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.VILLAGER_TRADE);
            var context = new net.minecraft.world.level.storage.loot.LootContext.Builder(params).create(java.util.Optional.empty());
            for (var entry : set.getTrades()) {
                var offer = entry.value().getOffer(context);
                check(offer != null && !offer.getResult().isEmpty(), "Empty trade rank " + rank);
                if (offer.getResult().getItem() instanceof com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem) {
                    var record = com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem.getRecipe(offer.getResult());
                    check(record != null && !record.input().isEmpty() && !record.output().isEmpty(), "Blank recipe paper trade");
                    recipePapers++;
                }
                if (rank == 5) {
                    check(offer.getResult().isEnchanted(), "Master knife missing enchantments");
                    check(offer.getBaseCostA().getCount() >= 13 && offer.getBaseCostA().getCount() <= 27, "Master knife price");
                }
                total++;
            }
            check(!villager.getOffers().isEmpty(), "Chef generated no offers at rank " + rank);
        }
        log("TRADES " + total + " offers generated; recipe papers=" + recipePapers + "; all 5 chef ranks usable");
    }

    private static void testVillageKitchens(ServerLevel level) {
        int index = 0;
        for (String biome : new String[] {"plains", "snowy", "savanna", "desert", "taiga"}) {
            String name = "kaleidoscope_cookery:village/houses/" + biome + "_kitchen";
            var pool = level.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL).getOrThrow(
                ResourceKey.create(Registries.TEMPLATE_POOL, id("minecraft:village/" + biome + "/houses"))).value();
            int weight = pool.getTemplates().stream().filter(e -> e.getFirst().toString().contains(name)).mapToInt(e -> e.getSecond()).sum();
            check(weight == 4, "Kitchen pool weight " + biome + "=" + weight);
            check(pool.getShuffledTemplates(level.getRandom()).stream().filter(e -> e.toString().contains(name)).count() == 4, "Kitchen missing from sampled pool");
            var template = level.getStructureManager().get(id(name)).orElseThrow();
            check(template.getSize().getX() > 0 && template.getSize().getY() > 0, "Empty kitchen " + biome);
            var pos = new net.minecraft.core.BlockPos(64 + index++ * 32, 80, 0);
            var settings = new net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings();
            check(template.placeInWorld(level, pos, pos, settings, level.getRandom(), 2), "Kitchen placement failed " + biome);
            log("VILLAGE " + biome + " weight=4 template placed size=" + template.getSize());
        }
        com.github.ysbbbbbb.kaleidoscopecookery.event.AddVillageStructuresEvent.addVillageStructures(
            new net.neoforged.neoforge.event.server.ServerAboutToStartEvent(level.getServer()));
        var pool = level.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL).getOrThrow(
            ResourceKey.create(Registries.TEMPLATE_POOL, id("minecraft:village/plains/houses"))).value();
        check(pool.getTemplates().stream().filter(e -> e.getFirst().toString().contains("plains_kitchen")).mapToInt(e -> e.getSecond()).sum() == 4,
            "Repeated kitchen initialization duplicated entries");
    }

    private static void testSickleHarvest(ServerLevel level, FakePlayer player) {
        var origin = new net.minecraft.core.BlockPos(160, 120, 160);
        var second = origin.offset(1, 0, 1);
        var solid = origin.offset(2, 0, 0);
        level.setBlock(origin.below(), net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState(), 3);
        level.setBlock(second.below(), net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState(), 3);
        level.setBlock(origin, net.minecraft.world.level.block.Blocks.SHORT_GRASS.defaultBlockState(), 3);
        level.setBlock(second, net.minecraft.world.level.block.Blocks.FERN.defaultBlockState(), 3);
        level.setBlock(solid, net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(), 3);
        player.setPos(origin.getX() + 0.5, origin.getY(), origin.getZ() + 0.5);
        ItemStack sickle = new ItemStack(BuiltInRegistries.ITEM.getValue(id("kaleidoscope_cookery:sickle")));
        player.setItemSlot(EquipmentSlot.MAINHAND, sickle);
        var hit = new net.minecraft.world.phys.BlockHitResult(
            net.minecraft.world.phys.Vec3.atCenterOf(origin), net.minecraft.core.Direction.UP, origin, false);
        sickle.getItem().useOn(new net.minecraft.world.item.context.UseOnContext(
            player, net.minecraft.world.InteractionHand.MAIN_HAND, hit));
        check(level.getBlockState(origin).isAir(), "Sickle did not cut short grass");
        check(level.getBlockState(second).isAir(), "Sickle did not cut fern in sweep area");
        check(level.getBlockState(solid).is(net.minecraft.world.level.block.Blocks.STONE), "Sickle destroyed solid block");
        log("SICKLE short grass and fern harvested; solid block preserved");
    }

    private static void testTeaSeedPlanting(ServerLevel level, FakePlayer player) {
        var soil = new net.minecraft.core.BlockPos(176, 120, 176);
        var crop = soil.above();
        level.setBlock(soil, net.minecraft.world.level.block.Blocks.GRASS_BLOCK.defaultBlockState(), 3);
        level.setBlock(crop, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
        player.setPos(soil.getX() + 0.5, crop.getY(), soil.getZ() + 0.5);
        ItemStack seed = new ItemStack(BuiltInRegistries.ITEM.getValue(id("kaleidoscope_cookery:tea_seed")));
        player.setItemSlot(EquipmentSlot.MAINHAND, seed);
        var hit = new net.minecraft.world.phys.BlockHitResult(
            net.minecraft.world.phys.Vec3.atCenterOf(soil), net.minecraft.core.Direction.UP, soil, false);
        var result = seed.getItem().useOn(new net.minecraft.world.item.context.UseOnContext(
            player, net.minecraft.world.InteractionHand.MAIN_HAND, hit));
        check(level.getBlockState(crop).is(BuiltInRegistries.BLOCK.getValue(id("kaleidoscope_cookery:tea_tree"))),
            "Tea seed did not plant on grass block; result=" + result);
        log("TEA_SEED planted tea tree directly on grass block");
    }

    private static void testGrassAndChestLoot(ServerLevel level, FakePlayer player) {
        for (String hat : new String[] {"none", "straw_hat", "straw_hat_flower"}) {
            player.setItemSlot(EquipmentSlot.HEAD, hat.equals("none") ? ItemStack.EMPTY :
                new ItemStack(BuiltInRegistries.ITEM.getValue(id("kaleidoscope_cookery:" + hat))));
            String[] expectedSeeds = {"tomato_seed", "chili_seed", "lettuce_seed", "wild_rice", "tea_seed"};
            Map<String, Integer> seeds = new TreeMap<>();
            for (String seed : expectedSeeds) seeds.put(seed, 0);
            for (int i = 0; i < 256; i++) {
                var drops = net.minecraft.world.level.block.Block.getDrops(net.minecraft.world.level.block.Blocks.SHORT_GRASS.defaultBlockState(),
                    level, new net.minecraft.core.BlockPos(0, 80, 0), null, player, ItemStack.EMPTY);
                for (var stack : drops) {
                    var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
                    if (itemId.getNamespace().equals("kaleidoscope_cookery") && seeds.containsKey(itemId.getPath())) {
                        seeds.merge(itemId.getPath(), stack.getCount(), Integer::sum);
                    }
                }
            }
            for (String seed : expectedSeeds) {
                check(hat.equals("none") ? seeds.get(seed) == 0 : seeds.get(seed) > 0,
                    "Grass seed condition " + hat + "/" + seed + "=" + seeds.get(seed));
            }
            log("GRASS " + hat + " 256 rolls seeds=" + seeds);
        }
        player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        var table = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE,
            id("kaleidoscope_cookery:chest/village_chest")));
        var params = new net.minecraft.world.level.storage.loot.LootParams.Builder(level)
            .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, player.position())
            .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CHEST);
        int papers = 0;
        for (int i = 0; i < 128; i++) {
            var loot = table.getRandomItems(params);
            check(!loot.isEmpty(), "Village chest table empty");
            for (var stack : loot) {
                if (stack.getItem() instanceof com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem) {
                    check(com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem.hasRecipe(stack), "Blank village recipe paper");
                    papers++;
                }
            }
        }
        check(papers > 0, "No recipe papers in village chest sample");
        log("CHEST 128 rolls recipe papers=" + papers);
    }

    private static int kill(ServerLevel level, FakePlayer player, String mob, ItemStack weapon, boolean environmental) {
        player.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        subject = (LivingEntity) BuiltInRegistries.ENTITY_TYPE.getValue(id("minecraft:" + mob)).create(level, EntitySpawnReason.COMMAND);
        check(subject != null, "Could not create " + mob);
        subject.setPos(1.5, 100, 1.5);
        level.addFreshEntity(subject);
        deathDrops.clear();
        var damage = environmental ? level.damageSources().generic() : level.damageSources().playerAttack(player);
        subject.hurtServer(level, damage, 1000);
        check(subject.isDeadOrDying(), "Subject survived test damage");
        int oil = deathDrops.stream().filter(s -> s.is(BuiltInRegistries.ITEM.getValue(id("kaleidoscope_cookery:oil")))).mapToInt(ItemStack::getCount).sum();
        if (mob.equals("pig") && !environmental) {
            check(deathDrops.stream().anyMatch(s -> s.is(Items.PORKCHOP)), "Vanilla pork drop missing");
        }
        subject.discard();
        subject = null;
        return oil;
    }

    private static void testAdvancement(ServerLevel level, FakePlayer player) {
        var advancement = level.getServer().getAdvancements().get(id("kaleidoscope_cookery:oil"));
        check(advancement != null, "Oil advancement missing");
        var trigger = (KilledTrigger.TriggerInstance) advancement.value().criteria().get("kill_pig").triggerInstance();
        var pig = BuiltInRegistries.ENTITY_TYPE.getValue(id("minecraft:pig")).create(level, EntitySpawnReason.COMMAND);
        var cow = BuiltInRegistries.ENTITY_TYPE.getValue(id("minecraft:cow")).create(level, EntitySpawnReason.COMMAND);
        player.setItemSlot(EquipmentSlot.MAINHAND, knife("iron"));
        check(trigger.matches(player, EntityPredicate.createContext(player, pig), level.damageSources().playerAttack(player)), "Knife kill should match advancement");
        check(!trigger.matches(player, EntityPredicate.createContext(player, cow), level.damageSources().playerAttack(player)), "Cow should not match advancement");
        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        check(!trigger.matches(player, EntityPredicate.createContext(player, pig), level.damageSources().playerAttack(player)), "Sword should not match advancement");
        log("ADVANCEMENT pig/knife accepted, cow/knife and pig/sword rejected");
    }

    private static net.minecraft.world.item.crafting.RecipeInput sampleInput(BaseRecipe<?> recipe) {
        return switch (recipe) {
            case PotRecipe r -> new SimpleInput(sampleStacks(r.getIngredients(), false));
            case FlexPotRecipe r -> new SimpleInput(sampleStacks(r.getIngredients(), true));
            case StockpotRecipe r -> new StockpotInput(sampleStacks(r.getIngredients(), false), r.soupBase());
            case FlexStockpotRecipe r -> new StockpotInput(sampleStacks(r.getIngredients(), true), r.soupBase());
            case MillstoneRecipe r -> new SimpleInput(sampleStacks(r.getIngredients(), false));
            case ChoppingBoardRecipe r -> new net.minecraft.world.item.crafting.SingleRecipeInput(BaseRecipe.displayStack(r.getIngredient()));
            case SteamerRecipe r -> new net.minecraft.world.item.crafting.SingleRecipeInput(BaseRecipe.displayStack(r.getIngredient()));
            case TeapotRecipe r -> new TeapotInput(BaseRecipe.displayStack(r.ingredient()).copyWithCount(r.ingredientCount()), r.teaFluid());
            case BambooTrayRecipe r -> new net.minecraft.world.item.crafting.SingleRecipeInput(BaseRecipe.displayStack(r.getIngredient()));
            default -> throw new AssertionError("Unhandled recipe type " + recipe.getClass());
        };
    }

    private static List<ItemStack> sampleStacks(List<net.minecraft.world.item.crafting.Ingredient> ingredients, boolean unique) {
        var result = new ArrayList<ItemStack>();
        check(selectSample(ingredients, 0, unique, result), "No valid representative ingredients");
        return result;
    }
    private static boolean selectSample(List<net.minecraft.world.item.crafting.Ingredient> ingredients, int index,
                                        boolean unique, List<ItemStack> result) {
        if (index == ingredients.size()) return true;
        for (var holder : ingredients.get(index).items().toList()) {
            if (unique && result.stream().anyMatch(stack -> stack.is(holder.value()))) continue;
            result.add(new ItemStack(holder.value()));
            if (selectSample(ingredients, index + 1, unique, result)) return true;
            result.removeLast();
        }
        return false;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void testRecipes(ServerLevel level) throws Exception {
        var manager = level.getServer().getRecipeManager();
        var expected = new String(CookeryRegression.class.getResourceAsStream("/expected-recipes.txt").readAllBytes()).lines().toList();
        for (String name : expected) {
            check(manager.byKey(ResourceKey.create(Registries.RECIPE, id(name))).isPresent(), "Recipe missing: " + name);
        }
        Map<String, Integer> counts = new TreeMap<>();
        int tested = 0;
        for (var holder : manager.getRecipes()) {
            Recipe recipe = holder.value();
            if (!(recipe instanceof BaseRecipe base)) continue;
            ItemStack result = base.getResultItem(level.registryAccess());
            check(!result.isEmpty(), "Empty recipe result: " + holder.id());
            var input = sampleInput(base);
            check(recipe.matches(input, level), "Recipe does not match valid sample: " + holder.id());
            var serializer = recipe.getSerializer();
            var ops = RegistryOps.create(JsonOps.INSTANCE, level.registryAccess());
            var json = serializer.codec().codec().encodeStart(ops, recipe).getOrThrow();
            BaseRecipe decodedJson = (BaseRecipe) serializer.codec().codec().parse(ops, json).getOrThrow();
            check(decodedJson.matches(input, level), "JSON recipe no longer matches: " + holder.id());
            check(json.equals(serializer.codec().codec().encodeStart(ops, decodedJson).getOrThrow()), "JSON fields changed: " + holder.id());
            check(ItemStack.matches(result, decodedJson.getResultItem(level.registryAccess())), "JSON result differs: " + holder.id());
            StreamCodec stream = serializer.streamCodec();
            var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
            try {
                stream.encode(buf, recipe);
                BaseRecipe decoded = (BaseRecipe) stream.decode(buf);
                check(decoded.matches(input, level), "Network recipe no longer matches: " + holder.id());
                check(json.equals(serializer.codec().codec().encodeStart(ops, decoded).getOrThrow()), "Network fields changed: " + holder.id());
                check(ItemStack.matches(result, decoded.getResultItem(level.registryAccess())), "Network result differs: " + holder.id());
                check(buf.readableBytes() == 0, "Unread recipe bytes: " + holder.id());
            } finally { buf.release(); }
            counts.merge(BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer).toString(), 1, Integer::sum);
            tested++;
        }
        log("RECIPES baseline=" + expected.size() + " all loaded; custom input matching + complete JSON/network round trips=" + tested + " " + counts);
    }
}
