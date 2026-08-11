package com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IChoppingBoard {
   boolean onPutItem(Level var1, LivingEntity var2, ItemStack var3);

   boolean onTakeOut(Level var1, LivingEntity var2);

   boolean onCutItem(Level var1, LivingEntity var2, ItemStack var3);

   void playParticlesSound();
}
