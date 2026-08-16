package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

/** Shows the recorded ingredients and result while Jade targets a recipe block. */
public enum RuntimeRecipeBlockComponentProvider implements IBlockComponentProvider {
   INSTANCE;

   @Override
   public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
      if (!(accessor.getBlockEntity() instanceof RecipeBlockEntity recipeBlock)) {
         return;
      }

      ItemStack recipeStack = recipeBlock.getItems().getStackInSlot(0);
      RecipeItem.RecipeRecord record = RecipeItem.getRecipe(recipeStack);
      if (record == null || record.output().isEmpty()) {
         return;
      }

      tooltip.add(record.output().getHoverName());
      boolean first = true;
      for (ItemStack ingredient : record.input()) {
         if (ingredient.isEmpty()) {
            continue;
         }
         if (first) {
            tooltip.add(JadeUI.item(ingredient));
            first = false;
         } else {
            tooltip.append(JadeUI.item(ingredient));
         }
      }
      if (first) {
         tooltip.add(JadeUI.progressArrow(1.0F));
      } else {
         tooltip.append(JadeUI.progressArrow(1.0F));
      }
      tooltip.append(JadeUI.item(record.output()));
   }

   @Override
   public Identifier getUid() {
      return RuntimeJadePlugin.RECIPE_BLOCK;
   }
}
