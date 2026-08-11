package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.SteamerRecipe;
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

public class SteamerBuilder implements RecipeBuilder {
   private static final String NAME = "steamer";
   private Ingredient ingredient = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
   private ItemStack result = ItemStack.EMPTY;
   private int cookTick = 1200;

   public static SteamerBuilder builder() {
      return new SteamerBuilder();
   }

   public SteamerBuilder setIngredient(ItemLike itemLike) {
      this.ingredient = Ingredient.of(new ItemLike[]{itemLike});
      return this;
   }

   public SteamerBuilder setIngredient(TagKey<Item> itemLike) {
      this.ingredient = Ingredient.of(itemLike);
      return this;
   }

   public SteamerBuilder setResult(ItemStack stack) {
      this.result = stack;
      return this;
   }

   public SteamerBuilder setResult(ItemLike itemLike) {
      this.result = new ItemStack(itemLike);
      return this;
   }

   public SteamerBuilder setResult(ItemLike itemLike, int count) {
      this.result = new ItemStack(itemLike, count);
      return this;
   }

   public SteamerBuilder setCookTick(int cookTick) {
      this.cookTick = Math.max(cookTick, 1);
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
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "steamer/" + path);
      this.save(output, filePath);
   }

   public void save(RecipeOutput output, String recipeId) {
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "steamer/" + recipeId);
      this.save(output, filePath);
   }

   public void save(RecipeOutput recipeOutput, Identifier id) {
      SteamerRecipe recipe = new SteamerRecipe(this.ingredient, this.result, this.cookTick);
      recipeOutput.accept(id, recipe, null);
   }
}
