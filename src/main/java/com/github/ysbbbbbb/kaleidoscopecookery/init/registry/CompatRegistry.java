package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra.TetraCompat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

@EventBusSubscriber()
public class CompatRegistry {
   public static boolean SHOW_POTION_EFFECT_TOOLTIPS = true;

   @SubscribeEvent
   public static void onEnqueue(InterModEnqueueEvent event) {
      event.enqueueWork(() -> SHOW_POTION_EFFECT_TOOLTIPS = !ModList.get().isLoaded("foodeffecttooltips"));
      event.enqueueWork(() -> checkModLoad("tetra", TetraCompat::init));
   }

   private static void checkModLoad(String modId, Runnable runnable) {
      if (ModList.get().isLoaded(modId)) {
         runnable.run();
      }
   }
}
