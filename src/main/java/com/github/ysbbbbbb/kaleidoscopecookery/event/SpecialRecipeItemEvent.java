package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.RecipeItemEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FruitBasketItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class SpecialRecipeItemEvent {
   @SubscribeEvent
   public static void onCheckItemEvent(RecipeItemEvent.CheckItem event) {
      ItemStack stack = event.getStack();
      if (stack.is((Item)ModItems.FRUIT_BASKET.get())) {
         ItemStackHandler items = FruitBasketItem.getItems(stack);
         addItems(event, items);
         FruitBasketItem.saveItems(stack, items);
      } else if (stack.is((Item)ModItems.TRANSMUTATION_LUNCH_BAG.get())) {
         ItemStackHandler items = TransmutationLunchBagItem.getItems(stack);
         addItems(event, items);
         TransmutationLunchBagItem.setItems(stack, items);
      } else {
         ResourceHandler<ItemResource> resourceHandler = stack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(stack));
         if (resourceHandler != null) {
            addItems(event, IItemHandler.of(resourceHandler));
         }
      }
   }

   @SubscribeEvent
   public static void onDeductItemEvent(RecipeItemEvent.DeductItem event) {
      ItemStack stack = event.getStack();
      if (stack.is((Item)ModItems.FRUIT_BASKET.get())) {
         ItemStackHandler items = FruitBasketItem.getItems(stack);
         deductItems(event, items);
         FruitBasketItem.saveItems(stack, items);
      } else if (stack.is((Item)ModItems.TRANSMUTATION_LUNCH_BAG.get())) {
         ItemStackHandler items = TransmutationLunchBagItem.getItems(stack);
         deductItems(event, items);
         TransmutationLunchBagItem.setItems(stack, items);
      } else {
         ResourceHandler<ItemResource> resourceHandler = stack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(stack));
         if (resourceHandler != null) {
            deductItems(event, IItemHandler.of(resourceHandler));
         }
      }
   }

   private static void addItems(RecipeItemEvent.CheckItem event, IItemHandler items) {
      for (int i = 0; i < items.getSlots(); i++) {
         ItemStack slotStack = items.getStackInSlot(i);
         if (!slotStack.isEmpty()) {
            event.addItem(slotStack.getItem(), slotStack.getCount());
         }
      }
   }

   private static void deductItems(RecipeItemEvent.DeductItem event, IItemHandler items) {
      Item needItem = event.getNeedItem();

      for (int i = 0; i < items.getSlots(); i++) {
         int needCount = event.getNeedCount();
         if (needCount <= 0) {
            return;
         }

         ItemStack slotStack = items.getStackInSlot(i);
         if (slotStack.is(needItem)) {
            ItemStack extractItem = items.extractItem(i, needCount, false);
            event.deduct(extractItem.getCount());
         }
      }
   }
}
