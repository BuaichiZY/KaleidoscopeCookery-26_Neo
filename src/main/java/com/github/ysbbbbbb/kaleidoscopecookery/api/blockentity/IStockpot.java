package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IStockpot {
   int PUT_SOUP_BASE = 0;
   int PUT_INGREDIENT = 1;
   int COOKING = 2;
   int FINISHED = 3;

   int getStatus();

   boolean hasHeatSource(Level var1);

   boolean hasLid();

   boolean onLitClick(Level var1, LivingEntity var2, ItemStack var3);

   boolean addSoupBase(Level var1, LivingEntity var2, ItemStack var3);

   boolean removeSoupBase(Level var1, LivingEntity var2, ItemStack var3);

   boolean addIngredient(Level var1, LivingEntity var2, ItemStack var3);

   boolean removeIngredient(Level var1, LivingEntity var2);

   boolean takeOutProduct(Level var1, LivingEntity var2, ItemStack var3);
}
