package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public final class PlayerMainInvWrapper implements IItemHandlerModifiable {
   private final Inventory inventory;
   public PlayerMainInvWrapper(Inventory inventory) { this.inventory = inventory; }
   public int getSlots() { return Inventory.INVENTORY_SIZE; }
   public ItemStack getStackInSlot(int slot) { return this.inventory.getItem(slot); }
   public void setStackInSlot(int slot, ItemStack stack) { this.inventory.setItem(slot, stack); }
   public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      ItemStack existing = getStackInSlot(slot);
      if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(existing, stack)) return stack;
      int moved = Math.min(stack.getCount(), Math.max(0, stack.getMaxStackSize() - existing.getCount()));
      if (!simulate && moved > 0) setStackInSlot(slot, stack.copyWithCount(existing.getCount() + moved));
      return moved == stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - moved);
   }
   public ItemStack extractItem(int slot, int amount, boolean simulate) {
      ItemStack existing = getStackInSlot(slot);
      int moved = Math.min(amount, existing.getCount());
      if (moved <= 0) return ItemStack.EMPTY;
      ItemStack result = existing.copyWithCount(moved);
      if (!simulate) setStackInSlot(slot, moved == existing.getCount() ? ItemStack.EMPTY : existing.copyWithCount(existing.getCount() - moved));
      return result;
   }
   public int getSlotLimit(int slot) { return 64; }
   public boolean isItemValid(int slot, ItemStack stack) { return true; }
}
