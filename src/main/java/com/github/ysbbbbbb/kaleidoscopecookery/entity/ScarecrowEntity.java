package com.github.ysbbbbbb.kaleidoscopecookery.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.LivingEntity.Fallsounds;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ScarecrowEntity extends LivingEntity {
   public static final EntityType<ScarecrowEntity> TYPE = Builder.<ScarecrowEntity>of(ScarecrowEntity::new, MobCategory.MISC)
      .sized(0.5F, 2.375F)
      .clientTrackingRange(10)
      .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "scarecrow")));
   private static final Predicate<Entity> RIDABLE_MINECARTS = e -> e instanceof AbstractMinecart;
   private static final Predicate<Entity> SHOULDER_RIDING_ENTITY = e -> e instanceof ShoulderRidingEntity entity
      && !entity.isOrderedToSit()
      && entity.canSitOnShoulder();
   private static final String HAND_ITEMS_TAG = "HandItems";
   private static final String ARMOR_ITEMS_TAG = "ArmorItems";
   private static final String SHOULDER_ENTITY_TAG = "ShoulderEntity";
   private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
   private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
   private CompoundTag shoulderEntity = new CompoundTag();
   public long lastHit;
   private int cooldown;
   private long timeEntitySatOnShoulder;

   public ScarecrowEntity(EntityType<ScarecrowEntity> type, Level level) {
      super(type, level);
   }

   public ScarecrowEntity(Level level, double pX, double pY, double pZ) {
      this(TYPE, level);
      this.setPos(pX, pY, pZ);
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      return createLivingAttributes().add(Attributes.STEP_HEIGHT, 0.0);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
   }

   public void tick() {
      super.tick();
      if (this.cooldown > 0) {
         this.cooldown--;
      }
   }

   @Override
   public InteractionResult interact(Player player, InteractionHand hand, Vec3 vec3) {
      ItemStack itemInHand = player.getItemInHand(hand);
      if (itemInHand.is(Items.NAME_TAG)) {
         return InteractionResult.PASS;
      } else if (player.isSpectator()) {
         return InteractionResult.SUCCESS;
      } else if (player.level().isClientSide()) {
         return InteractionResult.CONSUME;
      } else if (hand == InteractionHand.OFF_HAND) {
         return InteractionResult.PASS;
      } else if (this.cooldown > 0) {
         return InteractionResult.PASS;
      } else if (this.isClickHand(vec3)) {
         return this.handleHandItems(player, itemInHand);
      } else {
         return this.isClickHead(vec3) ? this.handleHeadItems(player, itemInHand) : InteractionResult.PASS;
      }
   }

   private InteractionResult handleHeadItems(Player player, ItemStack itemInHand) {
      this.cooldown = 5;
      ItemStack headItem = this.getItemBySlot(EquipmentSlot.HEAD);
      if (itemInHand.isEmpty() && !headItem.isEmpty()) {
         this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
         ItemHandlerHelper.giveItemToPlayer(player, headItem);
         return InteractionResult.SUCCESS;
      } else if (!(itemInHand.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SkullBlock)) {
         return InteractionResult.PASS;
      } else if (player.getAbilities().instabuild && headItem.isEmpty()) {
         this.setItemSlot(EquipmentSlot.HEAD, itemInHand.copyWithCount(1));
         this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "place_head_on_scarecrow");
         return InteractionResult.SUCCESS;
      } else if (itemInHand.isEmpty() || itemInHand.getCount() <= 1) {
         this.setItemSlot(EquipmentSlot.HEAD, itemInHand);
         this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
         player.setItemInHand(InteractionHand.MAIN_HAND, headItem);
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "place_head_on_scarecrow");
         return InteractionResult.SUCCESS;
      } else if (headItem.isEmpty()) {
         this.setItemSlot(EquipmentSlot.HEAD, itemInHand.split(1));
         this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "place_head_on_scarecrow");
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.PASS;
      }
   }

   private InteractionResult handleHandItems(Player player, ItemStack itemInHand) {
      this.cooldown = 5;
      if (itemInHand.isEmpty()) {
         ItemStack mainhand = this.getItemInHand(InteractionHand.MAIN_HAND);
         ItemStack offhand = this.getItemInHand(InteractionHand.OFF_HAND);
         if (!mainhand.isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            ItemHandlerHelper.giveItemToPlayer(player, mainhand);
            return InteractionResult.SUCCESS;
         } else if (!offhand.isEmpty()) {
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            ItemHandlerHelper.giveItemToPlayer(player, offhand);
            return InteractionResult.SUCCESS;
         } else {
            return InteractionResult.PASS;
         }
      } else if (itemInHand.getItem() instanceof BlockItem blockItem
         && blockItem.getBlock() instanceof LanternBlock
         && this.swapHand(InteractionHand.OFF_HAND, player, itemInHand)) {
         this.level().playSound(null, this.blockPosition(), SoundEvents.LANTERN_PLACE, this.getSoundSource());
         return InteractionResult.SUCCESS;
      } else if (itemInHand.isDamageableItem() && this.swapHand(InteractionHand.MAIN_HAND, player, itemInHand)) {
         this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.PASS;
      }
   }

   private boolean swapHand(InteractionHand hand, Player player, ItemStack itemInHand) {
      ItemStack scarecrowStack = this.getItemInHand(hand);
      if (player.getAbilities().instabuild && scarecrowStack.isEmpty() && !itemInHand.isEmpty()) {
         this.setItemInHand(hand, itemInHand.copyWithCount(1));
         return true;
      } else if (itemInHand.isEmpty() || itemInHand.getCount() <= 1) {
         this.setItemInHand(hand, itemInHand);
         player.setItemInHand(InteractionHand.MAIN_HAND, scarecrowStack);
         return true;
      } else if (scarecrowStack.isEmpty()) {
         this.setItemInHand(hand, itemInHand.split(1));
         return true;
      } else {
         return false;
      }
   }

   private boolean isClickHand(Vec3 vector) {
      return 1.0625 <= vector.y && vector.y <= 1.588235294117647;
   }

   private boolean isClickHead(Vec3 vector) {
      return 1.588235294117647 < vector.y;
   }

   public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
      if (this.isRemoved()) {
         return false;
      } else if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         this.kill();
         return false;
      } else if (this.isInvulnerableTo(serverLevel, source)) {
         return false;
      } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
         this.brokenByAnything(serverLevel, source);
         this.kill();
         return false;
      } else if (source.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
         return false;
      } else if (source.isCreativePlayer()) {
         this.playBrokenSound();
         this.showBreakingParticles();
         this.kill();
         return false;
      } else {
         long gameTime = this.level().getGameTime();
         if (gameTime - this.lastHit > 5L) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.ARMOR_STAND_HIT, this.getSoundSource(), 0.3F, 1.0F);
            this.level().broadcastEntityEvent(this, (byte)32);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
            this.lastHit = gameTime;
            if (!this.getShoulderEntity().isEmpty()) {
               this.removeEntitiesOnShoulder();
            }
         } else {
            this.brokenByPlayer(serverLevel, source);
            this.showBreakingParticles();
            this.kill();
         }

         return true;
      }
   }

   public void handleEntityEvent(byte id) {
      if (id == 32) {
         if (this.level().isClientSide()) {
            this.lastHit = this.level().getGameTime();
         }
      } else {
         super.handleEntityEvent(id);
      }
   }

   private void brokenByPlayer(ServerLevel level, DamageSource damageSource) {
      ItemStack stack = new ItemStack((ItemLike)ModItems.SCARECROW.get());
      if (this.hasCustomName()) {
         stack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
      }

      Block.popResource(this.level(), this.blockPosition(), stack);
      this.brokenByAnything(level, damageSource);
   }

   private void brokenByAnything(ServerLevel level, DamageSource damageSource) {
      this.playBrokenSound();
      this.dropAllDeathLoot(level, damageSource);

      for (int i = 0; i < this.handItems.size(); i++) {
         ItemStack stack = (ItemStack)this.handItems.get(i);
         if (!stack.isEmpty()) {
            Block.popResource(this.level(), this.blockPosition().above(), stack);
            this.handItems.set(i, ItemStack.EMPTY);
         }
      }

      for (int ix = 0; ix < this.armorItems.size(); ix++) {
         ItemStack stack = (ItemStack)this.armorItems.get(ix);
         if (!stack.isEmpty()) {
            Block.popResource(this.level(), this.blockPosition().above(), stack);
            this.armorItems.set(ix, ItemStack.EMPTY);
         }
      }
   }

   private void playBrokenSound() {
      this.level().playSound(null, this.blockPosition(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0F, 1.0F);
   }

   private void showBreakingParticles() {
      if (this.level() instanceof ServerLevel serverLevel) {
         BlockParticleOption particleOption = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState());
         serverLevel.sendParticles(
            particleOption,
            this.getX(),
            this.getY(0.6666666666666666),
            this.getZ(),
            10,
            this.getBbWidth() / 4.0F,
            this.getBbHeight() / 4.0F,
            this.getBbWidth() / 4.0F,
            0.05
         );
      }
   }

   private boolean setEntityOnShoulder(CompoundTag tag) {
      if (this.canEntityOnShoulder()) {
         this.setShoulderEntity(tag);
         this.timeEntitySatOnShoulder = this.level().getGameTime();
         return true;
      } else {
         return false;
      }
   }

   private void removeEntitiesOnShoulder() {
      if (this.timeEntitySatOnShoulder + 20L < this.level().getGameTime()) {
         this.respawnEntityOnShoulder(this.getShoulderEntity());
         this.setShoulderEntity(new CompoundTag());
      }
   }

   private void respawnEntityOnShoulder(CompoundTag tag) {
      if (this.level() instanceof ServerLevel serverLevel && !tag.isEmpty()) {
         ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, this.level().registryAccess(), tag);
         EntityType.create(input, this.level(), new EntitySpawnRequest(EntitySpawnReason.LOAD, false)).ifPresent(entity -> {
            entity.setPos(this.getX(), this.getY() + 1.675, this.getZ());
            serverLevel.addWithUUID(entity);
         });
      }
   }

   public void addAdditionalSaveData(ValueOutput tag) {
      super.addAdditionalSaveData(tag);
      tag.putChild("HandItems", new ItemStackHandler(this.handItems));
      tag.putChild("ArmorItems", new ItemStackHandler(this.armorItems));
      if (!this.getShoulderEntity().isEmpty()) {
         tag.store("ShoulderEntity", CompoundTag.CODEC, this.getShoulderEntity());
      }
   }

   public void readAdditionalSaveData(ValueInput tag) {
      super.readAdditionalSaveData(tag);
      if (tag.keySet().contains("HandItems")) {
         ItemStackHandler handler = new ItemStackHandler(this.handItems);
         tag.readChild("HandItems", handler);

         for (int i = 0; i < this.handItems.size(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
               this.handItems.set(i, stack);
            }
         }
      }

      if (tag.keySet().contains("ArmorItems")) {
         ItemStackHandler handler = new ItemStackHandler(this.armorItems);
         tag.readChild("ArmorItems", handler);

         for (int ix = 0; ix < this.armorItems.size(); ix++) {
            ItemStack stack = handler.getStackInSlot(ix);
            if (!stack.isEmpty()) {
               this.armorItems.set(ix, stack);
            }
         }
      }

      if (tag.keySet().contains("ShoulderEntity")) {
         this.setShoulderEntity(tag.read("ShoulderEntity", CompoundTag.CODEC).orElse(new CompoundTag()));
      }
   }

   protected float tickHeadTurn(float yRot, float animStep) {
      this.yBodyRotO = this.yRotO;
      this.yBodyRot = this.getYRot();
      return 0.0F;
   }

   public void setYBodyRot(float offset) {
      this.yBodyRotO = this.yRotO = offset;
      this.yHeadRotO = this.yHeadRot = offset;
   }

   public void setYHeadRot(float rotation) {
      this.yBodyRotO = this.yRotO = rotation;
      this.yHeadRotO = this.yHeadRot = rotation;
   }

   public void kill() {
      if (!this.getShoulderEntity().isEmpty()) {
         this.removeEntitiesOnShoulder();
      }

      this.remove(RemovalReason.KILLED);
      this.gameEvent(GameEvent.ENTITY_DIE);
   }

   public boolean isPushable() {
      return false;
   }

   protected void doPush(Entity entity) {
   }

   protected void pushEntities() {
      for (Entity entity : this.level().getEntities(this, this.getBoundingBox(), RIDABLE_MINECARTS)) {
         if (this.distanceToSqr(entity) <= 0.2) {
            entity.push(this);
            return;
         }
      }

      if (this.canEntityOnShoulder()) {
         for (Entity entityx : this.level().getEntities(this, this.getBoundingBox().inflate(2.0), SHOULDER_RIDING_ENTITY)) {
            if (this.distanceToSqr(entityx) <= 1.5 && entityx instanceof ShoulderRidingEntity shoulderEntity && this.setEntityOnShoulder(shoulderEntity)) {
               return;
            }
         }
      }
   }

   private boolean setEntityOnShoulder(ShoulderRidingEntity entity) {
      String id = entity.getEncodeId();
      if (id == null) {
         return false;
      } else {
         TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
         output.putString("id", id);
         entity.saveWithoutId(output);
         CompoundTag tag = output.buildResult();
         if (this.setEntityOnShoulder(tag)) {
            entity.discard();
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean canEntityOnShoulder() {
      return !this.isPassenger() && this.onGround() && !this.isInWater() && !this.isInPowderSnow && this.getShoulderEntity().isEmpty();
   }

   public Iterable<ItemStack> getHandSlots() {
      return this.handItems;
   }

   public Iterable<ItemStack> getArmorSlots() {
      return this.armorItems;
   }

   public ItemStack getItemBySlot(EquipmentSlot slot) {
      return switch (slot.getType()) {
         case HAND -> (ItemStack)this.handItems.get(slot.getIndex());
         case HUMANOID_ARMOR -> (ItemStack)this.armorItems.get(slot.getIndex());
         default -> ItemStack.EMPTY;
      };
   }

   public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
      switch (slot.getType()) {
         case HAND:
            this.onEquipItem(slot, (ItemStack)this.handItems.set(slot.getIndex(), stack), stack);
            break;
         case HUMANOID_ARMOR:
            this.onEquipItem(slot, (ItemStack)this.armorItems.set(slot.getIndex(), stack), stack);
      }
   }

   public boolean skipAttackInteraction(Entity entity) {
      return entity instanceof Player player && !this.level().mayInteract(player, this.blockPosition());
   }

   public HumanoidArm getMainArm() {
      return HumanoidArm.RIGHT;
   }

   public Fallsounds getFallSounds() {
      return new Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
   }

   @Nullable
   protected SoundEvent getHurtSound(DamageSource damageSource) {
      return SoundEvents.ARMOR_STAND_HIT;
   }

   @Nullable
   protected SoundEvent getDeathSound() {
      return SoundEvents.ARMOR_STAND_BREAK;
   }

   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
   }

   public boolean isAffectedByPotions() {
      return false;
   }

   public boolean attackable() {
      return false;
   }

   public ItemStack getPickResult() {
      return new ItemStack((ItemLike)ModItems.SCARECROW.get());
   }

   public CompoundTag getShoulderEntity() {
      return this.shoulderEntity;
   }

   public void setShoulderEntity(CompoundTag tag) {
      this.shoulderEntity = tag.copy();
   }
}
