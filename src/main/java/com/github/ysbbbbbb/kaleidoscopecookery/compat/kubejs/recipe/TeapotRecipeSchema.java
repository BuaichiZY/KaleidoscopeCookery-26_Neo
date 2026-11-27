package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface TeapotRecipeSchema {
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<Ingredient> INGREDIENT = IngredientComponent.INGREDIENT.inputKey("ingredient");
    RecipeKey<String> TEA_FLUID = StringComponent.ID.inputKey("tea_fluid").optional("minecraft:empty");
    RecipeKey<Integer> INGREDIENT_COUNT = NumberComponent.INT.otherKey("ingredient_count").optional(12);
    RecipeKey<Integer> TIME = NumberComponent.INT.otherKey("time").optional(2400);
    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENT, TEA_FLUID, INGREDIENT_COUNT, TIME);
}
