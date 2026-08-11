package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.RiceBowlRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ShapelessRecipeProvider extends ModRecipeProvider {
   public ShapelessRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
   }

   @Override
   public void buildRecipes(RecipeOutput consumer) {
      ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, (ItemLike)ModItems.RAW_ZONGZI.get(), 1)
         .requires((ItemLike)ModItems.RICE_SEED.get())
         .requires(Items.LILY_PAD)
         .unlockedBy("has_lily_pad", has(Items.LILY_PAD))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.RAW_BAMBOO_TUBE_RICE.get(), 1)
         .requires(Items.BAMBOO)
         .requires(TagCommon.GRAIN_RICE)
         .requires(TagCommon.RAW_MEATS)
         .unlockedBy("has_bamboo", has(Items.BAMBOO))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, (ItemLike)ModItems.RICE_PANICLE.get(), 9)
         .requires((ItemLike)ModItems.STRAW_BLOCK.get())
         .unlockedBy("has_rice_panicle", has((ItemLike)ModItems.RICE_PANICLE.get()))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, (ItemLike)ModItems.OIL.get(), 9)
         .requires((ItemLike)ModItems.OIL_BLOCK.get())
         .unlockedBy("has_ingot_iron", has(Items.IRON_INGOT))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.CHILI_SEED.get(), 1)
         .requires((ItemLike)ModItems.GREEN_CHILI.get())
         .unlockedBy("has_chili", has((ItemLike)ModItems.GREEN_CHILI.get()))
         .save(consumer, "chili_seed_from_green_chili");
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.CHILI_SEED.get(), 1)
         .requires((ItemLike)ModItems.RED_CHILI.get())
         .unlockedBy("has_chili", has((ItemLike)ModItems.RED_CHILI.get()))
         .save(consumer, "chili_seed_from_red_chili");
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.TOMATO_SEED.get(), 1)
         .requires((ItemLike)ModItems.TOMATO.get())
         .unlockedBy("has_tomato", has((ItemLike)ModItems.TOMATO.get()))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.STUFFED_DOUGH_FOOD.get(), 1)
         .requires(TagCommon.RAW_MEATS)
         .requires(TagCommon.VEGETABLES)
         .requires(TagCommon.DOUGH)
         .unlockedBy("has_dough", has(TagCommon.DOUGH))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike)ModItems.RECIPE_ITEM.get(), 1)
         .requires((ItemLike)ModItems.RECIPE_ITEM.get())
         .unlockedBy("has_recipe_item", has((ItemLike)ModItems.RECIPE_ITEM.get()))
         .save(consumer, "reset_recipe_item");
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.RAW_MEATBALL.get(), 1)
         .requires(TagCommon.RAW_MEATS)
         .requires(TagCommon.RAW_MEATS)
         .requires(TagCommon.VEGETABLES)
         .unlockedBy("has_raw_meats", has(TagCommon.RAW_MEATS))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.EMPTY_CUP.get(), 1)
         .requires(Items.FLOWER_POT)
         .unlockedBy("has_flower_pot", has(Items.FLOWER_POT))
         .save(consumer);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, PlateRegistry.getItem(PlateRegistry.BERRY_PLATTER), 1)
         .requires(Items.SWEET_BERRIES, 4)
         .requires(Items.GLOW_BERRIES, 4)
         .requires(Items.BOWL)
         .unlockedBy("has_berry_platter", has(Items.SWEET_BERRIES))
         .save(consumer, this.modLoc("berry_platter"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.GOLDEN_SALAD), 1)
         .requires(Items.GOLDEN_APPLE, 2)
         .requires(Items.GOLDEN_CARROT, 2)
         .requires(Items.GLISTERING_MELON_SLICE, 2)
         .requires(Items.BOWL)
         .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
         .save(consumer, this.modLoc("golden_salad"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.NETHER_STYLE_SASHIMI), 1)
         .requires(Items.CRIMSON_FUNGUS)
         .requires(Items.WARPED_FUNGUS)
         .requires((ItemLike)ModItems.SASHIMI.get(), 4)
         .requires(Items.BOWL)
         .unlockedBy("has_sashimi", has((ItemLike)ModItems.SASHIMI.get()))
         .save(consumer, this.modLoc("nether_style_sashimi"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.DESERT_STYLE_SASHIMI), 1)
         .requires(Items.CACTUS, 2)
         .requires((ItemLike)ModItems.SASHIMI.get(), 4)
         .requires(Items.BOWL)
         .unlockedBy("has_sashimi", has((ItemLike)ModItems.SASHIMI.get()))
         .save(consumer, this.modLoc("desert_style_sashimi"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.COLD_STYLE_SASHIMI), 1)
         .requires(Items.SNOWBALL, 3)
         .requires((ItemLike)ModItems.SASHIMI.get(), 4)
         .requires(Items.BOWL)
         .unlockedBy("has_sashimi", has((ItemLike)ModItems.SASHIMI.get()))
         .save(consumer, this.modLoc("cold_style_sashimi"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.END_STYLE_SASHIMI), 1)
         .requires(Items.CHORUS_FRUIT, 3)
         .requires((ItemLike)ModItems.SASHIMI.get(), 4)
         .requires(Items.BOWL)
         .unlockedBy("has_sashimi", has((ItemLike)ModItems.SASHIMI.get()))
         .save(consumer, this.modLoc("end_style_sashimi"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.TUNDRA_STYLE_SASHIMI), 1)
         .requires(Ingredient.of(ItemTags.FLOWERS), 2)
         .requires((ItemLike)ModItems.SASHIMI.get(), 4)
         .requires(Items.BOWL)
         .unlockedBy("has_sashimi", has((ItemLike)ModItems.SASHIMI.get()))
         .save(consumer, this.modLoc("tundra_style_sashimi"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FoodBiteRegistry.getItem(FoodBiteRegistry.COLD_ROASTED_MEAT), 1)
         .requires(Items.COOKED_BEEF, 3)
         .requires(Items.BOWL)
         .unlockedBy("has_cooked_beef", has(Items.COOKED_BEEF))
         .save(consumer, this.modLoc("cold_roasted_meat"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.COLD_CUT_HAM_SLICES.get(), 1)
         .requires((ItemLike)ModItems.COOKED_PORK_BELLY.get(), 8)
         .requires(Items.BOWL)
         .unlockedBy("has_cooked_pork_belly", has((ItemLike)ModItems.COOKED_PORK_BELLY.get()))
         .save(consumer, this.modLoc("cold_cut_ham_slices"));
      this.addRiceBowlRecipe(
         consumer,
         (ItemLike)ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get(),
         (Item)ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get(),
         "scramble_egg_with_tomatoes_rice_bowl"
      );
      this.addRiceBowlRecipe(consumer, (ItemLike)ModItems.BRAISED_BEEF.get(), (Item)ModItems.BRAISED_BEEF_RICE_BOWL.get(), "braised_beef_rice_bowl");
      this.addRiceBowlRecipe(
         consumer,
         (ItemLike)ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get(),
         (Item)ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get(),
         "stir_fried_pork_with_peppers_rice_bowl"
      );
      this.addRiceBowlRecipe(
         consumer, (ItemLike)ModItems.SWEET_AND_SOUR_PORK.get(), (Item)ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get(), "sweet_and_sour_pork_rice_bowl"
      );
      this.addRiceBowlRecipe(
         consumer,
         (ItemLike)ModItems.FISH_FLAVORED_SHREDDED_PORK.get(),
         (Item)ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get(),
         "fish_flavored_shredded_pork_rice_bowl"
      );

      for (int i = 0; i < 8; i++) {
         int count = i + 1;
         String name = "flour_from_" + count + "_wheat";
         ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)ModItems.RAW_DOUGH.get(), count)
            .requires(Items.WATER_BUCKET)
            .requires((ItemLike)ModItems.FLOUR.get(), count)
            .unlockedBy("has_wheat", has(Items.WHEAT))
            .save(consumer, name);
      }

      this.addPlateRecipe(consumer, (ItemLike)ModItems.SHENGJIAN_MANTOU.get(), PlateRegistry.SHENGJIAN_MANTOU_PLATE);
      this.addPlateRecipe(consumer, (ItemLike)ModItems.BAOZI.get(), PlateRegistry.BAOZI_PLATE);
      this.addPlateRecipe(consumer, (ItemLike)ModItems.QINGTUAN.get(), PlateRegistry.QINGTUAN_PLATE);
      this.addPlateRecipe(consumer, (ItemLike)ModItems.STICKY_CANDY.get(), PlateRegistry.STICKY_CANDY_PLATE);
      this.addPlateRecipe(consumer, (ItemLike)ModItems.STICKY_RICE_CAKE.get(), PlateRegistry.STICKY_RICE_CAKE_PLATE);
      this.addPlateRecipe(consumer, (ItemLike)ModItems.ZONGZI.get(), PlateRegistry.ZONGZI_PLATE);
      this.addPlateRecipe(consumer, (ItemLike)ModItems.TOMATO.get(), PlateRegistry.TOMATO_PLATTER);
      this.addPlateRecipe(consumer, Items.APPLE, PlateRegistry.APPLE_PLATTER);
      this.addPlateRecipe(consumer, Items.MELON_SLICE, PlateRegistry.WATERMELON_PLATTER);
      this.addPlateRecipe(consumer, Items.CHORUS_FRUIT, PlateRegistry.CHORUS_FRUIT_PLATTER);
   }

   private void addRiceBowlRecipe(RecipeOutput consumer, ItemLike dish, Item result, String id) {
      Ingredient ingredient = Ingredient.of(new ItemLike[]{dish});
      RiceBowlRecipe recipe = new RiceBowlRecipe(CraftingBookCategory.MISC, ingredient, result.getDefaultInstance());
      consumer.accept(this.modLoc(id), recipe, null);
   }

   private void addPlateRecipe(RecipeOutput consumer, ItemLike ingredient, Identifier result) {
      Item resultItem = PlateRegistry.getItem(result);
      int count = PlateRegistry.getCount(result);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, resultItem, 1)
         .requires(ingredient, count)
         .requires(Items.BOWL)
         .unlockedBy("has_ingredient", has(ingredient))
         .save(consumer);
   }
}
