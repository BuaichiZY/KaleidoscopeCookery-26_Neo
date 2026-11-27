package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

/** Input-only automation view for the millstone. */
@SuppressWarnings("removal")
public final class MillstoneInputHandler implements IItemHandler {
   private final MillstoneBlockEntity millstone;

   public MillstoneInputHandler(MillstoneBlockEntity millstone) {
      this.millstone = millstone;
   }

   @Override
   public int getSlots() {
      return 1;
   }

   @Override
   public ItemStack getStackInSlot(int slot) {
      return slot == 0 ? this.millstone.getInput() : ItemStack.EMPTY;
   }

   @Override
   public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (slot != 0 || stack.isEmpty()) return stack;
      Level level = this.millstone.getLevel();
      if (level == null || !this.millstone.isOutputEmpty() || !this.millstone.getInput().isEmpty()) return stack;
      ItemStack offered = stack.copyWithCount(Math.min(stack.getCount(), MillstoneBlockEntity.MAX_INPUT_COUNT));
      if (this.millstone.matchRecipe(new SimpleInput(List.of(offered)), level).isEmpty()) return stack;
      ItemStack remainder = stack.copy();
      if (!simulate) return this.millstone.onPutItem(level, remainder) ? remainder : stack;
      remainder.shrink(offered.getCount());
      return remainder;
   }

   @Override
   public ItemStack extractItem(int slot, int amount, boolean simulate) {
      return ItemStack.EMPTY;
   }

   @Override
   public int getSlotLimit(int slot) {
      return slot == 0 ? MillstoneBlockEntity.MAX_INPUT_COUNT : 0;
   }

   @Override
   public boolean isItemValid(int slot, ItemStack stack) {
      Level level = this.millstone.getLevel();
      return slot == 0 && level != null && !stack.isEmpty()
         && this.millstone.matchRecipe(new SimpleInput(List.of(stack)), level).isPresent();
   }
}
