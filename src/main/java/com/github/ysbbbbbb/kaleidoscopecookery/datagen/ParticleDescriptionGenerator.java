package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class ParticleDescriptionGenerator extends ParticleDescriptionProvider {
   public ParticleDescriptionGenerator(PackOutput output, ExistingFileHelper fileHelper) {
      super(output, fileHelper);
   }

   protected void addDescriptions() {
      this.spriteSet((ParticleType)ModParticles.COOKING.get(), Identifier.withDefaultNamespace("generic"), 8, true);
      this.spriteSet((ParticleType)ModParticles.STOCKPOT.get(), Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "stockpot"), 5, false);
   }
}
