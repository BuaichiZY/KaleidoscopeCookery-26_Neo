package com.github.ysbbbbbb.kaleidoscopecookery.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidUtils {
   public static boolean emptyItem(LivingEntity user, ItemStack bucket, IFluidHandler handler, int amount) {
      ItemStack copy = bucket.copyWithCount(1);
      return FluidUtil.getFluidHandler(copy).map(stackFluid -> {
         FluidStack transfer = FluidUtil.tryFluidTransfer(handler, stackFluid, amount, true);
         if (transfer.isEmpty()) {
            return false;
         } else {
            ItemStack result = stackFluid.getContainer();
            if (!(user instanceof Player player && player.isCreative())) {
               bucket.shrink(1);
            }

            ItemUtils.getItemToLivingEntity(user, result);
            SoundEvent sound = transfer.getFluid().getFluidType().getSound(transfer, SoundActions.BUCKET_EMPTY);
            if (sound != null) {
               user.playSound(sound);
            }

            return true;
         }
      }).orElse(false);
   }

   public static boolean fillItem(LivingEntity user, ItemStack bucket, IFluidHandler handler, int amount) {
      ItemStack copy = bucket.copyWithCount(1);
      return FluidUtil.getFluidHandler(copy).map(stackFluid -> {
         FluidStack transfer = FluidUtil.tryFluidTransfer(stackFluid, handler, amount, false);
         if (transfer.isEmpty()) {
            return false;
         } else {
            FluidUtil.tryFluidTransfer(stackFluid, handler, amount, true);
            ItemStack result = stackFluid.getContainer();
            if (!(user instanceof Player player && player.isCreative())) {
               bucket.shrink(1);
            }

            ItemUtils.getItemToLivingEntity(user, result);
            SoundEvent sound = transfer.getFluid().getFluidType().getSound(transfer, SoundActions.BUCKET_FILL);
            if (sound != null) {
               user.playSound(sound);
            }

            return true;
         }
      }).orElse(false);
   }

   public static boolean hasFluid(ItemStack stack) {
      return FluidUtil.getFluidHandler(stack).map(handler -> {
         for (int i = 0; i < handler.getTanks(); i++) {
            if (!handler.getFluidInTank(i).isEmpty()) {
               return true;
            }
         }

         return false;
      }).orElse(false);
   }
}
