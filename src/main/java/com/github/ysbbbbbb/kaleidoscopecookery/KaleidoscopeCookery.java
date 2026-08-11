package com.github.ysbbbbbb.kaleidoscopecookery;

import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModArmorMaterials;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModAttachmentType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModCreativeTabs;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEntities;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModPoi;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import org.slf4j.Logger;

@Mod("kaleidoscope_cookery")
public class KaleidoscopeCookery {
   public static final String MOD_ID = "kaleidoscope_cookery";
   public static final Logger LOGGER = LogUtils.getLogger();

   public KaleidoscopeCookery(IEventBus modEventBus, ModContainer modContainer) {
      modContainer.registerConfig(Type.COMMON, GeneralConfig.init());
      modContainer.registerConfig(Type.CLIENT, ClientConfig.init());
      modEventBus.addListener(NetworkHandler::registerPacket);
      FoodBiteRegistry.init();
      TeacupRegistry.init();
      PlateRegistry.init();
      ModTrigger.TRIGGERS.register(modEventBus);
      ModBlocks.BLOCKS.register(modEventBus);
      ModBlocks.BLOCK_ENTITIES.register(modEventBus);
      ModItems.ITEMS.register(modEventBus);
      ModEntities.ENTITY_TYPES.register(modEventBus);
      ModEffects.EFFECTS.register(modEventBus);
      ModPoi.POI_TYPES.register(modEventBus);
      ModVillager.VILLAGER_PROFESSION.register(modEventBus);
      ModCreativeTabs.TABS.register(modEventBus);
      ModSounds.SOUND_EVENTS.register(modEventBus);
      ModParticles.PARTICLES.register(modEventBus);
      ModRecipes.RECIPE_TYPES.register(modEventBus);
      ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
      ModDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
      ModAttachmentType.ATTACHMENT_TYPES.register(modEventBus);
   }
}
