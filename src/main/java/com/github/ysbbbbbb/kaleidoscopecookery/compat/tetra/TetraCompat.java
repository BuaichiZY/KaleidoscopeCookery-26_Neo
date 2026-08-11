package com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra;

import net.minecraft.world.item.ItemStack;

public class TetraCompat {
   public static final String TETRA_ID = "tetra";
   public static boolean IS_LOADED = false;

   public static void init() {
   }

   public static boolean isModularItem(ItemStack stack) {
      return false;
   }
}
