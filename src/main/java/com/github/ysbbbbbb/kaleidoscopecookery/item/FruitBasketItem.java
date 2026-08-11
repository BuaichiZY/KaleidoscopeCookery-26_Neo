package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FruitBasketItem extends BlockItem {
   private static final int MAX_SLOTS = 8;

   public FruitBasketItem() {
      super((Block)ModBlocks.FRUIT_BASKET.get(), ModRegistrationProperties.itemProperties().stacksTo(1));
   }

   public static ItemStackHandler getItems(ItemStack stack) {
      FruitBasketItem.ItemContainer container = (FruitBasketItem.ItemContainer)stack.get(ModDataComponents.FRUIT_BASKET_ITEMS);
      return container != null ? container.items() : new ItemStackHandler(8);
   }

   public static void saveItems(ItemStack stack, ItemStackHandler items) {
      stack.set(ModDataComponents.FRUIT_BASKET_ITEMS, new FruitBasketItem.ItemContainer(items));
   }

   public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
      FruitBasketItem.ItemContainer container = (FruitBasketItem.ItemContainer)stack.get(ModDataComponents.FRUIT_BASKET_ITEMS);
      return container != null ? Optional.of(new ItemContainerTooltip(container.items())) : Optional.empty();
   }

   public boolean canFitInsideContainerItems() {
      return false;
   }

   public record ItemContainer(ItemStackHandler items) {
      public static final Codec<FruitBasketItem.ItemContainer> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(list -> {
         ItemStackHandler handler = new ItemStackHandler(8);

         for (int i = 0; i < Math.min(list.size(), handler.getSlots()); i++) {
            handler.setStackInSlot(i, (ItemStack)list.get(i));
         }

         return new FruitBasketItem.ItemContainer(handler);
      }, container -> {
         ItemStackHandler handler = container.items();
         List<ItemStack> output = Lists.newArrayList();

         for (int i = 0; i < handler.getSlots(); i++) {
            output.add(handler.getStackInSlot(i));
         }

         return output;
      });
      public static final StreamCodec<RegistryFriendlyByteBuf, FruitBasketItem.ItemContainer> STREAM_CODEC =
         ItemStack.OPTIONAL_LIST_STREAM_CODEC.map(FruitBasketItem.ItemContainer::fromList, FruitBasketItem.ItemContainer::toList);

      private static FruitBasketItem.ItemContainer fromList(List<ItemStack> list) {
         ItemStackHandler handler = new ItemStackHandler(8);
         for (int i = 0; i < Math.min(list.size(), handler.getSlots()); i++) {
            handler.setStackInSlot(i, list.get(i));
         }
         return new FruitBasketItem.ItemContainer(handler);
      }

      private List<ItemStack> toList() {
         List<ItemStack> output = Lists.newArrayList();
         for (int i = 0; i < this.items.getSlots(); i++) {
            output.add(this.items.getStackInSlot(i));
         }
         return output;
      }
   }
}
