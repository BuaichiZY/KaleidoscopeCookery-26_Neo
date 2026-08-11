package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class VitalityEvent {
   @SubscribeEvent
   public static void onLivingDeath(LivingDeathEvent event) {
      Level level = event.getEntity().level();
      if (!level.isClientSide()) {
         Entity entity = event.getEntity();
         if (event.getSource().getEntity() instanceof LivingEntity living && living.hasEffect(ModEffects.VITALITY)) {
            EntityType<?> type = entity.getType();
            Vec3 pos = entity.position();
            if (entity instanceof AgeableMob mob && !mob.isBaby()) {
               if (type.create(level, EntitySpawnReason.TRIGGERED) instanceof AgeableMob ageableMob) {
                  ageableMob.setBaby(true);
                  ageableMob.setPos(pos);
                  level.addFreshEntity(ageableMob);
               }

               return;
            }

            if (entity instanceof Zombie mob && !mob.isBaby()) {
               if (level.getRandom().nextInt(20) == 0) {
                  Villager villager = new Villager(EntityType.VILLAGER, level);
                  villager.setBaby(true);
                  villager.setPos(pos);
                  level.addFreshEntity(villager);
                  return;
               }

               if (type.create(level, EntitySpawnReason.TRIGGERED) instanceof Zombie zombie) {
                  zombie.setBaby(true);
                  zombie.setPos(pos);
                  level.addFreshEntity(zombie);
               }
            }
         }
      }
   }
}
