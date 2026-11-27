package com.github.ysbbbbbb.kaleidoscopecookery.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.resource.ResourceStack;
import org.jspecify.annotations.Nullable;

/** Fluid-container helpers implemented on NeoForge's transactional transfer API. */
public final class FluidUtils {
   private FluidUtils() {}

   public static @Nullable ResourceHandler<FluidResource> getItemHandler(ItemStack stack) {
      if (stack.isEmpty()) return null;
      return ItemAccess.forStack(stack).oneByOne().getCapability(Capabilities.Fluid.ITEM);
   }

   public static FluidStack getFirstFluid(ItemStack stack) {
      ResourceHandler<FluidResource> handler = getItemHandler(stack);
      if (handler == null) return FluidStack.EMPTY;
      for (int slot = 0; slot < handler.size(); slot++) {
         if (!handler.getResource(slot).isEmpty()) return FluidUtil.getStack(handler, slot);
      }
      return FluidStack.EMPTY;
   }

   public static boolean emptyItem(LivingEntity user, ItemStack container, ResourceHandler<FluidResource> destination, int amount) {
      return transferContainer(user, container, destination, amount, true);
   }

   public static boolean fillItem(LivingEntity user, ItemStack container, ResourceHandler<FluidResource> source, int amount) {
      return transferContainer(user, container, source, amount, false);
   }

   private static boolean transferContainer(LivingEntity user, ItemStack container, ResourceHandler<FluidResource> other, int amount, boolean empty) {
      ItemStacksResourceHandler itemStorage = new ItemStacksResourceHandler(1);
      ItemStack single = container.copyWithCount(1);
      itemStorage.set(0, ItemResource.of(single), 1);
      ResourceHandler<FluidResource> contained = ItemAccess.forHandlerIndexStrict(itemStorage, 0)
         .oneByOne().getCapability(Capabilities.Fluid.ITEM);
      if (contained == null) return false;

      ResourceStack<FluidResource> moved = empty
         ? ResourceHandlerUtil.moveFirst(contained, other, resource -> true, amount, null)
         : ResourceHandlerUtil.moveFirst(other, contained, resource -> true, amount, null);
      if (moved == null || moved.amount() <= 0) return false;

      ItemStack result = ItemUtil.getStack(itemStorage, 0);
      if (!(user instanceof Player player && player.isCreative())) container.shrink(1);
      ItemUtils.getItemToLivingEntity(user, result);
      FluidStack soundStack = moved.resource().toStack(moved.amount());
      SoundEvent sound = moved.resource().getFluidType().getSound(
         soundStack, empty ? SoundActions.BUCKET_EMPTY : SoundActions.BUCKET_FILL
      );
      if (sound != null) user.playSound(sound);
      return true;
   }

   public static boolean hasFluidHandler(ItemStack stack) { return getItemHandler(stack) != null; }
   public static boolean hasFluid(ItemStack stack) { return !getFirstFluid(stack).isEmpty(); }
}
