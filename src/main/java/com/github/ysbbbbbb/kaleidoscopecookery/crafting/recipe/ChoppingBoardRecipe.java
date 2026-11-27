package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.level.Level;
import java.util.List;

public class ChoppingBoardRecipe implements BaseRecipe<SingleRecipeInput> {
   private final Ingredient ingredient;
   private final List<ItemStackTemplate> resultTemplates;
   private final int cutCount;
   private final Identifier modelId;

   public ChoppingBoardRecipe(Ingredient ingredient, ItemStack result, int cutCount, Identifier modelId) {
      this(ingredient, List.of(ItemStackTemplate.fromNonEmptyStack(result)), cutCount, modelId);
   }

   public ChoppingBoardRecipe(Ingredient ingredient, ItemStackTemplate resultTemplate, int cutCount, Identifier modelId) {
      this(ingredient, List.of(resultTemplate), cutCount, modelId);
   }

   public ChoppingBoardRecipe(Ingredient ingredient, List<ItemStackTemplate> resultTemplates, int cutCount, Identifier modelId) {
      this.ingredient = ingredient;
      this.resultTemplates = List.copyOf(resultTemplates);
      this.cutCount = Math.max(cutCount, 1);
      this.modelId = modelId;
   }

   public boolean matches(SingleRecipeInput inv, Level level) {
      return this.ingredient.test(inv.getItem(0));
   }

   public boolean isSpecial() {
      return true;
   }

   public Ingredient getIngredient() {
      return this.ingredient;
   }

   public ItemStack getResult() {
      return this.resultTemplates.isEmpty() ? ItemStack.EMPTY : this.resultTemplates.getFirst().create();
   }

   public ItemStackTemplate getResultTemplate() {
      return this.resultTemplates.getFirst();
   }

   public List<ItemStackTemplate> getResultTemplates() {
      return this.resultTemplates;
   }

   public List<ItemStack> getResults() {
      return this.resultTemplates.stream().map(ItemStackTemplate::create).toList();
   }

   public ItemStack getResultItem(Provider registries) {
      return this.getResult();
   }

   @SuppressWarnings("unchecked")
   public RecipeSerializer<ChoppingBoardRecipe> getSerializer() {
      return (RecipeSerializer<ChoppingBoardRecipe>)(RecipeSerializer<?>)ModRecipes.CHOPPING_BOARD_SERIALIZER.get();
   }

   public net.minecraft.world.item.crafting.RecipeType<ChoppingBoardRecipe> getType() {
      return ModRecipes.CHOPPING_BOARD_RECIPE;
   }

   public int getCutCount() {
      return this.cutCount;
   }

   public Identifier getModelId() {
      return this.modelId;
   }
}
