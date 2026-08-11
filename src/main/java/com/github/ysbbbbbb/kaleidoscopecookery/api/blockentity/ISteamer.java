package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISteamer {
   boolean hasHeatSource(Level var1);

   void updateLitLevel(Level var1);

   boolean takeFood(Level var1, LivingEntity var2);

   boolean placeFood(Level var1, LivingEntity var2, ItemStack var3);
}
