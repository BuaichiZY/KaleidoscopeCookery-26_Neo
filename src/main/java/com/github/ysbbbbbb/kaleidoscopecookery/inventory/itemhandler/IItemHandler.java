package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/** Mod-owned slot inventory contract used internally after NeoForge removed the legacy API. */
public interface IItemHandler {
   int getSlots();
   ItemStack getStackInSlot(int slot);
   ItemStack insertItem(int slot, ItemStack stack, boolean simulate);
   ItemStack extractItem(int slot, int amount, boolean simulate);
   int getSlotLimit(int slot);
   boolean isItemValid(int slot, ItemStack stack);

   static IItemHandler of(ResourceHandler<ItemResource> handler) {
      Objects.requireNonNull(handler);
      return new IItemHandler() {
         public int getSlots() { return handler.size(); }
         public ItemStack getStackInSlot(int slot) { return ItemUtil.getStack(handler, slot); }
         public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return ItemUtil.insertItemReturnRemaining(handler, slot, stack, simulate, null);
         }
         public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount <= 0) return ItemStack.EMPTY;
            ItemResource resource = handler.getResource(slot);
            if (resource.isEmpty()) return ItemStack.EMPTY;
            try (Transaction tx = Transaction.openRoot()) {
               int extracted = handler.extract(slot, resource, Math.min(amount, resource.getMaxStackSize()), tx);
               if (!simulate) tx.commit();
               return resource.toStack(extracted);
            }
         }
         public int getSlotLimit(int slot) { return handler.getCapacityAsInt(slot, ItemResource.EMPTY); }
         public boolean isItemValid(int slot, ItemStack stack) { return handler.isValid(slot, ItemResource.of(stack)); }
      };
   }
}
