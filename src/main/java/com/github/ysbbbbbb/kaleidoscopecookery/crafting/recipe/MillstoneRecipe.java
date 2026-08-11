package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.base.Preconditions;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record MillstoneRecipe(Ingredient ingredient, List<RandomOutput> results) implements BaseRecipe<SimpleInput> {
   public MillstoneRecipe(Ingredient ingredient, List<RandomOutput> results) {
      Preconditions.checkArgument(!results.isEmpty(), "Millstone recipe must have at least one output");
      Preconditions.checkArgument(results.size() <= 4, "Millstone recipe can have at most 4 outputs");
      this.ingredient = ingredient;
      this.results = results;
   }

   public boolean matches(SimpleInput input, Level level) {
      return this.ingredient.test(input.getItem(0));
   }

   public NonNullList<Ingredient> getIngredients() {
      NonNullList<Ingredient> ingredients = NonNullList.create();
      ingredients.add(this.ingredient);
      return ingredients;
   }

   public ItemStack getResultItem(Provider registryAccess) {
      return this.results.getFirst().stack();
   }

   public RecipeSerializer<MillstoneRecipe> getSerializer() {
      return (RecipeSerializer<MillstoneRecipe>)(RecipeSerializer<?>)ModRecipes.MILLSTONE_SERIALIZER.get();
   }

   public RecipeType<MillstoneRecipe> getType() {
      return ModRecipes.MILLSTONE_RECIPE;
   }
}
