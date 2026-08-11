package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IPot {
   int PUT_INGREDIENT = 0;
   int COOKING = 1;
   int FINISHED = 2;
   int BURNT = 3;

   int getStatus();

   boolean hasHeatSource(Level var1);

   boolean onPlaceOil(Level var1, LivingEntity var2, ItemStack var3);

   boolean addIngredient(Level var1, LivingEntity var2, ItemStack var3);

   boolean removeIngredient(Level var1, LivingEntity var2);

   void onShovelHit(Level var1, LivingEntity var2, ItemStack var3);

   boolean takeOutProduct(Level var1, LivingEntity var2, ItemStack var3);
}
