package cookery.regression;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.KitchenShelfBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.common.util.FakePlayer;

final class Version170Regression {
    static void verify(ServerLevel level, FakePlayer player) {
        for (String mob : List.of("bee", "fox", "chicken", "enderman")) {
            var type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse("minecraft:" + mob));
            var entity = (net.minecraft.world.entity.Mob) type.create(level, net.minecraft.world.entity.EntitySpawnReason.COMMAND);
            check(entity != null && entity.getAttributes().hasAttribute(net.minecraft.world.entity.ai.attributes.Attributes.TEMPT_RANGE),
                mob + " has the required skewer temptation range");
        }
        var shelfPos = new BlockPos(2, 120, 8);
        var shelfState = ModBlocks.KITCHEN_SHELF.get().defaultBlockState();
        level.setBlock(shelfPos, Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(shelfPos, shelfState, 3);
        level.setBlock(shelfPos.east(), shelfState, 3);
        check(KitchenShelfBlock.getConnectedShelves(level, shelfPos).size() == 2, "connected shelves");
        var shelf = (KitchenShelfBlockEntity) level.getBlockEntity(shelfPos);
        for (int i = 0; i < 4; i++) {
            player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.APPLE, 64));
            check(shelf.interact(player, true), "shelf insert");
            check(player.getMainHandItem().isEmpty(), "shelf consumed input");
        }
        check(shelf.getStoredStack().getCount() == 256, "shelf capacity");
        var restored = new KitchenShelfBlockEntity(shelfPos, shelfState);
        restored.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(),
            shelf.saveWithoutMetadata(level.registryAccess())));
        check(restored.getStoredStack().getCount() == 256, "shelf large stack persisted");
        check(shelf.dispenseMatching(List.of(new ItemStack(Items.APPLE), new ItemStack(Items.APPLE))), "recipe-driven dispense");
        check(shelf.getStoredStack().getCount() == 255, "exactly one item per ingredient type");
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        check(shelf.interact(player, true) && shelf.getStoredStack().isEmpty(), "empty shelf with one bulk takeout");

        var recipeId = Identifier.parse("kaleidoscope_cookery:stockpot/sugar_syrup_apple_2");
        var holder = level.recipeAccess().byKey(ResourceKey.create(Registries.RECIPE, recipeId)).orElseThrow();
        check(holder.value() instanceof StockpotRecipe recipe && recipe.syrup() && recipe.ingredients().size() == 2, "batch syrup recipe");
        var potPos = shelfPos.south(3);
        var potState = ModBlocks.STOCKPOT.get().defaultBlockState();
        level.setBlock(potPos, Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(potPos, potState, 3);
        var pot = (StockpotBlockEntity) level.getBlockEntity(potPos);
        var cooked = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
        cooked.putInt("Status", 3);
        cooked.putInt("TakeoutCount", 2);
        cooked.putInt("SyrupUses", 2);
        cooked.putString("RecipeId", recipeId.toString());
        cooked.putString("SoupBaseId", "minecraft:water");
        cooked.store("Result", ItemStack.OPTIONAL_CODEC, new ItemStack(ModItems.CANDIED_APPLE_SKEWER.get()));
        pot.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), cooked.buildResult()));
        check(!pot.takeOutProduct(level, player, new ItemStack(Items.STICK)), "reject invalid syrup carrier");
        var skewers = new ItemStack(ModItems.APPLE_SKEWER.get(), 2);
        check(pot.takeOutProduct(level, player, skewers) && pot.getSyrupUses() == 1 && skewers.getCount() == 1, "first syrup use");
        check(pot.takeOutProduct(level, player, skewers) && pot.getSyrupUses() == 0 && skewers.isEmpty() && pot.getStatus() == 0, "last syrup use resets pot");
    }

    static void verifyComposting(ServerLevel level) {
        var pos = new BlockPos(2, 120, 13);
        var seedLayers = new net.minecraft.world.item.component.Compostable(ContextIntProviders.COMPOSTABLE_LOW);
        var produceLayers = new net.minecraft.world.item.component.Compostable(ContextIntProviders.COMPOSTABLE_MEDIUM);
        var caterpillarLayers = new net.minecraft.world.item.component.Compostable(ContextIntProviders.COMPOSTABLE_ALWAYS_ADD_ONE);
        for (var seed : List.of(ModItems.TOMATO_SEED, ModItems.CHILI_SEED, ModItems.LETTUCE_SEED,
            ModItems.WILD_RICE_SEED, ModItems.RICE_SEED, ModItems.TEA_SEED, ModItems.FRESH_TEA_LEAVES, ModItems.DRIED_TEA_LEAVES)) {
            verifyComposterInsertion(level, pos, seed.get(), seedLayers);
        }
        for (var produce : List.of(ModItems.TOMATO, ModItems.RED_CHILI, ModItems.GREEN_CHILI,
            ModItems.LETTUCE, ModItems.RICE_PANICLE)) {
            verifyComposterInsertion(level, pos, produce.get(), produceLayers);
        }
        verifyComposterInsertion(level, pos, ModItems.CATERPILLAR.get(), caterpillarLayers);

        var empty = Blocks.COMPOSTER.defaultBlockState();
        level.setBlock(pos, empty, 3);
        var excluded = new ItemStack(ModItems.KITCHEN_SHELF.get());
        var unchanged = ComposterBlock.insertItem(null, empty, level, excluded, pos);
        check(unchanged.getValue(ComposterBlock.LEVEL) == 0 && excluded.getCount() == 1,
            "composter rejects non-compostable shelf");
    }

    private static void verifyComposterInsertion(ServerLevel level, BlockPos pos, Item item,
        net.minecraft.world.item.component.Compostable expected) {
        var stack = new ItemStack(item, 2);
        check(expected.equals(stack.get(DataComponents.COMPOSTABLE)), item + " has the expected composting probability");
        var empty = Blocks.COMPOSTER.defaultBlockState();
        level.setBlock(pos, empty, 3);
        var filled = ComposterBlock.insertItem(null, empty, level, stack, pos);
        check(stack.getCount() == 1 && filled.getValue(ComposterBlock.LEVEL) == 1
            && level.getBlockState(pos).getValue(ComposterBlock.LEVEL) == 1,
            item + " must be consumed and fill one layer in an empty composter");
    }

    private static void check(boolean value, String description) {
        if (!value) throw new AssertionError("1.7.0: " + description);
    }
}
