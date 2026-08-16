package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Punchy renders its custom first-person player arms before NeoForge fires
 * RenderHandEvent.  Treat raw dough as blacklisted only inside Punchy so its
 * extra arm/item copy stays hidden while our original centred animation runs.
 * @Pseudo keeps this compatibility hook optional when Punchy is not installed.
 */
@Pseudo
@Mixin(targets = "punchy.config.PunchyConfig", remap = false)
public abstract class PunchyRawDoughCompatMixin {
   @Inject(method = "isItemBlacklisted", at = @At("HEAD"), cancellable = true, remap = false)
   private static void blacklistRawDoughFromPunchy(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
      if (stack != null && stack.getItem() == ModItems.RAW_DOUGH.get()) {
         cir.setReturnValue(true);
      }
   }
}
