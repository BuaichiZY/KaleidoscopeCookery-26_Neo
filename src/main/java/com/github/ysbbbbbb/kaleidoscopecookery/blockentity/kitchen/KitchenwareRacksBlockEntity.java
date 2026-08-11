package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IKitchenwareRacks;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags.Items;

public class KitchenwareRacksBlockEntity extends BaseBlockEntity implements IKitchenwareRacks {
   private static final String LEFT_ITEM = "LeftItem";
   private static final String RIGHT_ITEM = "RightItem";
   private ItemStack itemLeft = ItemStack.EMPTY;
   private ItemStack itemRight = ItemStack.EMPTY;

   public KitchenwareRacksBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(ModBlocks.KITCHENWARE_RACKS_BE.get(), pPos, pBlockState);
   }

   @Override
   public boolean onClick(LivingEntity user, ItemStack stack, boolean isLeft) {
      ItemStack stackInRacks = isLeft ? this.itemLeft : this.itemRight;
      if (stack.isEmpty() && !stackInRacks.isEmpty()) {
         ItemUtils.getItemToLivingEntity(user, stackInRacks);
         if (isLeft) {
            this.itemLeft = ItemStack.EMPTY;
         } else {
            this.itemRight = ItemStack.EMPTY;
         }

         user.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
         this.refresh();
         return true;
      } else if (stack.is(Items.TOOLS) && stackInRacks.isEmpty()) {
         if (isLeft) {
            this.itemLeft = stack.split(1);
         } else {
            this.itemRight = stack.split(1);
         }

         user.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
         this.refresh();
         return true;
      } else {
         return false;
      }
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.store("LeftItem", ItemStack.OPTIONAL_CODEC, this.itemLeft);
      tag.store("RightItem", ItemStack.OPTIONAL_CODEC, this.itemRight);
   }

   protected void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      this.itemLeft = tag.read("LeftItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      this.itemRight = tag.read("RightItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
   }

   @Override
   public ItemStack getItemLeft() {
      return this.itemLeft;
   }

   @Override
   public ItemStack getItemRight() {
      return this.itemRight;
   }

   public void setItemLeft(ItemStack itemLeft) {
      this.itemLeft = itemLeft;
   }

   public void setItemRight(ItemStack itemRight) {
      this.itemRight = itemRight;
   }
}
