package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/** Bucket-backed soup base for bucket items that are not fluid bucket classes. */
public final class BucketSoupBase implements ISoupBase {
   private final Identifier name;
   private final Item filledBucket;
   private final int bubbleColor;

   public BucketSoupBase(Identifier name, Item filledBucket, int bubbleColor) {
      this.name = name;
      this.filledBucket = filledBucket;
      this.bubbleColor = bubbleColor;
   }

   public Identifier getName() { return this.name; }
   public int getBubbleColor() { return this.bubbleColor; }
   public ItemStack getDisplayStack() { return this.filledBucket.getDefaultInstance(); }
   public boolean isSoupBase(ItemStack stack) { return stack.is(this.filledBucket); }

   public ItemStack getReturnContainer(Level level, LivingEntity user, ItemStack soupBase) {
      level.playSound(null, user.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
      return new ItemStack(Items.BUCKET);
   }

   public boolean isContainer(ItemStack stack) { return stack.is(Items.BUCKET); }

   public ItemStack getReturnSoupBase(Level level, LivingEntity user, ItemStack container) {
      level.playSound(null, user.blockPosition(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
      return this.filledBucket.getDefaultInstance();
   }
}
