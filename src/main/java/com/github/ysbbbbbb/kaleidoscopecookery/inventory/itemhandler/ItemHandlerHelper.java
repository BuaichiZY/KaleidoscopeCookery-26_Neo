package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Prediction;

public final class ItemHandlerHelper {
   private ItemHandlerHelper() {}
   public static ItemStack insertItem(IItemHandler target, ItemStack stack, boolean simulate) {
      for (int slot = 0; slot < target.getSlots() && !stack.isEmpty(); slot++) stack = target.insertItem(slot, stack, simulate);
      return stack;
   }
   public static ItemStack insertItemStacked(IItemHandler target, ItemStack stack, boolean simulate) {
      if (target == null || stack.isEmpty()) return stack;
      for (int slot = 0; slot < target.getSlots() && !stack.isEmpty(); slot++) {
         if (ItemStack.isSameItemSameComponents(target.getStackInSlot(slot), stack)) stack = target.insertItem(slot, stack, simulate);
      }
      for (int slot = 0; slot < target.getSlots() && !stack.isEmpty(); slot++) {
         if (target.getStackInSlot(slot).isEmpty()) stack = target.insertItem(slot, stack, simulate);
      }
      return stack;
   }
   public static void giveItemToPlayer(Player player, ItemStack stack) { giveItemToPlayer(player, stack, -1); }
   public static void giveItemToPlayer(Player player, ItemStack stack, int preferredSlot) {
      if (stack.isEmpty()) return;
      if (preferredSlot >= 0 && preferredSlot < player.getInventory().getContainerSize()) {
         ItemStack existing = player.getInventory().getItem(preferredSlot);
         if (existing.isEmpty()) {
            player.getInventory().setItem(preferredSlot, stack.copy());
            return;
         }
      }
      ItemStack remainder = stack.copy();
      player.getInventory().add(remainder);
      if (!remainder.isEmpty() && !player.level().isClientSide()) player.drop(remainder, false, Prediction.SERVER_ONLY);
   }
}
