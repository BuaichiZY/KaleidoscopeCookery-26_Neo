package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import net.minecraft.world.item.ItemStack;

public interface IItemHandlerModifiable extends IItemHandler {
   void setStackInSlot(int slot, ItemStack stack);
}
