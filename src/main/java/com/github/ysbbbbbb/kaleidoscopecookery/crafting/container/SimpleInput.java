package com.github.ysbbbbbb.kaleidoscopecookery.crafting.container;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class SimpleInput implements RecipeInput {
   protected final List<ItemStack> inputs;

   public SimpleInput(List<ItemStack> inputs) {
      this.inputs = inputs;
   }

   public ItemStack getItem(int index) {
      return this.inputs.get(index);
   }

   public int size() {
      return this.inputs.size();
   }

   public List<ItemStack> getInputs() {
      return this.inputs;
   }
}
