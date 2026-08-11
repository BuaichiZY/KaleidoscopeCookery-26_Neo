package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.SteamerRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class EmiSteamerRecipe extends BasicEmiRecipe {
   public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
      Identifier.parse(ModRecipes.STEAMER_RECIPE.toString()), EmiIngredient.of(Ingredient.of(new ItemLike[]{(ItemLike)ModItems.STEAMER.get()}))
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/steamer.png");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 78;

   public EmiSteamerRecipe(Identifier id, List<EmiIngredient> inputs, List<EmiStack> outputs) {
      super(CATEGORY, id, 176, 78);
      this.inputs = inputs;
      this.outputs = outputs;
   }

   public static void register(EmiRegistry registry) {
      registry.addCategory(CATEGORY);
      registry.addWorkstation(CATEGORY, EmiStack.of((ItemLike)ModItems.STEAMER.get()));
      registry.addWorkstation(CATEGORY, EmiIngredient.of(TagMod.KITCHEN_KNIFE));
      registry.getRecipeManager().getAllRecipesFor(ModRecipes.STEAMER_RECIPE).forEach(holder -> {
         SteamerRecipe recipe = (SteamerRecipe)holder.value();
         List<EmiIngredient> inputs = recipe.getIngredients().stream().<EmiIngredient>map(EmiIngredient::of).toList();
         List<EmiStack> outputs = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
         registry.addRecipe(new EmiSteamerRecipe(holder.id(), inputs, outputs));
      });
   }

   public void addWidgets(WidgetHolder widgets) {
      widgets.addTexture(BG, 1, 1, 176, 78, 0, 0);
      widgets.addSlot((EmiIngredient)this.inputs.get(0), 38, 27).drawBack(false);
      widgets.addSlot((EmiIngredient)this.outputs.get(0), 124, 26).drawBack(false).recipeContext(this).large(true);
   }
}
