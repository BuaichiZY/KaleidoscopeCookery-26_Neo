package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface PotRecipeSchema {
   RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
   RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asListOrSelf().inputKey("ingredients");
   RecipeKey<Ingredient> CARRIER = IngredientComponent.INGREDIENT.inputKey("carrier").optional(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT);
   RecipeKey<Integer> TIME = NumberComponent.INT.otherKey("time").optional(200);
   RecipeKey<Integer> STIR_FRY_COUNT = NumberComponent.INT.otherKey("stir_fry_count").optional(3);
   RecipeSchema SCHEMA = new RecipeSchema(new RecipeKey[]{OUTPUT, INGREDIENTS, CARRIER, TIME, STIR_FRY_COUNT});
}
