package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.tooltip;

import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

/** Minecraft 26 render-state implementation of the recorded recipe tooltip. */
public final class RuntimeRecipeItemTooltip implements ClientTooltipComponent {
   private static final int TOOLTIP_TEXT_COLOR = 0xFFAAAAAA;

   private final RecipeItem.RecipeRecord recipeRecord;
   private final MutableComponent recipeTypeText;
   private final MutableComponent ingredientsText;
   private final MutableComponent outputText;

   public RuntimeRecipeItemTooltip(RecipeItemTooltip tooltip) {
      this.recipeRecord = tooltip.record();
      this.recipeTypeText = Component.translatable(
         this.recipeRecord.flexRecipe() ? "jei.kaleidoscope_cookery.flex_recipe" : "jei.kaleidoscope_cookery.strict_recipe"
      );
      Quality quality = tooltip.quality();
      if (quality != null) {
         this.recipeTypeText.append(CommonComponents.SPACE).append(quality.getTooltip());
      }
      this.ingredientsText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.ingredient");
      this.outputText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.output");
   }

   @Override
   public int getHeight(Font font) {
      return 40;
   }

   @Override
   public int getWidth(Font font) {
      int ingredientsSize = this.recipeRecord.input().size() * 12 + font.width(this.ingredientsText) + 2;
      int outputSize = font.width(this.outputText) + 20;
      return Math.max(font.width(this.recipeTypeText), Math.max(ingredientsSize, outputSize));
   }

   @Override
   public void extractImage(
      Font font,
      int x,
      int y,
      int tooltipWidth,
      int tooltipHeight,
      GuiGraphicsExtractor graphics
   ) {
      graphics.text(font, this.recipeTypeText, x, y + 4, TOOLTIP_TEXT_COLOR);

      int ingredientsY = y + 12;
      graphics.text(font, this.ingredientsText, x, ingredientsY + 4, TOOLTIP_TEXT_COLOR);
      int ingredientsWidth = font.width(this.ingredientsText);
      int index = 0;
      for (ItemStack stack : this.recipeRecord.input()) {
         graphics.fakeItem(stack, x + ingredientsWidth + index * 12, ingredientsY);
         index++;
      }

      int outputY = y + 24;
      graphics.text(font, this.outputText, x, outputY + 4, TOOLTIP_TEXT_COLOR);
      ItemStack output = this.recipeRecord.output();
      int outputX = x + font.width(this.outputText);
      graphics.fakeItem(output, outputX, outputY);
      graphics.itemDecorations(font, output, outputX, outputY);
   }
}
