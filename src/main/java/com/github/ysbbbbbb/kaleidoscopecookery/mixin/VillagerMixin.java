package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public class VillagerMixin {
   @Unique
   private static Set<Item> MOD_WANTED_ITEMS = null;

   @Inject(method = "wantsToPickUp(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
   public void onVillagerWantsToPickUp(ServerLevel level, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
      if (MOD_WANTED_ITEMS == null) {
         MOD_WANTED_ITEMS = ImmutableSet.of(
            (Item)ModItems.TOMATO.get(),
            (Item)ModItems.TOMATO_SEED.get(),
            (Item)ModItems.RED_CHILI.get(),
            (Item)ModItems.GREEN_CHILI.get(),
            (Item)ModItems.CHILI_SEED.get(),
            (Item)ModItems.LETTUCE.get(),
            new Item[]{(Item)ModItems.LETTUCE_SEED.get()}
         );
      }

      if (MOD_WANTED_ITEMS.contains(stack.getItem())) {
         cir.setReturnValue(true);
      }
   }
}
