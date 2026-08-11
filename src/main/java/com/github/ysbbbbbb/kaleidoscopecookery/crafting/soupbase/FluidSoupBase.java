package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.SoundActions;

public class FluidSoupBase implements ISoupBase {
   protected final Identifier name;
   protected final Item bucketItem;
   protected final Fluid fluid;
   protected final int bubbleColor;

   public FluidSoupBase(Identifier name, Item bucketItem, int bubbleColor) {
      this.name = name;
      this.bucketItem = bucketItem;
      if (bucketItem instanceof BucketItem bucket) {
         this.fluid = bucket.content;
         this.bubbleColor = bubbleColor;
      } else {
         throw new IllegalArgumentException("Item must be a bucket item!");
      }
   }

   @Override
   public Identifier getName() {
      return this.name;
   }

   @Override
   public int getBubbleColor() {
      return this.bubbleColor;
   }

   @Override
   public ItemStack getDisplayStack() {
      return this.bucketItem.getDefaultInstance();
   }

   @Override
   public boolean isSoupBase(ItemStack stack) {
      return stack.is(this.bucketItem);
   }

   @Override
   public ItemStack getReturnContainer(Level level, LivingEntity user, ItemStack soupBase) {
      SoundEvent sound = this.fluid.getFluidType().getSound(user, SoundActions.BUCKET_EMPTY);
      if (sound != null) {
         Vec3 position = user.position();
         level.playSound(null, position.x(), position.y() + 0.5, position.z(), sound, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      return new ItemStack(Items.BUCKET);
   }

   @Override
   public boolean isContainer(ItemStack stack) {
      return stack.is(Items.BUCKET);
   }

   @Override
   public ItemStack getReturnSoupBase(Level level, LivingEntity user, ItemStack container) {
      SoundEvent sound = this.fluid.getFluidType().getSound(user, SoundActions.BUCKET_FILL);
      if (sound != null) {
         Vec3 position = user.position();
         level.playSound(null, position.x(), position.y() + 0.5, position.z(), sound, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      return this.bucketItem.getDefaultInstance();
   }

   public Fluid getFluid() {
      return this.fluid;
   }
}
