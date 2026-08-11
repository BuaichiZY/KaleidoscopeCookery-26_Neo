package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber()
public class ModEntities {
   public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "kaleidoscope_cookery");
   public static Supplier<EntityType<SitEntity>> SIT = ENTITY_TYPES.register("sit", () -> SitEntity.TYPE);
   public static Supplier<EntityType<ScarecrowEntity>> SCARECROW = ENTITY_TYPES.register("scarecrow", () -> ScarecrowEntity.TYPE);
   public static Supplier<EntityType<ThrowableBaoziEntity>> THROWABLE_BAOZI = ENTITY_TYPES.register("throwable_baozi", () -> ThrowableBaoziEntity.TYPE);

   @SubscribeEvent
   public static void addEntityAttributeEvent(EntityAttributeCreationEvent event) {
      event.put(ScarecrowEntity.TYPE, ScarecrowEntity.createAttributes().build());
   }
}
