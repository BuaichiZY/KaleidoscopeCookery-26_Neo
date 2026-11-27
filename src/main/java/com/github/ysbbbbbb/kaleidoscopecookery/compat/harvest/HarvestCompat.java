package com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest;


import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

public class HarvestCompat {
    public static final String ID = "harvest_with_ease";

    public static void init() {
        ModList.get().getModContainerById(ID).ifPresent(modContainer ->
                NeoForge.EVENT_BUS.addListener(CropHarvestEvent::onHarvest));
    }
}
