package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe.ChoppingBoardRecipeSchema;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe.MillstoneRecipeSchema;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe.PotRecipeSchema;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe.SteamerRecipeSchema;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe.StockpotRecipeSchema;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.ModKubeJSUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util.SimpleSoupBaseBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class ModKubeJSPlugin implements KubeJSPlugin {
   public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
      registry.namespace("kaleidoscope_cookery").register(ModRecipes.POT_SERIALIZER.getId().getPath(), PotRecipeSchema.SCHEMA);
      registry.namespace("kaleidoscope_cookery").register(ModRecipes.FLEX_POT_SERIALIZER.getId().getPath(), PotRecipeSchema.SCHEMA);
      registry.namespace("kaleidoscope_cookery").register(ModRecipes.CHOPPING_BOARD_SERIALIZER.getId().getPath(), ChoppingBoardRecipeSchema.SCHEMA);
      registry.namespace("kaleidoscope_cookery").register(ModRecipes.STOCKPOT_SERIALIZER.getId().getPath(), StockpotRecipeSchema.SCHEMA);
      registry.namespace("kaleidoscope_cookery").register(ModRecipes.FLEX_STOCKPOT_SERIALIZER.getId().getPath(), StockpotRecipeSchema.SCHEMA);
      registry.namespace("kaleidoscope_cookery").register(ModRecipes.MILLSTONE_SERIALIZER.getId().getPath(), MillstoneRecipeSchema.SCHEMA);
      registry.namespace("kaleidoscope_cookery").register(ModRecipes.STEAMER_SERIALIZER.getId().getPath(), SteamerRecipeSchema.SCHEMA);
   }

   public void registerBindings(BindingRegistry bindings) {
      bindings.add("KCookery", ModKubeJSUtil.class);
      bindings.add("KCookerySoupBase", SimpleSoupBaseBuilder.class);
   }
}
