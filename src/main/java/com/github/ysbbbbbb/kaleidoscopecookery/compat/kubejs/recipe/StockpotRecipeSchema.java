package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface StockpotRecipeSchema {
   RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
   RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asListOrSelf().inputKey("ingredients");
   RecipeKey<String> SOUP_BASE = StringComponent.ID.inputKey("soup_base").optional(StockpotRecipeSerializer.DEFAULT_SOUP_BASE.toString());
   RecipeKey<Integer> TIME = NumberComponent.INT.otherKey("time").optional(300);
   RecipeKey<Ingredient> CARRIER = IngredientComponent.INGREDIENT.inputKey("carrier").optional(StockpotRecipeSerializer.DEFAULT_CARRIER);
   RecipeKey<String> COOKING_TEXTURE = StringComponent.ID.otherKey("cooking_texture").optional(StockpotVisuals.DEFAULT_COOKING_TEXTURE.toString());
   RecipeKey<String> FINISHED_TEXTURE = StringComponent.ID.otherKey("finished_texture").optional(StockpotVisuals.DEFAULT_FINISHED_TEXTURE.toString());
   RecipeKey<Integer> COOKING_BUBBLE_COLOR = NumberComponent.INT.otherKey("cooking_bubble_color").optional(16772291);
   RecipeKey<Integer> FINISHED_BUBBLE_COLOR = NumberComponent.INT.otherKey("finished_bubble_color").optional(16034443);
   RecipeSchema SCHEMA = new RecipeSchema(
      new RecipeKey[]{OUTPUT, INGREDIENTS, SOUP_BASE, TIME, CARRIER, COOKING_TEXTURE, FINISHED_TEXTURE, COOKING_BUBBLE_COLOR, FINISHED_BUBBLE_COLOR}
   );
}
