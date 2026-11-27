package com.github.ysbbbbbb.kaleidoscopecookery.loot;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RecipeRandomlyFunction extends LootItemConditionalFunction {
   public static final Identifier ID = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "recipe_randomly");
   public static final MapCodec<RecipeRandomlyFunction> CODEC = RecordCodecBuilder.mapCodec(
      instance -> commonFields(instance)
         .and(RecipeItem.RecipeRecord.CODEC.listOf().optionalFieldOf("recipes", List.of()).forGetter(f -> f.possibleRecipes))
         .apply(instance, RecipeRandomlyFunction::new)
   );
   private final List<RecipeItem.RecipeRecord> possibleRecipes;

   protected RecipeRandomlyFunction(List<LootItemCondition> predicates, List<RecipeItem.RecipeRecord> possibleRecipes) {
      super(predicates);
      this.possibleRecipes = ImmutableList.copyOf(possibleRecipes);
   }

   public MapCodec<RecipeRandomlyFunction> codec() {
      return CODEC;
   }

   protected ItemStack run(ItemStack stack, LootContext context) {
      List<RecipeItem.RecipeRecord> candidates = this.possibleRecipes;
      if (candidates.isEmpty()) {
         // Choose from recipes that exist, rather than food items that may have no recipe.
         candidates = new java.util.ArrayList<>();
         var access = context.getLevel().registryAccess();
         var manager = context.getLevel().recipeAccess();
         for (var holder : com.github.ysbbbbbb.kaleidoscopecookery.util.RecipeLookup.byType(manager, ModRecipes.POT_RECIPE)) {
            var recipe = holder.value();
            var output = recipe.getResultItem(access);
            if (!output.isEmpty()) {
               var inputs = recipe.getIngredients().stream().filter(i -> !ModRecipes.isEmptyIngredient(i))
                  .map(com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BaseRecipe::displayStack).toList();
               candidates.add(new RecipeItem.RecipeRecord(inputs, output, RecipeItem.POT, false));
            }
         }
         for (var holder : com.github.ysbbbbbb.kaleidoscopecookery.util.RecipeLookup.byType(manager, ModRecipes.STOCKPOT_RECIPE)) {
            var recipe = holder.value();
            var output = recipe.getResultItem(access);
            if (!output.isEmpty()) {
               var inputs = recipe.getIngredients().stream().filter(i -> !ModRecipes.isEmptyIngredient(i))
                  .map(com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BaseRecipe::displayStack).toList();
               candidates.add(new RecipeItem.RecipeRecord(inputs, output, RecipeItem.STOCKPOT, false));
            }
         }
      }
      if (!candidates.isEmpty()) {
         RecipeItem.setRecipe(stack, candidates.get(context.getRandom().nextInt(candidates.size())));
      }
      return stack;
   }

   public static RecipeRandomlyFunction.Builder randomRecipe() {
      return new RecipeRandomlyFunction.Builder();
   }

   public static class Builder extends net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<RecipeRandomlyFunction.Builder> {
      private final List<RecipeItem.RecipeRecord> recipes = Lists.newArrayList();

      protected RecipeRandomlyFunction.Builder getThis() {
         return this;
      }

      public RecipeRandomlyFunction.Builder withRecord(RecipeItem.RecipeRecord record) {
         this.recipes.add(record);
         return this;
      }

      public RecipeRandomlyFunction.Builder pot(ItemLike output, ItemLike... input) {
         List<ItemStack> list = Arrays.stream(input).<ItemStack>map(ItemStack::new).toList();
         RecipeItem.RecipeRecord record = new RecipeItem.RecipeRecord(list, new ItemStack(output), RecipeItem.POT, false);
         return this.withRecord(record);
      }

      public RecipeRandomlyFunction.Builder stockpot(ItemLike output, ItemLike... input) {
         List<ItemStack> list = Arrays.stream(input).<ItemStack>map(ItemStack::new).toList();
         RecipeItem.RecipeRecord record = new RecipeItem.RecipeRecord(list, new ItemStack(output), RecipeItem.STOCKPOT, false);
         return this.withRecord(record);
      }

      public LootItemFunction build() {
         return new RecipeRandomlyFunction(this.getConditions(), this.recipes);
      }
   }
}
