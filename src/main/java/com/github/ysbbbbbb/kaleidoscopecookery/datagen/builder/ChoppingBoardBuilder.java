package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class ChoppingBoardBuilder implements RecipeBuilder {
   private static final String NAME = "chopping_board";
   private Ingredient ingredient = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
   private ItemStack result = ItemStack.EMPTY;
   private int cutCount = 3;
   private Identifier modelId;

   public static ChoppingBoardBuilder builder() {
      return new ChoppingBoardBuilder();
   }

   public ChoppingBoardBuilder setIngredient(ItemLike itemLike) {
      this.ingredient = Ingredient.of(new ItemLike[]{itemLike});
      return this;
   }

   public ChoppingBoardBuilder setIngredient(TagKey<Item> itemLike) {
      this.ingredient = Ingredient.of(itemLike);
      return this;
   }

   public ChoppingBoardBuilder setResult(ItemStack stack) {
      this.result = stack;
      return this;
   }

   public ChoppingBoardBuilder setResult(ItemLike itemLike) {
      this.result = new ItemStack(itemLike);
      return this;
   }

   public ChoppingBoardBuilder setResult(ItemLike itemLike, int count) {
      this.result = new ItemStack(itemLike, count);
      return this;
   }

   public ChoppingBoardBuilder setCutCount(int cutCount) {
      this.cutCount = Math.max(cutCount, 1);
      return this;
   }

   public ChoppingBoardBuilder setModelId(Identifier modelId) {
      this.modelId = modelId;
      return this;
   }

   public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
      return this;
   }

   public RecipeBuilder group(@Nullable String groupName) {
      return this;
   }

   public Item getResult() {
      return this.result.getItem();
   }

   public void save(RecipeOutput output) {
      String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "chopping_board/" + path);
      this.save(output, filePath);
   }

   public void save(RecipeOutput output, String recipeId) {
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "chopping_board/" + recipeId);
      this.save(output, filePath);
   }

   public void save(RecipeOutput recipeOutput, Identifier id) {
      ChoppingBoardRecipe recipe = new ChoppingBoardRecipe(this.ingredient, this.result, this.cutCount, this.modelId);
      recipeOutput.accept(id, recipe, null);
   }
}
