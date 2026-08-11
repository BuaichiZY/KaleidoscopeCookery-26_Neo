package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.create.CreateCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

public class EmiMillstoneRecipe extends BasicEmiRecipe {
   public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
      Identifier.parse(ModRecipes.MILLSTONE_RECIPE.toString()), EmiIngredient.of(Ingredient.of(new ItemLike[]{(ItemLike)ModItems.MILLSTONE.get()}))
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/millstone.png");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 95;

   public EmiMillstoneRecipe(Identifier id, List<EmiIngredient> inputs, List<EmiStack> outputs) {
      super(CATEGORY, id, 176, 95);
      this.inputs = inputs;
      this.outputs = outputs;
   }

   public static void register(EmiRegistry registry) {
      registry.addCategory(CATEGORY);
      registry.addWorkstation(CATEGORY, EmiStack.of((ItemLike)ModItems.MILLSTONE.get()));
      List<RecipeHolder<MillstoneRecipe>> millstoneRecipes = Lists.newArrayList();
      millstoneRecipes.addAll(registry.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE));
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         CreateCompat.getTransformRecipeForSearch(level, millstoneRecipes);
      }

      millstoneRecipes.forEach(r -> {
         MillstoneRecipe value = (MillstoneRecipe)r.value();
         List<EmiIngredient> inputs = value.getIngredients().stream().<EmiIngredient>map(EmiIngredient::of).toList();
         List<EmiStack> outputs = Lists.newArrayList();
         value.results().stream().filter(output -> !output.isEmpty()).forEach(output -> {
            EmiStack emiStack = EmiStack.of(output.stack()).setChance(output.chance());
            outputs.add(emiStack);
         });
         registry.addRecipe(new EmiMillstoneRecipe(r.id(), inputs, outputs));
      });
   }

   public void addWidgets(WidgetHolder widgets) {
      widgets.addTexture(BG, 1, 1, 176, 95, 0, 0);
      widgets.addSlot((EmiIngredient)this.inputs.getFirst(), 69, 39).drawBack(true);
      widgets.addSlot((EmiIngredient)this.outputs.getFirst(), 146, 47).drawBack(true).large(true).recipeContext(this);
      if (this.outputs.size() > 1) {
         for (int i = 1; i < this.outputs.size(); i++) {
            int x = 174 + i * -20;
            int y = 26;
            widgets.addSlot((EmiIngredient)this.outputs.get(i), x, y).drawBack(true).recipeContext(this);
         }
      }

      if (!this.catalysts.isEmpty()) {
         widgets.addSlot((EmiIngredient)this.catalysts.getFirst(), 115, 36).drawBack(false).recipeContext(this);
      }
   }
}
