package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

public record StockpotRecipe(
   NonNullList<Ingredient> ingredients, Identifier soupBase, ItemStack result, int time, Ingredient carrier, StockpotVisuals visuals
) implements BaseRecipe<StockpotInput> {
   public StockpotRecipe(List<Ingredient> ingredients, Identifier soupBase, ItemStack result, int time, Ingredient carrier, StockpotVisuals visuals) {
      this(NonNullList.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT, BaseRecipe.fillInputs(ingredients)), soupBase, result, time, carrier, visuals);
   }

   public StockpotRecipe(NonNullList<Ingredient> ingredients, ItemStack result, int time, ItemStack container) {
      this(ingredients, StockpotRecipeSerializer.DEFAULT_SOUP_BASE, result, time, Ingredient.of(container.getItem()), StockpotVisuals.DEFAULT);
   }

   public Identifier cookingTexture() {
      return this.visuals.cookingTexture();
   }

   public Identifier finishedTexture() {
      return this.visuals.finishedTexture();
   }

   public int cookingBubbleColor() {
      return this.visuals.cookingBubbleColor();
   }

   public int finishedBubbleColor() {
      return this.visuals.finishedBubbleColor();
   }

   public boolean matches(StockpotInput container, Level level) {
      if (!container.getSoupBase().equals(this.soupBase)) {
         return false;
      }

      List<ItemStack> actualInputs = container.getInputs().stream().filter(stack -> !stack.isEmpty()).toList();
      return RecipeMatcher.findMatches(actualInputs, this.ingredients) != null;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.ingredients;
   }

   public ItemStack getResultItem(Provider registryAccess) {
      return this.result;
   }

   public RecipeSerializer<StockpotRecipe> getSerializer() {
      return (RecipeSerializer<StockpotRecipe>)(RecipeSerializer<?>)ModRecipes.STOCKPOT_SERIALIZER.get();
   }

   public RecipeType<StockpotRecipe> getType() {
      return ModRecipes.STOCKPOT_RECIPE;
   }
}
