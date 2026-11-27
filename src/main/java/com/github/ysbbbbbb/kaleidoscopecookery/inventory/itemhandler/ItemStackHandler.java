package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

/** Small mod-owned replacement for NeoForge's removed ItemStackHandler. */
public class ItemStackHandler implements IItemHandlerModifiable, ValueIOSerializable {
   protected NonNullList<ItemStack> stacks;

   public ItemStackHandler() { this(1); }
   public ItemStackHandler(int size) { this.stacks = NonNullList.withSize(size, ItemStack.EMPTY); }
   public ItemStackHandler(NonNullList<ItemStack> stacks) { this.stacks = stacks; }

   public void setSize(int size) { this.stacks = NonNullList.withSize(size, ItemStack.EMPTY); }
   public void setStackInSlot(int slot, ItemStack stack) {
      validateSlotIndex(slot);
      this.stacks.set(slot, stack);
      onContentsChanged(slot);
   }
   public int getSlots() { return this.stacks.size(); }
   public ItemStack getStackInSlot(int slot) { validateSlotIndex(slot); return this.stacks.get(slot); }
   public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (stack.isEmpty()) return ItemStack.EMPTY;
      validateSlotIndex(slot);
      if (!isItemValid(slot, stack)) return stack;
      ItemStack existing = this.stacks.get(slot);
      if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(stack, existing)) return stack;
      int room = getStackLimit(slot, stack) - existing.getCount();
      if (room <= 0) return stack;
      int moved = Math.min(room, stack.getCount());
      if (!simulate) {
         this.stacks.set(slot, stack.copyWithCount(existing.getCount() + moved));
         onContentsChanged(slot);
      }
      return moved == stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - moved);
   }
   public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (amount <= 0) return ItemStack.EMPTY;
      validateSlotIndex(slot);
      ItemStack existing = this.stacks.get(slot);
      if (existing.isEmpty()) return ItemStack.EMPTY;
      int moved = Math.min(amount, Math.min(existing.getCount(), existing.getMaxStackSize()));
      ItemStack result = existing.copyWithCount(moved);
      if (!simulate) {
         this.stacks.set(slot, moved == existing.getCount() ? ItemStack.EMPTY : existing.copyWithCount(existing.getCount() - moved));
         onContentsChanged(slot);
      }
      return result;
   }
   public int getSlotLimit(int slot) { return Item.ABSOLUTE_MAX_STACK_SIZE; }
   protected int getStackLimit(int slot, ItemStack stack) { return Math.min(getSlotLimit(slot), stack.getMaxStackSize()); }
   public boolean isItemValid(int slot, ItemStack stack) { return true; }

   public void serialize(ValueOutput output) {
      ValueOutput.TypedOutputList<ItemStackWithSlot> list = output.list("Items", ItemStackWithSlot.CODEC);
      for (int i = 0; i < this.stacks.size(); i++) if (!this.stacks.get(i).isEmpty()) list.add(new ItemStackWithSlot(i, this.stacks.get(i)));
      output.putInt("Size", this.stacks.size());
   }
   public void deserialize(ValueInput input) {
      setSize(input.getIntOr("Size", this.stacks.size()));
      input.listOrEmpty("Items", ItemStackWithSlot.CODEC).forEach(slot -> {
         if (slot.isValidInContainer(this.stacks.size())) this.stacks.set(slot.slot(), slot.stack());
      });
      onLoad();
   }
   protected void validateSlotIndex(int slot) {
      if (slot < 0 || slot >= this.stacks.size()) throw new IndexOutOfBoundsException("slot " + slot);
   }
   protected void onLoad() {}
   protected void onContentsChanged(int slot) {}
}
