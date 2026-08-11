package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum RecipeBlockComponentProvider implements IBlockComponentProvider {
   INSTANCE;

   public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
      if (accessor.getBlockEntity() instanceof RecipeBlockEntity recipeBlock) {
         ItemStack stackInSlot = recipeBlock.getItems().getStackInSlot(0);
         if (!stackInSlot.isEmpty()) {
            RecipeItem.RecipeRecord recipe = RecipeItem.getRecipe(stackInSlot);
            if (recipe != null) {
               ItemStack output = recipe.output();
               tooltip.add(output.getHoverName());
               boolean isFirst = true;

               for (ItemStack stack : recipe.input()) {
                  if (isFirst) {
                     tooltip.add(IElementHelper.get().item(stack));
                  } else {
                     tooltip.append(IElementHelper.get().item(stack));
                  }

                  isFirst = false;
               }

               tooltip.append(IElementHelper.get().progress(1.0F));
               tooltip.append(IElementHelper.get().item(output));
            }
         }
      }
   }

   public Identifier getUid() {
      return ModPlugin.RECIPE_BLOCK;
   }
}
