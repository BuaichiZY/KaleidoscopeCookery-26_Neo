package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BambooTubeRiceBlockItem extends BlockItem implements IHasContainer {
   public BambooTubeRiceBlockItem(Block block, FoodProperties properties) {
      super(block, ModRegistrationProperties.itemProperties().food(properties));
   }

   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      ItemStack itemStack = super.finishUsingItem(stack, level, entity);
      return this.returnContainerToEntity(itemStack, level, entity);
   }

   @Override
   public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      TooltipDisplay display,
      Consumer<Component> tooltip,
      TooltipFlag flag
   ) {
      tooltip.accept(Component.translatable("item_group.kaleidoscope_cookery.cookery_food.name").withStyle(ChatFormatting.BLUE));
      MutableComponent full = Component.translatable("tooltip.kaleidoscope_cookery.bamboo_tube_rice.maxim")
         .withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC});
      String text = full.getString();

      for (String line : text.split("\n")) {
         if (!line.isEmpty()) {
            tooltip.accept(Component.literal(line).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
         }
      }
   }

   @Override
   public Item getContainerItem() {
      return Items.BAMBOO;
   }
}
