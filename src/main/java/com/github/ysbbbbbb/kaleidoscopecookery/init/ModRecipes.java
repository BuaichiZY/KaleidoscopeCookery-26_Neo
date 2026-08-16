package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexPotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.SteamerRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.ChoppingBoardRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.FlexPotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.FlexStockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.MillstoneRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.PotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.RiceBowlRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.SteamerRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
   /** Minecraft 26 no longer exposes an empty Ingredient. A barrier is used
    * only as an internal sentinel and is never emitted into recipe inputs. */
   public static final Ingredient EMPTY_INGREDIENT = Ingredient.of(Items.BARRIER);

   /**
    * Tests for the internal empty-ingredient sentinel without relying on
    * object identity. Recipe codecs rebuild Ingredient instances, so a plain
    * equals check can leak the barrier sentinel into recipe viewers.
    */
   public static boolean isEmptyIngredient(Ingredient ingredient) {
      // Check the in-memory sentinel before touching ingredient contents.
      // Tag-backed ingredients cannot be inspected while a datapack reload is
      // still rebinding item tags.
      if (ingredient == null || ingredient == EMPTY_INGREDIENT || ingredient.equals(EMPTY_INGREDIENT)) {
         return true;
      }

      if (ingredient.isEmpty()) {
         return true;
      }

      var items = ingredient.items().iterator();
      if (!items.hasNext()) {
         return true;
      }

      boolean isBarrier = items.next().value() == Items.BARRIER;
      return isBarrier && !items.hasNext();
   }
   public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
      BuiltInRegistries.RECIPE_SERIALIZER, "kaleidoscope_cookery"
   );
   public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(
      BuiltInRegistries.RECIPE_TYPE, "kaleidoscope_cookery"
   );
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> POT_SERIALIZER = RECIPE_SERIALIZERS.register("pot", () -> new RecipeSerializer<>(PotRecipeSerializer.CODEC, PotRecipeSerializer.STREAM_CODEC));
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> FLEX_POT_SERIALIZER = RECIPE_SERIALIZERS.register(
      "flex_pot", () -> new RecipeSerializer<>(FlexPotRecipeSerializer.CODEC, FlexPotRecipeSerializer.STREAM_CODEC)
   );
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CHOPPING_BOARD_SERIALIZER = RECIPE_SERIALIZERS.register(
      "chopping_board", () -> new RecipeSerializer<>(ChoppingBoardRecipeSerializer.CODEC, ChoppingBoardRecipeSerializer.STREAM_CODEC)
   );
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> STOCKPOT_SERIALIZER = RECIPE_SERIALIZERS.register(
      "stockpot", () -> new RecipeSerializer<>(StockpotRecipeSerializer.CODEC, StockpotRecipeSerializer.STREAM_CODEC)
   );
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> FLEX_STOCKPOT_SERIALIZER = RECIPE_SERIALIZERS.register(
      "flex_stockpot", () -> new RecipeSerializer<>(FlexStockpotRecipeSerializer.CODEC, FlexStockpotRecipeSerializer.STREAM_CODEC)
   );
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> MILLSTONE_SERIALIZER = RECIPE_SERIALIZERS.register(
      "millstone", () -> new RecipeSerializer<>(MillstoneRecipeSerializer.CODEC, MillstoneRecipeSerializer.STREAM_CODEC)
   );
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> STEAMER_SERIALIZER = RECIPE_SERIALIZERS.register(
      "steamer", () -> new RecipeSerializer<>(SteamerRecipeSerializer.CODEC, SteamerRecipeSerializer.STREAM_CODEC)
   );
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> TEAPOT_SERIALIZER = RECIPE_SERIALIZERS.register("teapot", () -> new RecipeSerializer<>(TeapotRecipeSerializer.CODEC, TeapotRecipeSerializer.STREAM_CODEC));
   public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> RICE_BOWL_SERIALIZER = RECIPE_SERIALIZERS.register(
      "rice_bowl", () -> new RecipeSerializer<>(RiceBowlRecipeSerializer.CODEC, RiceBowlRecipeSerializer.STREAM_CODEC)
   );
   public static final RecipeType<PotRecipe> POT_RECIPE = recipeType("pot");
   public static final RecipeType<FlexPotRecipe> FLEX_POT_RECIPE = recipeType("flex_pot");
   public static final RecipeType<ChoppingBoardRecipe> CHOPPING_BOARD_RECIPE = recipeType("chopping_board");
   public static final RecipeType<StockpotRecipe> STOCKPOT_RECIPE = recipeType("stockpot");
   public static final RecipeType<FlexStockpotRecipe> FLEX_STOCKPOT_RECIPE = recipeType("flex_stockpot");
   public static final RecipeType<MillstoneRecipe> MILLSTONE_RECIPE = recipeType("millstone");
   public static final RecipeType<SteamerRecipe> STEAMER_RECIPE = recipeType("steamer");
   public static final RecipeType<TeapotRecipe> TEAPOT_RECIPE = recipeType("teapot");

   private static <T extends net.minecraft.world.item.crafting.Recipe<?>> RecipeType<T> recipeType(String name) {
      RecipeType<T> type = RecipeType.simple(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name));
      RECIPE_TYPES.register(name, () -> type);
      return type;
   }
}
