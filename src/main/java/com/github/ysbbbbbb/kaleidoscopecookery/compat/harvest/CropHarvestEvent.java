package com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import it.crystalnest.harvest_with_ease.api.event.HarvestEvents.HarvestCheckEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CropHarvestEvent {
   static void onHarvest(HarvestCheckEvent event) {
      BlockState crop = event.getCrop();
      if (crop.is((Block)ModBlocks.TOMATO_CROP.get()) || crop.is((Block)ModBlocks.CHILI_CROP.get()) || crop.is((Block)ModBlocks.RICE_CROP.get())) {
         event.preventHarvest();
      }
   }
}
