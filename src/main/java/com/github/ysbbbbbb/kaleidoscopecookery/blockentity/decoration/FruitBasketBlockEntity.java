package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FruitBasketBlockEntity extends BaseBlockEntity {
   public static final String ITEMS = "BasketItems";
   private final ItemStackHandler items = new ItemStackHandler(8);

   public FruitBasketBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(ModBlocks.FRUIT_BASKET_BE.get(), pPos, pBlockState);
   }

   public void putOn(ItemStack stack) {
      if (stack.getItem().canFitInsideContainerItems()) {
         ItemStack reminder = ItemHandlerHelper.insertItemStacked(this.items, stack.copy(), false);
         if (stack.getCount() != reminder.getCount()) {
            stack.shrink(stack.getCount() - reminder.getCount());
            if (this.level != null) {
               this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
            }

            this.refresh();
         }
      }
   }

   public void takeOut(Player player) {
      for (int i = 0; i < this.items.getSlots(); i++) {
         ItemStack stack = this.items.getStackInSlot(i);
         if (!stack.isEmpty()) {
            ItemStack extractItem = this.items.extractItem(i, this.items.getSlotLimit(i), false);
            ItemHandlerHelper.giveItemToPlayer(player, extractItem);
            if (this.level != null) {
               this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
            }

            this.refresh();
            return;
         }
      }
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.putChild("BasketItems", this.items);
   }

   protected void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
tag.readChild("BasketItems",       this.items);
   }

   public ItemStackHandler getItems() {
      return this.items;
   }

   public void setItems(ItemStackHandler items, RegistryAccess access) {
      for (int slot = 0; slot < this.items.getSlots(); slot++) {
         ItemStack stack = slot < items.getSlots() ? items.getStackInSlot(slot).copy() : ItemStack.EMPTY;
         this.items.setStackInSlot(slot, stack);
      }
      this.refresh();
   }
}
