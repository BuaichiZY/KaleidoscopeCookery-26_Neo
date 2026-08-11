package com.github.ysbbbbbb.kaleidoscopecookery.client.resources;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult.Error;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ItemRenderReplacerReloadListener implements ResourceManagerReloadListener {
   public static final ItemRenderReplacer INSTANCE = new ItemRenderReplacer();
   private static final Identifier FILE_PATH = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "models/item_render_replacer.json");

   public void onResourceManagerReload(ResourceManager resourceManager) {
      resourceManager.listPacks().forEach(packResources -> {
         IoSupplier<InputStream> resource = packResources.getResource(PackType.CLIENT_RESOURCES, FILE_PATH);
         if (resource != null) {
            try (
               InputStream inputStream = (InputStream)resource.get();
               InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            ) {
               JsonElement jsonElement = JsonParser.parseReader(reader);
               DataResult<ItemRenderReplacer> result = ItemRenderReplacer.CODEC.parse(JsonOps.INSTANCE, jsonElement);
               if (result.result().isPresent()) {
                  INSTANCE.addAll((ItemRenderReplacer)result.result().get());
                  KaleidoscopeCookery.LOGGER.info("Successfully loaded item render replacer data");
               } else if (result.error().isPresent()) {
                  KaleidoscopeCookery.LOGGER.error("Failed to parse item render replacer data: {}", ((Error)result.error().get()).message());
               }
            } catch (Exception var10) {
               KaleidoscopeCookery.LOGGER.error("Failed to load item render replacer resource", var10);
            }
         }
      });
   }
}
