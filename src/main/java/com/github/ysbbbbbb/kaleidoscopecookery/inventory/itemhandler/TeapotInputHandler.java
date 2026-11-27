package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import net.minecraft.world.item.ItemStack;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.IItemHandler;

@SuppressWarnings("removal")
public final class TeapotInputHandler implements IItemHandler {
   private final TeapotBlockEntity teapot;
   public TeapotInputHandler(TeapotBlockEntity teapot) { this.teapot = teapot; }
   public int getSlots() { return 1; }
   public ItemStack getStackInSlot(int slot) { return slot == 0 ? this.teapot.getInput() : ItemStack.EMPTY; }
   public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (slot != 0 || !this.teapot.canInsertIngredient(stack)) return stack;
      if (!simulate) this.teapot.insertIngredient(stack);
      ItemStack remainder = stack.copy();
      remainder.shrink(1);
      return remainder;
   }
   public ItemStack extractItem(int slot, int amount, boolean simulate) { return ItemStack.EMPTY; }
   public int getSlotLimit(int slot) { return 1; }
   public boolean isItemValid(int slot, ItemStack stack) { return slot == 0 && this.teapot.canInsertIngredient(stack); }
}
