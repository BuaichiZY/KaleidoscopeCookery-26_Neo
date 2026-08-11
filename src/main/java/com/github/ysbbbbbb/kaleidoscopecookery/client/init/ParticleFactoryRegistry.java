package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.client.particle.CookingParticle;
import com.github.ysbbbbbb.kaleidoscopecookery.client.particle.StockpotParticle;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public class ParticleFactoryRegistry {
   @SubscribeEvent
   public static void onRegisterParticleFactory(RegisterParticleProvidersEvent event) {
      event.registerSpriteSet((ParticleType)ModParticles.COOKING.get(), CookingParticle.Provider::new);
      event.registerSpriteSet((ParticleType)ModParticles.STOCKPOT.get(), StockpotParticle.Provider::new);
   }
}
