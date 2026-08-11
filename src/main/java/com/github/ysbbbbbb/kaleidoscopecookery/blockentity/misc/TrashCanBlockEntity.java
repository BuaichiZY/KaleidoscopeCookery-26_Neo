package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TrashCanBlockEntity extends BaseBlockEntity {
   public static final int EVENT_PUT = 1;
   public static final int EVENT_WITHDRAW = 2;
   public static final int EVENT_ENTER = 3;
   private final ItemStackHandler storage = new ItemStackHandler(3);
   public AnimationState putState = new AnimationState();
   public AnimationState withdrawState = new AnimationState();
   public AnimationState player1State = new AnimationState();
   public AnimationState player2State = new AnimationState();
   public AnimationState enterState = new AnimationState();

   public TrashCanBlockEntity(BlockPos pos, BlockState blockState) {
      super(ModBlocks.TRASH_CAN_BE.get(), pos, blockState);
   }

   public void clientTick(Level level) {
      long offset = level.getGameTime() + this.worldPosition.hashCode();
      if (Math.floorMod(offset, 61) == 0) {
         List<SitEntity> sits = level.getEntitiesOfClass(SitEntity.class, new AABB(this.worldPosition));
         if (!sits.isEmpty()) {
            if (level.getRandom().nextBoolean()) {
               this.player2State.stop();
               this.player1State.start((int)level.getGameTime());
            } else {
               this.player1State.stop();
               this.player2State.start((int)level.getGameTime());
            }
         }
      }

      List<SitEntity> occupiedSeats = level.getEntitiesOfClass(SitEntity.class, new AABB(this.worldPosition));
      if (!occupiedSeats.isEmpty() && !this.player1State.isStarted() && !this.player2State.isStarted()) {
         this.player1State.start((int)level.getGameTime());
      }

      if (Math.floorMod(offset, 5) == 0) {
         if (occupiedSeats.isEmpty()) {
            this.player1State.stop();
            this.player2State.stop();
         }
      }
   }

   public void serverTick(ServerLevel level) {
      if (level.getGameTime() % 2L != 0L) {
         return;
      }

      AABB intake = new AABB(this.worldPosition).inflate(0.125D, 0.0D, 0.125D).expandTowards(0.0D, 0.75D, 0.0D);
      for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, intake)) {
         this.entityInside(level, this.worldPosition, item);
      }
   }

   public void entityInside(Level level, BlockPos pos, Entity entity) {
      if (entity instanceof ItemEntity itemEntity) {
         AABB entityBox = entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ());
         VoxelShape shape = Shapes.create(entityBox);
         if (Shapes.joinIsNotEmpty(shape, TrashCanBlock.SUCK_ZONE, BooleanOp.AND)) {
            if (this.hasItem(itemEntity.getItem())) {
               if (level instanceof ServerLevel serverLevel) {
                  serverLevel.sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), 1, 0.1, 0.1, 0.1, 0.01);
                  serverLevel.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1.0F, 0.65F);
                  serverLevel.blockEvent(pos, this.getBlockState().getBlock(), EVENT_PUT, 0);
               }

               entity.discard();
            }
         }
      }
   }

   public void putItem(ItemStack stack) {
      ItemStack result = ItemHandlerHelper.insertItemStacked(this.storage, stack.copy(), false);
      if (result.getCount() == stack.getCount()) {
         this.storage.setStackInSlot(0, this.storage.getStackInSlot(1).copy());
         this.storage.setStackInSlot(1, this.storage.getStackInSlot(2).copy());
         this.storage.setStackInSlot(2, stack.copy());
         stack.shrink(stack.getCount());
      }

      if (result.getCount() < stack.getCount()) {
         stack.shrink(stack.getCount() - result.getCount());
      }

      if (this.level instanceof ServerLevel serverLevel) {
         BlockPos pos = this.getBlockPos();
         serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5, 3, 0.25, 0.05, 0.25, 0.01);
         serverLevel.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1.0F, 0.5F);
         serverLevel.blockEvent(pos, this.getBlockState().getBlock(), EVENT_PUT, 0);
      }

      if (this.level != null) {
         this.putState.start((int)this.level.getGameTime());
      }

      this.refresh();
   }

   public void withdrawItem(LivingEntity user) {
      for (int i = this.storage.getSlots() - 1; i >= 0; i--) {
         ItemStack stack = this.storage.getStackInSlot(i);
         if (!stack.isEmpty()) {
            user.setItemInHand(InteractionHand.MAIN_HAND, stack.copy());
            this.storage.setStackInSlot(i, ItemStack.EMPTY);
            if (this.level instanceof ServerLevel serverLevel) {
               BlockPos pos = this.getBlockPos();
               serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5, 3, 0.25, 0.05, 0.25, 0.01);
               serverLevel.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 1.0F, 0.8F);
               serverLevel.blockEvent(pos, this.getBlockState().getBlock(), EVENT_WITHDRAW, 0);
            }

            if (this.level != null) {
               this.withdrawState.start((int)this.level.getGameTime());
            }

            this.refresh();
            return;
         }
      }
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.putChild("Storage", this.storage);
   }

   public void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
tag.readChild("Storage",       this.storage);
   }

   private boolean hasItem(ItemStack itemStack) {
      for (int i = 0; i < this.storage.getSlots(); i++) {
         if (this.storage.getStackInSlot(i).is(itemStack.getItem())) {
            ItemStack result = this.storage.insertItem(i, itemStack.copy(), false);
            if (result.getCount() < itemStack.getCount()) {
               this.refresh();
            }

            return true;
         }
      }

      return false;
   }

   public ItemStackHandler getStorage() {
      return this.storage;
   }

   @Override
   public boolean triggerEvent(int id, int type) {
      if (this.level != null) {
         int tick = (int)this.level.getGameTime();
         if (id == EVENT_PUT) {
            this.putState.start(tick);
            return true;
         }
         if (id == EVENT_WITHDRAW) {
            this.withdrawState.start(tick);
            return true;
         }
         if (id == EVENT_ENTER) {
            this.enterState.start(tick);
            return true;
         }
      }
      return super.triggerEvent(id, type);
   }
}
