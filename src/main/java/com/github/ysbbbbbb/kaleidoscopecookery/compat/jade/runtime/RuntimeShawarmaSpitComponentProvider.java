package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.JadeUI;

public enum RuntimeShawarmaSpitComponentProvider implements IBlockComponentProvider {
   INSTANCE;

   @Override
   public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
      if (!(accessor.getBlockEntity() instanceof ShawarmaSpitBlockEntity first)) {
         return;
      }

      Level level = accessor.getLevel();
      BlockPos pos = accessor.getPosition();
      DoubleBlockHalf half = accessor.getBlockState().getValue(ShawarmaSpitBlock.HALF);
      if (half == DoubleBlockHalf.LOWER && level.getBlockEntity(pos.above()) instanceof ShawarmaSpitBlockEntity second) {
         this.addItemInfo(tooltip, second);
         this.addItemInfo(tooltip, first);
      } else if (half == DoubleBlockHalf.UPPER && level.getBlockEntity(pos.below()) instanceof ShawarmaSpitBlockEntity second) {
         this.addItemInfo(tooltip, first);
         this.addItemInfo(tooltip, second);
      }
   }

   private void addItemInfo(ITooltip tooltip, ShawarmaSpitBlockEntity shawarmaSpit) {
      ItemStack cookingItem = shawarmaSpit.cookingItem;
      ItemStack cookedItem = shawarmaSpit.cookedItem;
      if (!cookingItem.isEmpty()) {
         tooltip.add(JadeUI.item(cookingItem));
         tooltip.append(JadeUI.progressArrow(shawarmaSpit.getCookProgress()));
         if (!cookedItem.isEmpty()) {
            tooltip.append(JadeUI.item(cookedItem));
         }
         if (shawarmaSpit.cookTime > 0) {
            tooltip.append(JadeUI.text(IThemeHelper.get().seconds(shawarmaSpit.cookTime, 20.0F).withStyle(ChatFormatting.GRAY)));
         }
      } else if (!cookedItem.isEmpty()) {
         tooltip.add(JadeUI.item(cookedItem));
      }
   }

   @Override
   public Identifier getUid() {
      return RuntimeJadePlugin.SHAWARMA_SPIT;
   }
}
