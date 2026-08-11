package com.github.ysbbbbbb.kaleidoscopecookery.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.jspecify.annotations.NullMarked;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

@ParametersAreNonnullByDefault
@NullMarked
public class ThrowableBaoziEntity extends ThrowableItemProjectile {
   public static final EntityType<ThrowableBaoziEntity> TYPE = Builder.<ThrowableBaoziEntity>of(ThrowableBaoziEntity::new, MobCategory.MISC)
      .sized(0.25F, 0.25F)
      .clientTrackingRange(4)
      .updateInterval(10)
      .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "throwable_baozi")));

   public ThrowableBaoziEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
      super(entityType, level);
   }

   public ThrowableBaoziEntity(EntityType<? extends ThrowableItemProjectile> entityType, double x, double y, double z, Level level) {
      super(entityType, x, y, z, level, new ItemStack((Item)ModItems.BAOZI.get()));
   }

   public ThrowableBaoziEntity(EntityType<? extends ThrowableItemProjectile> entityType, LivingEntity shooter, Level level) {
      super(entityType, shooter, level, new ItemStack((Item)ModItems.BAOZI.get()));
   }

   public ThrowableBaoziEntity(Level level, LivingEntity shooter) {
      super(TYPE, shooter, level, new ItemStack((Item)ModItems.BAOZI.get()));
   }

   protected Item getDefaultItem() {
      return (Item)ModItems.BAOZI.get();
   }

   public void handleEntityEvent(byte id) {
      if (id == 3) {
         ParticleOptions option = new ItemParticleOption(ParticleTypes.ITEM, this.getDefaultItem());

         for (int i = 0; i < 12; i++) {
            this.level()
               .addParticle(
                  option,
                  this.getX(),
                  this.getY(),
                  this.getZ(),
                  (this.random.nextFloat() * 2.0F - 1.0F) * 0.1,
                  (this.random.nextFloat() * 2.0F - 1.0F) * 0.1 + 0.1,
                  (this.random.nextFloat() * 2.0F - 1.0F) * 0.1
               );
         }
      }

      if (id == 12) {
         for (int i = 0; i < 7; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
            double offsetY = this.random.nextDouble() * 0.5 + 0.5;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;
            this.level().addParticle(ParticleTypes.HEART, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, 0.0, 0.0, 0.0);
         }
      }
   }

   protected void onHitEntity(EntityHitResult entityHitResult) {
      super.onHitEntity(entityHitResult);
      Entity hitEntity = entityHitResult.getEntity();
      hitEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
      this.playSound(SoundEvents.SNOW_HIT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
      if (hitEntity instanceof Wolf wolf) {
         wolf.heal(wolf.getMaxHealth());
         this.level().broadcastEntityEvent(this, (byte)12);
         if (this.getOwner() instanceof ServerPlayer player) {
            ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "meat_buns_beat_dogs");
         }
      }
   }

   protected void onHit(HitResult hitResult) {
      super.onHit(hitResult);
      if (!this.level().isClientSide()) {
         this.level().broadcastEntityEvent(this, (byte)3);
         this.playSound(SoundEvents.SNOW_HIT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         this.discard();
      }
   }
}
