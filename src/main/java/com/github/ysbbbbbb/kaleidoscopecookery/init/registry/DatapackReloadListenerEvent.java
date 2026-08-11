package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.datamap.resources.MillstoneBindableDataReloadListener;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class DatapackReloadListenerEvent {
   @SubscribeEvent
   public static void onAddReloadListenerEvent(AddServerReloadListenersEvent event) {
      event.addListener(
         Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone_bindable"),
         new MillstoneBindableDataReloadListener()
      );
   }
}
