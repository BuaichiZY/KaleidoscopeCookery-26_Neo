package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;

public class MobSoupBase extends FluidSoupBase {
   public MobSoupBase(Identifier name, Item bucket, int bubbleColor) {
      super(name, bucket, bubbleColor);
      if (!(bucket instanceof MobBucketItem)) {
         throw new IllegalArgumentException("Mob bucket item must have a valid entity type!");
      }
   }

   public MobSoupBase(Identifier name, Item bucket) {
      this(name, bucket, 4159204);
   }
}
