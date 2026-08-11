package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;

public class FlourItem extends Item {
   public FlourItem() {
      super(ModRegistrationProperties.itemProperties());
   }

   public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
      if (entity.tickCount % 10 == 0 && entity.isInWater()) {
         ItemStack doughStack = new ItemStack((ItemLike)ModItems.RAW_DOUGH.get(), stack.getCount());
         entity.setItem(doughStack);
         return true;
      } else {
         return super.onEntityItemUpdate(stack, entity);
      }
   }
}
