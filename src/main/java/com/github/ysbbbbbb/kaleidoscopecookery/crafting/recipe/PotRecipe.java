package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

public record PotRecipe(int time, int stirFryCount, Ingredient carrier, NonNullList<Ingredient> ingredients, ItemStackTemplate resultTemplate)
   implements BaseRecipe<SimpleInput> {
   public PotRecipe(int time, int stirFryCount, Ingredient carrier, List<Ingredient> ingredients, ItemStack result) {
      this(time, stirFryCount, carrier, NonNullList.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT, BaseRecipe.fillInputs(ingredients)), result);
   }

   public PotRecipe(int time, int stirFryCount, Ingredient carrier, List<Ingredient> ingredients, ItemStackTemplate resultTemplate) {
      this(
         time,
         stirFryCount,
         carrier,
         NonNullList.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT, BaseRecipe.fillInputs(ingredients)),
         resultTemplate
      );
   }

   public PotRecipe(int time, int stirFryCount, Ingredient carrier, NonNullList<Ingredient> ingredients, ItemStack result) {
      this(time, stirFryCount, carrier, ingredients, ItemStackTemplate.fromNonEmptyStack(result));
   }

   public ItemStack result() {
      return this.resultTemplate.create();
   }

   public boolean matches(SimpleInput simpleInput, Level level) {
      List<ItemStack> actualInputs = simpleInput.getInputs().stream().filter(stack -> !stack.isEmpty()).toList();
      return RecipeMatcher.findMatches(actualInputs, this.ingredients) != null;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.ingredients;
   }

   public ItemStack getResultItem(Provider registryAccess) {
      return this.resultTemplate.create();
   }

   public RecipeSerializer<PotRecipe> getSerializer() {
      return (RecipeSerializer<PotRecipe>)(RecipeSerializer<?>)ModRecipes.POT_SERIALIZER.get();
   }

   public RecipeType<PotRecipe> getType() {
      return ModRecipes.POT_RECIPE;
   }
}
