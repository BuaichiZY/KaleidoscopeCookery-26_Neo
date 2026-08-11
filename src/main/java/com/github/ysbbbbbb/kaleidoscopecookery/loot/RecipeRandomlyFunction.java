package com.github.ysbbbbbb.kaleidoscopecookery.loot;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModLootModifier;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
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

   public LootItemFunctionType<RecipeRandomlyFunction> getType() {
      return ModLootModifier.RECIPE_RANDOMLY;
   }

   protected ItemStack run(ItemStack stack, LootContext context) {
      RandomSource randomsource = context.getRandom();
      if (!this.possibleRecipes.isEmpty()) {
         RecipeItem.RecipeRecord record = this.possibleRecipes.get(randomsource.nextInt(this.possibleRecipes.size()));
         RecipeItem.setRecipe(stack, record);
         return stack;
      } else {
         List<Identifier> keys = FoodBiteRegistry.FOOD_DATA_MAP.keySet().stream().toList();
         if (keys.isEmpty()) {
            return stack;
         } else {
            Identifier randomKey = keys.get(randomsource.nextInt(keys.size()));
            Item result = FoodBiteRegistry.getItem(randomKey);
            RegistryAccess registryAccess = context.getLevel().registryAccess();

            for (RecipeHolder<PotRecipe> recipeHolder : context.getLevel().recipeAccess().getAllRecipesFor(ModRecipes.POT_RECIPE)) {
               PotRecipe recipe = (PotRecipe)recipeHolder.value();
               ItemStack resultItem = recipe.getResultItem(registryAccess);
               if (resultItem.is(result)) {
                  List<ItemStack> inputs = recipe.getIngredients().stream().filter(i -> !i.isEmpty()).map(i -> i.getItems()[0]).toList();
                  RecipeItem.RecipeRecord record = new RecipeItem.RecipeRecord(inputs, resultItem, RecipeItem.POT, false);
                  RecipeItem.setRecipe(stack, record);
                  return stack;
               }
            }

            for (RecipeHolder<StockpotRecipe> recipeHolderx : context.getLevel().recipeAccess().getAllRecipesFor(ModRecipes.STOCKPOT_RECIPE)) {
               StockpotRecipe recipe = (StockpotRecipe)recipeHolderx.value();
               ItemStack resultItem = recipe.getResultItem(registryAccess);
               if (resultItem.is(result)) {
                  List<ItemStack> inputs = recipe.getIngredients().stream().filter(i -> !i.isEmpty()).map(i -> i.getItems()[0]).toList();
                  RecipeItem.RecipeRecord record = new RecipeItem.RecipeRecord(inputs, resultItem, RecipeItem.STOCKPOT, false);
                  RecipeItem.setRecipe(stack, record);
                  return stack;
               }
            }

            return stack;
         }
      }
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
