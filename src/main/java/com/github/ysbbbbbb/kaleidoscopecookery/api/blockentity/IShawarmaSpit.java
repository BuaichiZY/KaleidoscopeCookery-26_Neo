package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IShawarmaSpit {
   boolean onPutCookingItem(Level var1, ItemStack var2);

   boolean onTakeCookedItem(Level var1, LivingEntity var2);
}
