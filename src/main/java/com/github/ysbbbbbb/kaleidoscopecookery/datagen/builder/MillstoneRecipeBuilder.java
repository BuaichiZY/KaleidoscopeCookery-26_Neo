package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import java.util.List;
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

public class MillstoneRecipeBuilder implements RecipeBuilder {
   private static final String NAME = "millstone";
   private Ingredient ingredient = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
   private ItemStack result = ItemStack.EMPTY;

   public static MillstoneRecipeBuilder builder() {
      return new MillstoneRecipeBuilder();
   }

   public MillstoneRecipeBuilder setIngredient(ItemLike itemLike) {
      this.ingredient = Ingredient.of(new ItemLike[]{itemLike});
      return this;
   }

   public MillstoneRecipeBuilder setIngredient(TagKey<Item> itemLike) {
      this.ingredient = Ingredient.of(itemLike);
      return this;
   }

   public MillstoneRecipeBuilder setResult(ItemStack stack) {
      this.result = stack;
      return this;
   }

   public MillstoneRecipeBuilder setResult(ItemLike itemLike) {
      this.result = new ItemStack(itemLike);
      return this;
   }

   public MillstoneRecipeBuilder setResult(ItemLike itemLike, int count) {
      this.result = new ItemStack(itemLike, count);
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
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone/" + path);
      this.save(output, filePath);
   }

   public void save(RecipeOutput output, String recipeId) {
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone/" + recipeId);
      this.save(output, filePath);
   }

   public void save(RecipeOutput recipeOutput, Identifier id) {
      RandomOutput output = new RandomOutput(this.result, 1.0F);
      MillstoneRecipe recipe = new MillstoneRecipe(this.ingredient, List.of(output));
      recipeOutput.accept(id, recipe, null);
   }
}
