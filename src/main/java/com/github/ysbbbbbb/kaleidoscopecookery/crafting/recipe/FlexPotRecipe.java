package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

public record FlexPotRecipe(int time, int stirFryCount, Ingredient carrier, NonNullList<Ingredient> ingredients, ItemStack result)
   implements BaseRecipe<SimpleInput> {
   public FlexPotRecipe(int time, int stirFryCount, Ingredient carrier, List<Ingredient> ingredients, ItemStack result) {
      this(time, stirFryCount, carrier, NonNullList.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT, BaseRecipe.fillInputs(ingredients)), result);
   }

   public boolean matches(SimpleInput simpleInput, Level level) {
      NonNullList<ItemStack> merged = NonNullList.create();
      Set<Item> record = Sets.newHashSet();
      int index = 0;

      for (int i = 0; i < simpleInput.size(); i++) {
         ItemStack stack = simpleInput.getItem(i);
         Item item = stack.getItem();
         if (!stack.isEmpty() && !record.contains(item)) {
            merged.add(stack);
            record.add(item);
            if (++index >= 9) {
               break;
            }
         }
      }

      return RecipeMatcher.findMatches(merged, this.ingredients) != null;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.ingredients;
   }

   public ItemStack getResultItem(Provider registryAccess) {
      return this.result;
   }

   public RecipeSerializer<FlexPotRecipe> getSerializer() {
      return (RecipeSerializer<FlexPotRecipe>)(RecipeSerializer<?>)ModRecipes.FLEX_POT_SERIALIZER.get();
   }

   public RecipeType<FlexPotRecipe> getType() {
      return ModRecipes.FLEX_POT_RECIPE;
   }
}
