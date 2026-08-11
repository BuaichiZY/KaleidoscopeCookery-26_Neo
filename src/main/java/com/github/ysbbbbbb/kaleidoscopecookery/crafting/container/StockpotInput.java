package com.github.ysbbbbbb.kaleidoscopecookery.crafting.container;

import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class StockpotInput extends SimpleInput {
   private final Identifier soupBase;

   public StockpotInput(List<ItemStack> items, Identifier soupBase) {
      super(items);
      this.soupBase = soupBase;
   }

   public Identifier getSoupBase() {
      return this.soupBase;
   }
}
