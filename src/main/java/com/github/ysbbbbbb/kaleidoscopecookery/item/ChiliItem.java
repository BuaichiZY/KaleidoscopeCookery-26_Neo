package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class ChiliItem extends Item {
   private final int damage;

   public ChiliItem(int damage) {
      super(ModRegistrationProperties.itemProperties().food(ModFoods.CHILI, ModFoods.consumableFor(ModFoods.CHILI)));
      this.damage = damage;
   }

   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      entity.hurt(level.damageSources().magic(), this.damage);
      return super.finishUsingItem(stack, level, entity);
   }
}
