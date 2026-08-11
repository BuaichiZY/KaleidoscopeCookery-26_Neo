package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class AddVillageStructuresEvent {
   private static final ResourceKey<StructureProcessorList> CROP_REPLACE_PROCESSOR_LIST_KEY = ResourceKey.create(
      Registries.PROCESSOR_LIST, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "crop_replace")
   );
   private static final Identifier PLAINS = Identifier.parse("minecraft:village/plains/houses");
   private static final Identifier SNOWY = Identifier.parse("minecraft:village/snowy/houses");
   private static final Identifier SAVANNA = Identifier.parse("minecraft:village/savanna/houses");
   private static final Identifier DESERT = Identifier.parse("minecraft:village/desert/houses");
   private static final Identifier TAIGA = Identifier.parse("minecraft:village/taiga/houses");

   @SubscribeEvent
   public static void addVillageStructures(ServerAboutToStartEvent event) {
      Frozen registryAccess = event.getServer().registryAccess();
      addBuildingToPool(registryAccess, PLAINS, "village/houses/plains_kitchen", 4);
      addBuildingToPool(registryAccess, SNOWY, "village/houses/snowy_kitchen", 4);
      addBuildingToPool(registryAccess, SAVANNA, "village/houses/savanna_kitchen", 4);
      addBuildingToPool(registryAccess, DESERT, "village/houses/desert_kitchen", 4);
      addBuildingToPool(registryAccess, TAIGA, "village/houses/taiga_kitchen", 4);
   }

   public static void addBuildingToPool(RegistryAccess registryAccess, Identifier poolId, String structId, int weight) {
      Optional<Registry<StructureTemplatePool>> templatePools = registryAccess.registry(Registries.TEMPLATE_POOL);
      if (!templatePools.isEmpty()) {
         Optional<Registry<StructureProcessorList>> processorLists = registryAccess.registry(Registries.PROCESSOR_LIST);
         if (!processorLists.isEmpty()) {
            StructureTemplatePool pool = (StructureTemplatePool)templatePools.get().get(poolId);
            if (pool != null) {
               Holder<StructureProcessorList> holder = processorLists.get().getHolderOrThrow(CROP_REPLACE_PROCESSOR_LIST_KEY);
               Identifier structLocation = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", structId);
               SinglePoolElement piece = (SinglePoolElement)SinglePoolElement.legacy(structLocation.toString(), holder).apply(Projection.RIGID);

               for (int i = 0; i < weight; i++) {
                  pool.templates.add(piece);
               }

               List<Pair<StructurePoolElement, Integer>> newRawTemplates = Lists.newArrayList(pool.rawTemplates);
               newRawTemplates.add(Pair.of(piece, weight));
               pool.rawTemplates = newRawTemplates;
            }
         }
      }
   }
}
