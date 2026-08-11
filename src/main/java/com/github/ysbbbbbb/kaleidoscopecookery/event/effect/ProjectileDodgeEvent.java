package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class ProjectileDodgeEvent {
   private static final int DODGE_COST = 200;

   @SubscribeEvent
   public static void onProjectileHit(ProjectileImpactEvent event) {
      if (!event.getEntity().level().isClientSide()) {
         if (event.getRayTraceResult() instanceof EntityHitResult hitResult
            && hitResult.getEntity() instanceof LivingEntity living
            && living.hasEffect(ModEffects.PROJECTILE_DODGE)) {
            event.setCanceled(true);
            randomTeleport(living.level(), living, 3.0, 16);
            MobEffectInstance instance = living.getEffect(ModEffects.PROJECTILE_DODGE);
            if (instance != null) {
               if (instance.isInfiniteDuration()) {
                  return;
               }

               if (instance.getDuration() <= 200) {
                  living.removeEffect(ModEffects.PROJECTILE_DODGE);
               } else {
                  MobEffectInstance shortened = new MobEffectInstance(
                     instance.getEffect(),
                     instance.getDuration() - 200,
                     instance.getAmplifier(),
                     instance.isAmbient(),
                     instance.isVisible(),
                     instance.showIcon()
                  );
                  living.forceAddEffect(shortened, null);
               }
            }
         }
      }
   }

   public static void randomTeleport(Level level, LivingEntity living, double range, int maxAttempts) {
      if (!level.isClientSide()) {
         double x = living.getX();
         double y = living.getY();
         double z = living.getZ();
         int minH = level.getMinY();
         int maxH = ((ServerLevel)level).getLogicalHeight();

         for (int i = 0; i < maxAttempts; i++) {
            double targetX = x + (living.getRandom().nextDouble() - 0.5) * range;
            double targetY = Mth.clamp(y + (living.getRandom().nextDouble() - 0.5) * range, minH, minH + maxH - 1);
            double targetZ = z + (living.getRandom().nextDouble() - 0.5) * range;
            if (living.isPassenger()) {
               living.stopRiding();
            }

            Vec3 previousPos = living.position();
            level.gameEvent(GameEvent.TELEPORT, previousPos, Context.of(living));
            if (living.randomTeleport(targetX, targetY, targetZ, true)) {
               SoundEvent soundEvent = SoundEvents.ENDERMAN_TELEPORT;
               level.playSound(null, x, y, z, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
               living.playSound(soundEvent, 1.0F, 1.0F);
               break;
            }
         }
      }
   }
}
