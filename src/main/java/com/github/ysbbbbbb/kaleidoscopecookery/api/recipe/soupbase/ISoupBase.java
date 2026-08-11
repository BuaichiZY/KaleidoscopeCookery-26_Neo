package com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISoupBase {
   Identifier getName();

   int getBubbleColor();

   ItemStack getDisplayStack();

   boolean isSoupBase(ItemStack var1);

   ItemStack getReturnContainer(Level var1, LivingEntity var2, ItemStack var3);

   boolean isContainer(ItemStack var1);

   ItemStack getReturnSoupBase(Level var1, LivingEntity var2, ItemStack var3);
}
