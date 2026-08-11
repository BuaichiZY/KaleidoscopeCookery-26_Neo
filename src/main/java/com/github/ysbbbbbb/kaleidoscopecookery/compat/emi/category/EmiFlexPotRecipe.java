package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexPotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class EmiFlexPotRecipe extends BasicEmiRecipe {
   public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
      Identifier.parse(ModRecipes.FLEX_POT_RECIPE.toString()), EmiIngredient.of(Ingredient.of(new ItemLike[]{(ItemLike)ModItems.POT.get()}))
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/pot.png");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 102;
   private final int stirFryCount;

   public EmiFlexPotRecipe(Identifier id, List<EmiIngredient> inputs, List<EmiStack> outputs, List<EmiIngredient> catalysts, int stirFryCount) {
      super(CATEGORY, id, 176, 102);
      this.inputs = inputs;
      this.outputs = outputs;
      this.catalysts = catalysts;
      this.stirFryCount = stirFryCount;
   }

   public static void register(EmiRegistry registry) {
      registry.addCategory(CATEGORY);
      registry.addWorkstation(CATEGORY, EmiStack.of((ItemLike)ModItems.POT.get()));
      registry.addWorkstation(CATEGORY, EmiStack.of((ItemLike)ModItems.KITCHEN_SHOVEL.get()));
      registry.getRecipeManager().getAllRecipesFor(ModRecipes.FLEX_POT_RECIPE).forEach(recipeHolder -> {
         FlexPotRecipe r = (FlexPotRecipe)recipeHolder.value();
         List<EmiIngredient> inputs = r.getIngredients().stream().<EmiIngredient>map(EmiIngredient::of).toList();
         List<EmiStack> outputs = List.of(EmiStack.of(r.getResultItem(RegistryAccess.EMPTY)));
         List<EmiIngredient> catalysts = r.carrier().isEmpty() ? List.of() : List.of(EmiIngredient.of(r.carrier()));
         registry.addRecipe(new EmiFlexPotRecipe(recipeHolder.id(), inputs, outputs, catalysts, r.stirFryCount()));
      });
   }

   public void addWidgets(WidgetHolder widgets) {
      widgets.addTexture(BG, 1, 1, 176, 102, 0, 0);
      widgets.addText(Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), 88, 5, 5592405, false).horizontalAlign(Alignment.CENTER);
      widgets.addText(Component.translatable("jei.kaleidoscope_cookery.pot.stir_fry_count", new Object[]{this.stirFryCount}), 88, 85, 5592405, false)
         .horizontalAlign(Alignment.CENTER);

      for (int i = 0; i < this.inputs.size(); i++) {
         int xOffset = i % 3 * 18 + 15;
         int yOffset = i / 3 * 18 + 24;
         widgets.addSlot((EmiIngredient)this.inputs.get(i), xOffset, yOffset).drawBack(false);
         if (!((EmiIngredient)this.inputs.get(i)).isEmpty()) {
            widgets.addText(Component.literal("*"), xOffset, yOffset, 16777215, true);
         }
      }

      if (!this.catalysts.isEmpty()) {
         widgets.addSlot((EmiIngredient)this.catalysts.getFirst(), 133, 18).drawBack(false);
      }

      widgets.addSlot((EmiIngredient)this.outputs.getFirst(), 143, 60).drawBack(false).recipeContext(this);
   }
}
