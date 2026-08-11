package com.github.ysbbbbbb.kaleidoscopecookery.client.tooltip;

import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ClientRecipeItemTooltip implements ClientTooltipComponent {
   private final RecipeItem.RecipeRecord recipeRecord;
   private final MutableComponent recipeTypeText;
   private final MutableComponent ingredientsText;
   private final MutableComponent outputText;

   public ClientRecipeItemTooltip(RecipeItemTooltip containerTooltip) {
      this.recipeRecord = containerTooltip.record();
      this.recipeTypeText = Component.translatable(
         this.recipeRecord.flexRecipe() ? "jei.kaleidoscope_cookery.flex_recipe" : "jei.kaleidoscope_cookery.strict_recipe"
      );
      Quality quality = containerTooltip.quality();
      if (quality != null) {
         this.recipeTypeText.append(CommonComponents.SPACE).append(quality.getTooltip());
      }

      this.ingredientsText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.ingredient");
      this.outputText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.output");
   }

   public int getHeight() {
      return 40;
   }

   public int getWidth(Font font) {
      int ingredientsSize = this.recipeRecord.input().size() * 12 + font.width(this.ingredientsText) + 2;
      int outputSize = font.width(this.outputText) + 20;
      return Math.max(font.width(this.recipeTypeText), Math.max(ingredientsSize, outputSize));
   }

   public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
      int ingredientsWidth = font.width(this.ingredientsText);
      int outputWidth = font.width(this.outputText);
      guiGraphics.drawString(font, this.recipeTypeText, pX, pY + 4, ChatFormatting.GRAY.getColor());
      int ingredientsYOffset = pY + 12;
      guiGraphics.drawString(font, this.ingredientsText, pX, ingredientsYOffset + 4, ChatFormatting.GRAY.getColor());
      int i = 0;

      for (ItemStack stack : this.recipeRecord.input()) {
         int xOffset = pX + ingredientsWidth + i * 12;
         guiGraphics.renderFakeItem(stack, xOffset, ingredientsYOffset);
         i++;
      }

      int xOffset = pX + outputWidth;
      int yOffset = pY + 24;
      guiGraphics.drawString(font, this.outputText, pX, yOffset + 4, ChatFormatting.GRAY.getColor());
      ItemStack stack = this.recipeRecord.output();
      guiGraphics.renderFakeItem(stack, xOffset, yOffset);
      guiGraphics.renderItemDecorations(font, stack, xOffset, yOffset);
   }
}
