package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class SidedInvWrapper implements IItemHandlerModifiable {
   private final WorldlyContainer inventory;
   private final @Nullable Direction side;
   public SidedInvWrapper(WorldlyContainer inventory, @Nullable Direction side) { this.inventory = inventory; this.side = side; }
   private int realSlot(int slot) {
      if (this.side == null) return slot < this.inventory.getContainerSize() ? slot : -1;
      int[] slots = this.inventory.getSlotsForFace(this.side);
      return slot >= 0 && slot < slots.length ? slots[slot] : -1;
   }
   public int getSlots() { return this.side == null ? this.inventory.getContainerSize() : this.inventory.getSlotsForFace(this.side).length; }
   public ItemStack getStackInSlot(int slot) { int real = realSlot(slot); return real < 0 ? ItemStack.EMPTY : this.inventory.getItem(real); }
   public void setStackInSlot(int slot, ItemStack stack) { int real = realSlot(slot); if (real >= 0) { this.inventory.setItem(real, stack); this.inventory.setChanged(); } }
   public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      int real = realSlot(slot);
      if (real < 0 || stack.isEmpty() || !this.inventory.canPlaceItem(real, stack) || !this.inventory.canPlaceItemThroughFace(real, stack, this.side)) return stack;
      ItemStack existing = this.inventory.getItem(real);
      if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(existing, stack)) return stack;
      int moved = Math.min(stack.getCount(), Math.max(0, Math.min(getSlotLimit(slot), stack.getMaxStackSize()) - existing.getCount()));
      if (moved == 0) return stack;
      if (!simulate) setStackInSlot(slot, stack.copyWithCount(existing.getCount() + moved));
      return moved == stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - moved);
   }
   public ItemStack extractItem(int slot, int amount, boolean simulate) {
      int real = realSlot(slot);
      if (real < 0 || amount <= 0) return ItemStack.EMPTY;
      ItemStack existing = this.inventory.getItem(real);
      if (existing.isEmpty() || this.side != null && !this.inventory.canTakeItemThroughFace(real, existing, this.side)) return ItemStack.EMPTY;
      int moved = Math.min(amount, existing.getCount());
      ItemStack result = existing.copyWithCount(moved);
      if (!simulate) setStackInSlot(slot, moved == existing.getCount() ? ItemStack.EMPTY : existing.copyWithCount(existing.getCount() - moved));
      return result;
   }
   public int getSlotLimit(int slot) { return this.inventory.getMaxStackSize(); }
   public boolean isItemValid(int slot, ItemStack stack) { int real = realSlot(slot); return real >= 0 && this.inventory.canPlaceItem(real, stack); }
}
