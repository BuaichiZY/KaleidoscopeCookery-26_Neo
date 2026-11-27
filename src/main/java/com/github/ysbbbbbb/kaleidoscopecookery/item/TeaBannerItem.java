package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TeaBannerItem extends BlockItem {
   public TeaBannerItem() {
      super(ModBlocks.TEA_BANNER.get(), ModRegistrationProperties.blockItemProperties().stacksTo(1));
   }

   @Override
   public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                               Consumer<Component> tooltip, TooltipFlag flag) {
      TypedEntityData<BlockEntityType<?>> data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
      CompoundTag tag = data == null ? null : data.copyTagWithoutId();
      DyeColor color = tag == null ? DyeColor.RED : TeaBannerBlockEntity.getColor(tag);
      ItemStack pattern = ItemStack.EMPTY;
      HolderLookup.Provider registries = context.registries();
      if (tag != null && registries != null) {
         pattern = TeaBannerBlockEntity.getPatternItem(tag, registries);
      }
      Component patternName = pattern.isEmpty()
         ? Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.pattern.tea")
         : pattern.getHoverName();
      tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.color",
         Component.translatable("color.minecraft." + color.getName())).withStyle(ChatFormatting.GRAY));
      tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.pattern", patternName).withStyle(ChatFormatting.GRAY));
   }
}
