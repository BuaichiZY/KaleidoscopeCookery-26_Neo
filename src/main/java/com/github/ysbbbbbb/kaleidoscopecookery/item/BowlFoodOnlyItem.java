package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BowlFoodOnlyItem extends FoodWithEffectsItem implements IHasContainer {
   public BowlFoodOnlyItem(FoodProperties properties) {
      super(properties, Items.BOWL);
   }

   public BowlFoodOnlyItem(FoodProperties properties, Item craftingItem) {
      super(properties, craftingItem);
   }

   @Override
   public Item getContainerItem() {
      return Items.BOWL;
   }
}
