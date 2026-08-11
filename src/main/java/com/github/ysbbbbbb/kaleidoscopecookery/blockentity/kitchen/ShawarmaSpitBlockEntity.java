package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IShawarmaSpit;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ShawarmaSpitBlockEntity extends BaseBlockEntity implements IShawarmaSpit {
   private static final int MAX_ITEMS = 8;
   public static final String COOKING_ITEM = "CookingItem";
   public static final String COOKED_ITEM = "CookedItem";
   public static final String COOK_TIME = "CookTime";
   public static final String TOTAL_COOK_TIME = "TotalCookTime";
   private final CachedCheck<SingleRecipeInput, CampfireCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
   public ItemStack cookingItem = ItemStack.EMPTY;
   public ItemStack cookedItem = ItemStack.EMPTY;
   public int cookTime;
   public int totalCookTime;

   public ShawarmaSpitBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(ModBlocks.SHAWARMA_SPIT_BE.get(), pPos, pBlockState);
   }

   @Override
   public boolean onPutCookingItem(Level level, ItemStack itemStack) {
      if (this.cookingItem.isEmpty() && this.cookedItem.isEmpty()) {
         if (!(level instanceof ServerLevel serverLevel)) {
            return true;
         }

         SingleRecipeInput singleRecipeInput = new SingleRecipeInput(itemStack);
         return this.quickCheck
            .getRecipeFor(singleRecipeInput, serverLevel)
            .map(
               recipe -> {
                  this.cookingItem = itemStack.split(8);
                  this.cookedItem = ((CampfireCookingRecipe)recipe.value()).assemble(singleRecipeInput);
                  this.cookedItem.setCount(this.cookingItem.getCount());
                  this.totalCookTime = ((CampfireCookingRecipe)recipe.value()).cookingTime();
                  this.cookTime = this.totalCookTime;
                  this.refresh();
                  if (level instanceof ServerLevel) {
                     level.playSound(
                        null,
                        this.worldPosition.getX() + 0.5,
                        this.worldPosition.getY() + 0.5,
                        this.worldPosition.getZ() + 0.5,
                        SoundEvents.ITEM_FRAME_ADD_ITEM,
                        SoundSource.BLOCKS,
                        0.5F + level.getRandom().nextFloat(),
                        level.getRandom().nextFloat() * 0.7F + 0.6F
                     );
                  }

                  return true;
               }
            )
            .orElse(false);
      } else {
         return false;
      }
   }

   @Override
   public boolean onTakeCookedItem(Level level, LivingEntity entity) {
      ItemStack mainHandItem = entity.getMainHandItem();
      if (this.cookTime <= 0 && !this.cookedItem.isEmpty()) {
         this.giveItem(level, entity, mainHandItem, this.cookedItem.copy());
         return true;
      } else if (this.cookTime > 0 && !this.cookingItem.isEmpty()) {
         this.giveItem(level, entity, mainHandItem, this.cookingItem.copy());
         return true;
      } else {
         return false;
      }
   }

   private void giveItem(Level level, LivingEntity entity, ItemStack mainHandItem, ItemStack copy) {
      this.cookingItem = ItemStack.EMPTY;
      this.cookedItem = ItemStack.EMPTY;
      this.cookTime = 0;
      this.totalCookTime = 0;
      this.refresh();
      if ((Boolean)this.getBlockState().getValue(ShawarmaSpitBlock.POWERED)) {
         entity.hurt(level.damageSources().inFire(), 1.0F);
      }

      ItemUtils.getItemToLivingEntity(entity, copy);
      if (level instanceof ServerLevel) {
         level.playSound(
            null,
            this.worldPosition.getX() + 0.5,
            this.worldPosition.getY() + 0.5,
            this.worldPosition.getZ() + 0.5,
            SoundEvents.ITEM_FRAME_REMOVE_ITEM,
            SoundSource.BLOCKS,
            0.5F + level.getRandom().nextFloat(),
            level.getRandom().nextFloat() * 0.7F + 0.6F
         );
      }
   }

   public void tick() {
      if (this.cookingItem.isEmpty()) {
         if (!this.cookedItem.isEmpty()) {
            this.spawnParticles();
         }
      } else {
         this.spawnParticles();
         if (this.cookTime > 0) {
            this.cookTime--;
         } else {
            if (this.level instanceof ServerLevel) {
               this.level
                  .playSound(
                     null,
                     this.worldPosition.getX() + 0.5,
                     this.worldPosition.getY() + 0.5,
                     this.worldPosition.getZ() + 0.5,
                     SoundEvents.FIRE_EXTINGUISH,
                     SoundSource.BLOCKS,
                     0.5F + this.level.getRandom().nextFloat(),
                     this.level.getRandom().nextFloat() * 0.7F + 0.6F
                  );
            }

            this.cookingItem = ItemStack.EMPTY;
            this.refresh();
         }
      }
   }

   private void spawnParticles() {
      if (this.level instanceof ServerLevel serverLevel) {
         if (this.level.getRandom().nextFloat() < 0.25F) {
            serverLevel.sendParticles(
               (SimpleParticleType)ModParticles.COOKING.get(),
               this.worldPosition.getX() + 0.5,
               this.worldPosition.getY() + 0.5,
               this.worldPosition.getZ() + 0.5,
               1,
               0.25,
               0.2,
               0.25,
               0.1F
            );
         }

         if (this.level.getRandom().nextInt(20) == 0) {
            serverLevel.playSound(
               null,
               this.worldPosition.getX() + 0.5,
               this.worldPosition.getY() + 0.5,
               this.worldPosition.getZ() + 0.5,
               SoundEvents.CAMPFIRE_CRACKLE,
               SoundSource.BLOCKS,
               0.5F + this.level.getRandom().nextFloat(),
               this.level.getRandom().nextFloat() * 0.7F + 0.6F
            );
         }
      }
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.store("CookingItem", ItemStack.OPTIONAL_CODEC, this.cookingItem);
      tag.store("CookedItem", ItemStack.OPTIONAL_CODEC, this.cookedItem);
      tag.putInt("CookTime", this.cookTime);
      tag.putInt("TotalCookTime", this.totalCookTime);
   }

   protected void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      if (tag.keySet().contains("CookingItem")) {
         this.cookingItem = tag.read("CookingItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      }

      if (tag.keySet().contains("CookedItem")) {
         this.cookedItem = tag.read("CookedItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      }

      this.cookTime = tag.getIntOr("CookTime", 0);
      this.totalCookTime = tag.getIntOr("TotalCookTime", this.cookTime);
   }

   public float getCookProgress() {
      if (this.totalCookTime <= 0) {
         return this.cookingItem.isEmpty() && !this.cookedItem.isEmpty() ? 1.0F : 0.0F;
      }

      return Math.max(0.0F, Math.min(1.0F, (float)(this.totalCookTime - this.cookTime) / (float)this.totalCookTime));
   }
}
