package com.github.ysbbbbbb.kaleidoscopecookery.client.particle;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class StockpotParticleOptions extends ScalableParticleOptionsBase {
   public static final MapCodec<StockpotParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(StockpotParticleOptions::getColor),
            SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)
         )
         .apply(instance, (color, scale) -> new StockpotParticleOptions(new Vector3f(color), scale))
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, StockpotParticleOptions> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VECTOR3F,
      StockpotParticleOptions::getColor,
      ByteBufCodecs.FLOAT,
      ScalableParticleOptionsBase::getScale,
      (color, scale) -> new StockpotParticleOptions(new Vector3f(color), scale)
   );
   private final Vector3f color;

   public StockpotParticleOptions(Vector3f color, float scale) {
      super(scale);
      this.color = color;
   }

   public Vector3f getColor() {
      return this.color;
   }

   public ParticleType<StockpotParticleOptions> getType() {
      return (ParticleType<StockpotParticleOptions>)ModParticles.STOCKPOT.get();
   }
}
