package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.mixin.StructureTemplatePoolAccessor;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public final class AddVillageStructuresEvent {
    @SubscribeEvent
    public static void addVillageStructures(ServerAboutToStartEvent event) {
        var registries = event.getServer().registryAccess();
        var pools = registries.lookupOrThrow(Registries.TEMPLATE_POOL);
        var processors = registries.lookupOrThrow(Registries.PROCESSOR_LIST).getOrThrow(
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.parse("kaleidoscope_cookery:crop_replace")));
        for (String biome : new String[] {"plains", "snowy", "savanna", "desert", "taiga"}) {
            var pool = pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL,
                Identifier.parse("minecraft:village/" + biome + "/houses"))).value();
            String location = "kaleidoscope_cookery:village/houses/" + biome + "_kitchen";
            // Guard repeated initialization and preserve other mods' pool entries.
            if (pool.getTemplates().stream().anyMatch(entry -> entry.getFirst().toString().contains(location))) continue;
            var piece = SinglePoolElement.legacy(location, processors).apply(StructureTemplatePool.Projection.RIGID);
            var accessor = (StructureTemplatePoolAccessor) pool;
            var entries = new ArrayList<>(pool.getTemplates());
            entries.add(Pair.of(piece, 4));
            accessor.cookery$setRawTemplates(entries);
            for (int i = 0; i < 4; i++) accessor.cookery$getTemplates().add(piece);
            accessor.cookery$setMaxSize(Integer.MIN_VALUE);
        }
    }
}
