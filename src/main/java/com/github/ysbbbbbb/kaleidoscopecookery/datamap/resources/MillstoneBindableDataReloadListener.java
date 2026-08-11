package com.github.ysbbbbbb.kaleidoscopecookery.datamap.resources;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.MillstoneBindableData;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult.Error;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EntityType;

public class MillstoneBindableDataReloadListener implements ResourceManagerReloadListener {
   public static final Map<EntityType<?>, MillstoneBindableData> INSTANCE = Maps.newHashMap();
   private static final Identifier FILE_PATH = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "datamap/millstone_bindable_data.json");

   public void onResourceManagerReload(ResourceManager resourceManager) {
      resourceManager.listPacks().forEach(packResources -> {
         IoSupplier<InputStream> resource = packResources.getResource(PackType.SERVER_DATA, FILE_PATH);
         if (resource != null) {
            try (
               InputStream inputStream = (InputStream)resource.get();
               InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            ) {
               JsonElement jsonElement = JsonParser.parseReader(reader);
               DataResult<Map<EntityType<?>, MillstoneBindableData>> result = MillstoneBindableData.CODEC.parse(JsonOps.INSTANCE, jsonElement);
               if (result.result().isPresent()) {
                  INSTANCE.putAll((Map<? extends EntityType<?>, ? extends MillstoneBindableData>)result.result().get());
                  KaleidoscopeCookery.LOGGER.info("Successfully loaded millstone bindable data");
               } else if (result.error().isPresent()) {
                  KaleidoscopeCookery.LOGGER.error("Failed to parse millstone bindable data: {}", ((Error)result.error().get()).message());
               }
            } catch (Exception var10) {
               KaleidoscopeCookery.LOGGER.error("Failed to load millstone bindable data", var10);
            }
         }
      });
   }
}
