package com.github.ysbbbbbb.kaleidoscopecookery.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public final class CarpetColor {
   public static Item getCarpetByColor(DyeColor color) {
      return Items.CARPET.pick(color);
   }

   @Nullable
   public static DyeColor getColorByCarpet(Item item) {
      for (DyeColor color : DyeColor.values()) {
         if (item == Items.CARPET.pick(color)) {
            return color;
         }
      }

      return null;
   }
}
