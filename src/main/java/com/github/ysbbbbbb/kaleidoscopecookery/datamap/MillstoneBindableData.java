package com.github.ysbbbbbb.kaleidoscopecookery.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public record MillstoneBindableData(int rotSpeedTick, float liftAngle, Vec3 offset) {
   private static final Codec<MillstoneBindableData> DATA_CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            Codec.INT.fieldOf("rot_speed_tick").forGetter(MillstoneBindableData::rotSpeedTick),
            Codec.FLOAT.fieldOf("lift_angle").forGetter(MillstoneBindableData::liftAngle),
            Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(MillstoneBindableData::offset)
         )
         .apply(instance, MillstoneBindableData::new)
   );
   private static final Codec<EntityType<?>> ENTITY_TYPE_CODEC = Identifier.CODEC.comapFlatMap(id -> {
      EntityType<?> type = (EntityType<?>)BuiltInRegistries.ENTITY_TYPE.getValue(id);
      return DataResult.success(type);
   }, BuiltInRegistries.ENTITY_TYPE::getKey);
   public static final Codec<Map<EntityType<?>, MillstoneBindableData>> CODEC = Codec.unboundedMap(ENTITY_TYPE_CODEC, DATA_CODEC);
   public static final MillstoneBindableData DEFAULT = new MillstoneBindableData(200, 5.0F, Vec3.ZERO);
}
