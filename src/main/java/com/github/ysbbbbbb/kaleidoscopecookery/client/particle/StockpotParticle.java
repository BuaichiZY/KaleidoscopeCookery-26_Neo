package com.github.ysbbbbbb.kaleidoscopecookery.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

public class StockpotParticle extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   protected StockpotParticle(ClientLevel level, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
      super(level, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
      this.friction = 0.96F;
      this.spriteSet = spriteSet;
      this.scale(1.0F);
      this.hasPhysics = false;
      this.setSpriteFromAge(spriteSet);
   }

   public int getLightColor(float partialTick) {
      return 240;
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   public void tick() {
      super.tick();
      this.setSpriteFromAge(this.spriteSet);
   }

   @OnlyIn(Dist.CLIENT)
   public record Provider(SpriteSet spriteSet) implements ParticleProvider<StockpotParticleOptions> {
      public Particle createParticle(
         StockpotParticleOptions options, ClientLevel level, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed
      ) {
         StockpotParticle particle = new StockpotParticle(level, posX, posY, posZ, xSpeed, ySpeed, zSpeed, this.spriteSet);
         Vector3f color = options.getColor();
         float scale = options.getScale() - 0.1F + level.getRandom().nextFloat() * 0.2F;
         particle.setAlpha(1.0F);
         particle.setColor(color.x, color.y, color.z);
         particle.setSize(scale, scale);
         particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
         particle.setLifetime(level.getRandom().nextInt(4) + 6);
         return particle;
      }
   }
}
