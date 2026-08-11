package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent.RegisterAdditional;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public class ModModelEvent {
   private static final String MODELS = "models/";
   private static final String MODELS_CHOPPING_BOARD = "models/chopping_board";
   private static final String MODELS_CARPET = "models/block/carpet";
   private static final String JSON = ".json";

   @SubscribeEvent
   public static void registerModels(RegisterAdditional event) {
      ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
      resourceManager.listResources("models/chopping_board", id -> id.getPath().endsWith(".json"))
         .keySet()
         .stream()
         .map(ModModelEvent::handleModelId)
         .forEach(event::register);
      resourceManager.listResources("models/block/carpet", id -> id.getPath().endsWith(".json"))
         .keySet()
         .stream()
         .map(ModModelEvent::handleModelId)
         .forEach(event::register);
      event.register(ModelIdentifier.standalone(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/honey")));
      event.register(ModelIdentifier.standalone(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/egg")));
      event.register(ModelIdentifier.standalone(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/oil_in_millstone")));
      ItemRenderReplacer.resetCache();
   }

   private static ModelIdentifier handleModelId(Identifier input) {
      String namespace = input.getNamespace();
      String path = input.getPath();
      String substring = path.substring("models/".length(), path.length() - ".json".length());
      Identifier id = Identifier.fromNamespaceAndPath(namespace, substring);
      return ModelIdentifier.standalone(id);
   }
}
