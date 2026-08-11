package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ITeapot {
   int PUT_INGREDIENT = 0;
   int PROCESSING = 1;
   int FINISHED = 2;

   int getStatus();

   boolean hasHeatSource(Level var1);

   boolean addTeaFluid(Level var1, LivingEntity var2, ItemStack var3);

   boolean removeTeaFluid(Level var1, LivingEntity var2, ItemStack var3);

   boolean addIngredient(Level var1, LivingEntity var2, ItemStack var3);

   boolean removeIngredient(Level var1, LivingEntity var2);

   boolean takeTeapot(Level var1, LivingEntity var2);
}
