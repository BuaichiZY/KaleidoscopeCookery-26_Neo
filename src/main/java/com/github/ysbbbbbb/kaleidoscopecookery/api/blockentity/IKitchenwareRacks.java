package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IKitchenwareRacks {
   boolean onClick(LivingEntity var1, ItemStack var2, boolean var3);

   ItemStack getItemLeft();

   ItemStack getItemRight();
}
