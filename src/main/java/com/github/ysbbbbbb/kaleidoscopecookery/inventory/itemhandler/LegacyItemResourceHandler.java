package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.world.item.ItemStack;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/** Transactional bridge between the legacy slot API and NeoForge 26.1 transfers. */
@SuppressWarnings("removal")
public final class LegacyItemResourceHandler implements ResourceHandler<ItemResource> {
   private final IItemHandler handler;
   private final BiConsumer<Integer, ItemStack> setter;
   private final List<SlotJournal> journals;

   public LegacyItemResourceHandler(IItemHandler handler, BiConsumer<Integer, ItemStack> setter) {
      this.handler = handler;
      this.setter = setter;
      this.journals = new ArrayList<>(handler.getSlots());
      for (int slot = 0; slot < handler.getSlots(); slot++) this.journals.add(new SlotJournal(slot));
   }

   public int size() { return this.handler.getSlots(); }
   public ItemResource getResource(int index) { return ItemResource.of(this.handler.getStackInSlot(index)); }
   public long getAmountAsLong(int index) { return this.handler.getStackInSlot(index).getCount(); }
   public long getCapacityAsLong(int index, ItemResource resource) {
      return resource.isEmpty() || isValid(index, resource) ? this.handler.getSlotLimit(index) : 0;
   }
   public boolean isValid(int index, ItemResource resource) {
      return !resource.isEmpty() && this.handler.isItemValid(index, resource.toStack());
   }

   public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
      TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
      int offered = Math.min(amount, this.handler.getSlotLimit(index));
      if (offered == 0) return 0;
      ItemStack stack = resource.toStack(offered);
      int simulated = offered - this.handler.insertItem(index, stack, true).getCount();
      if (simulated <= 0) return 0;
      this.journals.get(index).updateSnapshots(transaction);
      return offered - this.handler.insertItem(index, stack, false).getCount();
   }

   public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
      TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
      ItemStack stored = this.handler.getStackInSlot(index);
      if (!resource.matches(stored) || amount == 0) return 0;
      int simulated = this.handler.extractItem(index, amount, true).getCount();
      if (simulated <= 0) return 0;
      this.journals.get(index).updateSnapshots(transaction);
      return this.handler.extractItem(index, amount, false).getCount();
   }

   private final class SlotJournal extends SnapshotJournal<ItemStack> {
      private final int slot;
      private SlotJournal(int slot) { this.slot = slot; }
      protected ItemStack createSnapshot() { return handler.getStackInSlot(this.slot).copy(); }
      protected void revertToSnapshot(ItemStack snapshot) { setter.accept(this.slot, snapshot); }
   }
}
