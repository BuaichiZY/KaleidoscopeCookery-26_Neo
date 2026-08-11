package com.github.ysbbbbbb.kaleidoscopecookery.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class SitEntity extends Entity implements IEntityWithComplexSpawn {
   public static final EntityType<SitEntity> TYPE = Builder.<SitEntity>of(SitEntity::new, MobCategory.MISC)
      .sized(0.5F, 0.1F)
      .clientTrackingRange(10)
      .noSummon()
      .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "sit")));
   public static final int DEFAULT = 0;
   public static final int TRASH_CAN = 1;
   private int passengerTick = 0;
   private int sitType = 0;

   public SitEntity(EntityType<?> entityTypeIn, Level worldIn) {
      super(entityTypeIn, worldIn);
   }

   public SitEntity(Level worldIn, BlockPos pos) {
      this(TYPE, worldIn);
      this.setPos(pos.getX() + 0.5, pos.getY() + 0.4375, pos.getZ() + 0.5);
   }

   public SitEntity(Level worldIn, BlockPos pos, double y) {
      this(TYPE, worldIn);
      this.setPos(pos.getX() + 0.5, pos.getY() + y, pos.getZ() + 0.5);
   }

   public SitEntity(Level worldIn, BlockPos pos, double y, int sitType) {
      this(worldIn, pos, y);
      this.sitType = sitType;
   }

   public SitEntity(Level worldIn, BlockPos pos, int sitType) {
      this(worldIn, pos);
      this.sitType = sitType;
   }

   public Vec3 getPassengerRidingPosition(Entity entity) {
      return super.getPassengerRidingPosition(entity).add(0.0, -0.0625, 0.0);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
   }

   protected void readAdditionalSaveData(ValueInput tag) {
      this.sitType = tag.getIntOr("SitType", 0);
   }

   protected void addAdditionalSaveData(ValueOutput tag) {
      tag.putInt("SitType", this.sitType);
   }

   public void tick() {
      if (!this.level().isClientSide()) {
         this.checkBelowWorld();
         this.checkPassengers();
         if (this.tickCount % 20 == 0) {
            BlockState blockState = this.level().getBlockState(this.blockPosition());
            if (!blockState.is(TagMod.SITTABLE)) {
               this.discard();
            }
         }
      }
   }

   protected void removePassenger(Entity passenger) {
      if (this.getSitType() == 1 && passenger instanceof Player player) {
         BlockPos blockPos = this.blockPosition();
         if (this.level().getBlockEntity(blockPos) instanceof TrashCanBlockEntity trashCan) {
            trashCan.player1State.stop();
            trashCan.player2State.stop();
            player.playSound((SoundEvent)ModSounds.TRASH_CAN.get());
         }
      }

      super.removePassenger(passenger);
   }

   private void checkPassengers() {
      if (this.getPassengers().isEmpty()) {
         this.passengerTick++;
      } else {
         this.passengerTick = 0;
      }

      if (this.passengerTick > 10) {
         this.discard();
      }
   }

   public boolean skipAttackInteraction(Entity targetEntity) {
      return true;
   }

   public boolean hurtServer(ServerLevel level, DamageSource damageSource, float damageAmount) {
      return false;
   }

   public void move(MoverType moverType, Vec3 movement) {
   }

   public void push(Entity pushedEntity) {
   }

   public void push(double x, double y, double z) {
   }

   protected boolean repositionEntityAfterLoad() {
      return false;
   }

   public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
   }

   public void refreshDimensions() {
   }

   public boolean canCollideWith(Entity entity) {
      return false;
   }

   public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
      buffer.writeVarInt(this.sitType);
   }

   public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
      this.sitType = additionalData.readVarInt();
   }

   public int getSitType() {
      return this.sitType;
   }
}
