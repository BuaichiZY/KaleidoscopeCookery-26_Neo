package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public abstract class RecipeItemEvent extends Event {
   private final ItemStack stack;

   public RecipeItemEvent(ItemStack stack) {
      this.stack = stack;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public static class CheckItem extends RecipeItemEvent {
      private final Reference2IntMap<Item> supply;

      public CheckItem(ItemStack stack, Reference2IntMap<Item> supply) {
         super(stack);
         this.supply = supply;
      }

      public void addItem(Item item, int count) {
         this.supply.mergeInt(item, count, Integer::sum);
      }
   }

   public static class DeductItem extends RecipeItemEvent {
      private final Item needItem;
      private final int[] needCount;

      public DeductItem(ItemStack stack, Item needItem, int[] needCount) {
         super(stack);
         this.needItem = needItem;
         this.needCount = needCount;
      }

      public Item getNeedItem() {
         return this.needItem;
      }

      public int getNeedCount() {
         return this.needCount[0];
      }

      public void deduct(int count) {
         this.needCount[0] = this.needCount[0] - count;
         this.needCount[0] = Math.max(this.needCount[0], 0);
      }
   }
}
