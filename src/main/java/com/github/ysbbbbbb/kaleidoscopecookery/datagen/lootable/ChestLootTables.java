package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.RecipeRandomlyFunction;
import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ChestLootTables implements LootTableSubProvider {
   public static final Identifier VILLAGE_CHEST = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "chest/village_chest");
   public static final Identifier VILLAGE_HIDE_CHEST = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "chest/village_hide_chest");

   public ChestLootTables(Provider registries) {
   }

   public void generate(BiConsumer<ResourceKey<LootTable>, Builder> output) {
      output.accept(
         ResourceKey.create(Registries.LOOT_TABLE, VILLAGE_CHEST),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .setRolls(UniformGenerator.between(3.0F, 8.0F))
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.TOMATO.get())
                        .setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.RED_CHILI.get())
                        .setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.OIL.get())
                        .setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.TOMATO_SEED.get())
                        .setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.CHILI_SEED.get())
                        .setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.LETTUCE_SEED.get())
                        .setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.WILD_RICE_SEED.get())
                        .setWeight(10)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.RICE_PANICLE.get())
                        .setWeight(8)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)ModItems.STRAW_BLOCK.get())
                        .setWeight(8)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                  )
                  .add(LootItem.lootTableItem((ItemLike)ModItems.RECIPE_ITEM.get()).setWeight(8).apply(RecipeRandomlyFunction.randomRecipe()))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.IRON_KITCHEN_KNIFE.get()).setWeight(5))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.STRAW_HAT.get()).setWeight(5))
            )
      );
      output.accept(
         ResourceKey.create(Registries.LOOT_TABLE, VILLAGE_HIDE_CHEST),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .setRolls(UniformGenerator.between(2.0F, 3.0F))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.SEAFOOD_MISO_SOUP.get()).setWeight(10))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.CHICKEN_AND_MUSHROOM_STEW.get()).setWeight(10))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.PORK_BONE_SOUP.get()).setWeight(10))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.BRAISED_BEEF_WITH_POTATOES.get()).setWeight(10))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.BEEF_MEATBALL_SOUP.get()).setWeight(10))
                  .add(LootItem.lootTableItem((ItemLike)ModItems.EGG_FRIED_RICE.get()).setWeight(10))
                  .add(LootItem.lootTableItem(FoodBiteRegistry.getItem(FoodBiteRegistry.SLIME_BALL_MEAL)).setWeight(10))
            )
      );
   }
}
