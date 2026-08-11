package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IMillstone;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.MillstoneMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.MillstoneRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.MillstoneBindableData;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.resources.MillstoneBindableDataReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

public class MillstoneBlockEntity extends BaseBlockEntity implements IMillstone {
   public static final int MAX_INPUT_COUNT = 8;
   private static final String ENTITY_ID_KEY = "EntityId";
   private static final String CACHE_ROT_KEY = "CacheRot";
   private static final String ROT_SPEED_TICK_KEY = "RotSpeedTick";
   private static final String LIFT_ANGLE_KEY = "LiftAngle";
   private static final String INPUT_ITEM_KEY = "InputItem";
   private static final String PREVIEW_ITEM_KEY = "PreviewItem";
   private static final String OUTPUT_ITEM_KEY = "OutputItem";
   private static final String PROGRESS_KEY = "Progress";
   private final CachedCheck<SimpleInput, MillstoneRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.MILLSTONE_RECIPE);
   private final ItemStackHandler outputs = new ItemStackHandler(4) {
      protected void onContentsChanged(int slot) {
         MillstoneBlockEntity.this.refresh();
      }
   };
   private ItemStack input = ItemStack.EMPTY;
   private ItemStack previewOutput = ItemStack.EMPTY;
   private UUID entityId = Util.NIL_UUID;
   private float cacheRot = 0.0F;
   private float rotSpeedTick = 200.0F;
   private float liftAngle = 5.0F;
   private int progress = 0;
   @Nullable
   private Mob bindEntity;
   private Vec3 offset = Vec3.ZERO;

   public MillstoneBlockEntity(BlockPos pos, BlockState state) {
      super(ModBlocks.MILLSTONE_BE.get(), pos, state);
   }

   public float getRotation(Level level, float partialTick) {
      float degPerTick = 360.0F / Math.max(this.rotSpeedTick, 1.0F);
      float gameTime = (float)level.getGameTime() + partialTick;
      return Math.abs(this.cacheRot + gameTime * degPerTick) % 360.0F;
   }

   public void tick(Level level) {
      if (this.level instanceof ServerLevel serverLevel) {
         if (!Util.NIL_UUID.equals(this.entityId)) {
            if (serverLevel.getGameTime() % 20L == 9L && !this.isOutputEmpty()) {
               Direction direction = (Direction)this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
               BlockPos outputPos = this.worldPosition.relative(direction);

               for (int i = 0; i < this.outputs.getSlots(); i++) {
                  ItemStack outputStack = this.outputs.getStackInSlot(i);
                  if (!outputStack.isEmpty()) {
                     ItemEntity entity = new ItemEntity(
                        serverLevel, outputPos.getX() + 0.5, outputPos.getY(), outputPos.getZ() + 0.5, outputStack, 0.0, 0.0, 0.0
                     );
                     entity.setDefaultPickUpDelay();
                     serverLevel.addFreshEntity(entity);
                  }
               }

               this.resetWhenTakeout();
            }

            float rot = this.getRotation(level, 0.0F);
            Vec3 center = Vec3.atBottomCenterOf(this.getBlockPos());
            double maxDistanceSqr = 25.0;
            if (this.bindEntity == null) {
               if (!(serverLevel.getEntity(this.entityId) instanceof Mob mob)
                  || !mob.isAlive()
                  || !(mob.distanceToSqr(center) < maxDistanceSqr)
                  || !this.canBindEntity(mob)) {
                  this.entityId = Util.NIL_UUID;
                  this.cacheRot = 0.0F;
                  this.liftAngle = 0.0F;
                  this.refresh();
                  return;
               }

               this.bindEntity(mob);
            } else if (!this.bindEntity.isAlive()
               || this.bindEntity.distanceToSqr(center) >= maxDistanceSqr
               || this.bindEntity.fallDistance > 0.5F
               || this.bindEntity.isInWall()
               || this.saddleEntityIsControlling(this.bindEntity)) {
               this.entityId = Util.NIL_UUID;
               this.bindEntity = null;
               this.cacheRot = rot;
               this.liftAngle = 0.0F;
               this.refresh();
               return;
            }

            Vec3 pos = new Vec3(0.0, 0.0, 2.0).add(this.offset).yRot(rot * (float) (Math.PI / 180.0)).add(center);
            this.bindEntity.setPos(pos);
            this.bindEntity.setYRot(-rot - 90.0F);
            this.bindEntity.setXRot(0.0F);
            if (this.bindEntity.tickCount % 10 == 0 && this.isOutputEmpty() && this.input.isEmpty() && this.progress <= 0) {
               ResourceHandler<ItemResource> resourceHandler = this.bindEntity.getCapability(Capabilities.Item.ENTITY);
               if (resourceHandler != null) {
                  IItemHandler handler = IItemHandler.of(resourceHandler);
                  for (int ix = 0; ix < handler.getSlots(); ix++) {
                     ItemStack stackInSlot = handler.getStackInSlot(ix);
                     if (!stackInSlot.isEmpty()) {
                        ItemStack stack = handler.extractItem(ix, 8, true);
                        if (this.onPutItem(level, stack)) {
                           handler.extractItem(ix, 8, false);
                           return;
                        }
                     }
                  }
               }

               BlockPos above = this.worldPosition.above();
               Vec3 startPos = new Vec3(above.getX() - 0.3125, above.getY(), above.getZ() - 0.3125);
               Vec3 endPos = new Vec3(above.getX() + 1.3125, above.getY() + 0.5, above.getZ() + 1.3125);
               AABB aabb = new AABB(startPos, endPos);

               for (ItemEntity itemEntity : serverLevel.getEntitiesOfClass(ItemEntity.class, aabb)) {
                  ItemStack stack = itemEntity.getItem();
                  if (!stack.isEmpty()) {
                     int countCanInsert = Math.min(stack.getCount(), 8);
                     ItemStack stackToInsert = stack.copyWithCount(countCanInsert);
                     if (this.onPutItem(level, stackToInsert)) {
                        stack.shrink(countCanInsert);
                        if (stack.isEmpty()) {
                           itemEntity.discard();
                        } else {
                           itemEntity.setItem(stack);
                        }
                        break;
                     }
                  }
               }
            }

            if (serverLevel.getGameTime() % 5L == 2L) {
               Item item = !this.isOutputEmpty() ? this.outputs.getStackInSlot(0).getItem() : (!this.input.isEmpty() ? this.input.getItem() : Items.AIR);
               if (item != Items.AIR) {
                  Vec3 particlePos = new Vec3(0.0, 1.0, 1.0).yRot(rot * (float) (Math.PI / 180.0)).add(center);
                  if (item instanceof BlockItem blockItem) {
                     BlockState block = blockItem.getBlock().defaultBlockState();
                     BlockParticleOption option = new BlockParticleOption(ParticleTypes.BLOCK, block);
                     serverLevel.sendParticles(option, particlePos.x, particlePos.y, particlePos.z, 5, 0.1, 0.1, 0.1, 0.05);
                  } else {
                     ItemParticleOption option = new ItemParticleOption(ParticleTypes.ITEM, item);
                     serverLevel.sendParticles(option, particlePos.x, particlePos.y, particlePos.z, 5, 0.1, 0.1, 0.1, 0.05);
                  }
               }
            }

            if (serverLevel.getGameTime() % 25L == 0L) {
               float pitch = level.getRandom().nextFloat() * 0.2F + 0.9F;
               serverLevel.playSound(null, this.worldPosition, (SoundEvent)ModSounds.BLOCK_MILLSTONE.get(), SoundSource.BLOCKS, 0.5F, pitch);
            }

            if (this.progress > 0 && this.isOutputEmpty()) {
               this.progress--;
               if (this.progress % 10 == 0) {
                  this.refresh();
               }
            }

            if (this.progress <= 0 && !this.input.isEmpty() && this.isOutputEmpty()) {
               SimpleInput simpleInput = new SimpleInput(List.of(this.input));
               this.matchRecipe(simpleInput, level)
                  .ifPresentOrElse(
                     recipe -> {
                        for (int ixx = 0; ixx < this.input.getCount(); ixx++) {
                           ((MillstoneRecipe)recipe.value())
                              .results()
                              .stream()
                              .filter(output -> !output.isEmpty())
                              .filter(output -> Math.random() < output.chance())
                              .map(RandomOutput::stack)
                              .forEach(stackx -> ItemHandlerHelper.insertItemStacked(this.outputs, stackx.copy(), false));
                        }

                        this.input = ItemStack.EMPTY;
                        this.previewOutput = ItemStack.EMPTY;
                        this.refresh();
                     },
                     () -> {
                        this.outputs.setStackInSlot(0, this.input.copyAndClear());
                        this.input = ItemStack.EMPTY;
                        this.previewOutput = ItemStack.EMPTY;
                        this.refresh();
                     }
                  );
            }
         }
      }
   }

   @Override
   public boolean onPutItem(Level level, ItemStack putOnItem) {
      if (!this.isOutputEmpty()) {
         return false;
      } else if (this.progress > 0 && !this.input.isEmpty()) {
         return false;
      } else {
         SimpleInput simpleInput = new SimpleInput(List.of(putOnItem));
         return this.matchRecipe(simpleInput, level).map(recipe -> {
            this.input = putOnItem.split(8);
            this.previewOutput = ((MillstoneRecipe)recipe.value())
               .results()
               .stream()
               .filter(output -> !output.isEmpty())
               .map(RandomOutput::stack)
               .findFirst()
               .map(stack -> {
                  ItemStack preview = stack.copy();
                  int previewCount = Math.max(1, preview.getCount()) * Math.max(1, this.input.getCount());
                  preview.setCount(Math.min(preview.getMaxStackSize(), previewCount));
                  return preview;
               })
               .orElse(ItemStack.EMPTY);
            this.progress = Math.max(Math.round(this.rotSpeedTick), 1);
            this.refresh();
            level.playSound(null, this.worldPosition, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.8F, level.getRandom().nextFloat() * 0.2F + 0.9F);
            return true;
         }).orElse(false);
      }
   }

   public void resetWhenTakeout() {
      for (int i = 0; i < this.outputs.getSlots(); i++) {
         if (!this.outputs.getStackInSlot(i).isEmpty()) {
            this.outputs.setStackInSlot(i, ItemStack.EMPTY);
         }
      }

      this.progress = 0;
      this.previewOutput = ItemStack.EMPTY;
      this.refresh();
   }

   public boolean saddleEntityIsControlling(Mob mob) {
      return mob.isSaddled() && mob.getControllingPassenger() != null;
   }

   public boolean canBindEntity(Mob mob) {
      if (!mob.getType().builtInRegistryHolder().is(TagMod.MILLSTONE_BINDABLE)
         && !MillstoneBindableDataReloadListener.INSTANCE.containsKey(mob.getType())) {
         return false;
      } else if (mob.getVehicle() != null) {
         return false;
      } else if (mob.isBaby()) {
         return false;
      } else if (this.saddleEntityIsControlling(mob)) {
         return false;
      } else if (mob instanceof AbstractHorse horse) {
         return horse.isTamed();
      } else if (mob instanceof TamableAnimal tamableAnimal) {
         return tamableAnimal.isTame();
      } else {
         return mob instanceof OwnableEntity ownable ? ownable.getOwnerReference() != null : true;
      }
   }

   public void bindEntity(Mob mob) {
      if (this.level != null && !this.level.isClientSide()) {
         if (mob.isAlive()) {
            this.entityId = mob.getUUID();
            this.bindEntity = mob;
            float rot = this.getRotation(this.level, 0.0F);
            this.cacheRot = this.fixRot(this.cacheRot - (rot - this.cacheRot));
            MillstoneBindableData data = MillstoneBindableDataReloadListener.INSTANCE.getOrDefault(mob.getType(), MillstoneBindableData.DEFAULT);
            this.rotSpeedTick = data.rotSpeedTick();
            this.liftAngle = data.liftAngle();
            this.offset = data.offset();
            this.refresh();
         }
      }
   }

   public Optional<RecipeHolder<MillstoneRecipe>> matchRecipe(SimpleInput input, Level level) {
      RecipeHolder<MillstoneRecipe> recipe = MillstoneRecipeSerializer.getEmptyRecipe();
      MillstoneMatchRecipeEvent.Pre preEvent = new MillstoneMatchRecipeEvent.Pre(level, this, input);
      NeoForge.EVENT_BUS.post(preEvent);
      if (preEvent.getOutput() != null) {
         recipe = preEvent.getOutput();
      } else {
         Optional<RecipeHolder<MillstoneRecipe>> opt = level instanceof ServerLevel serverLevel
            ? this.quickCheck.getRecipeFor(input, serverLevel)
            : Optional.empty();
         if (opt.isPresent()) {
            recipe = opt.orElseThrow();
         }
      }

      MillstoneMatchRecipeEvent.Post postEvent = new MillstoneMatchRecipeEvent.Post(level, this, input, recipe);
      NeoForge.EVENT_BUS.post(postEvent);
      if (postEvent.getOutput() != null) {
         recipe = postEvent.getOutput();
      }

      return recipe.id().identifier().equals(MillstoneRecipeSerializer.EMPTY_ID) ? Optional.empty() : Optional.of(recipe);
   }

   public void sendActionBarMessage(LivingEntity user, String key, Object... args) {
      if (user instanceof ServerPlayer serverPlayer) {
         MutableComponent message = Component.translatable(key, args);
         serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
      }
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.store("EntityId", UUIDUtil.CODEC, this.entityId);
      tag.putFloat("CacheRot", this.fixRot(this.cacheRot));
      tag.putFloat("RotSpeedTick", this.rotSpeedTick);
      tag.putFloat("LiftAngle", this.liftAngle);
      tag.store("InputItem", ItemStack.OPTIONAL_CODEC, this.input);
      tag.store("PreviewItem", ItemStack.OPTIONAL_CODEC, this.previewOutput);
      tag.putChild("OutputItem", this.outputs);
      tag.putInt("Progress", this.progress);
   }

   public void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      this.entityId = tag.read("EntityId", UUIDUtil.CODEC).orElse(Util.NIL_UUID);
      this.cacheRot = this.fixRot(tag.getFloatOr("CacheRot", 0.0F));
      this.rotSpeedTick = tag.getFloatOr("RotSpeedTick", 0.0F);
      this.liftAngle = tag.getFloatOr("LiftAngle", 0.0F);
      this.input = tag.read("InputItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      this.previewOutput = tag.read("PreviewItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      if (tag.keySet().contains("OutputItem")) {
tag.readChild("OutputItem",          this.outputs);
      }

      this.progress = tag.getIntOr("Progress", 0);
   }

   public boolean hasEntity() {
      return !Util.NIL_UUID.equals(this.entityId);
   }

   public float getCacheRot() {
      return this.cacheRot;
   }

   public void setCacheRot(float cacheRot) {
      this.cacheRot = cacheRot;
   }

   public float getLiftAngle() {
      return this.liftAngle;
   }

   public ItemStack getInput() {
      return this.input;
   }

   public ItemStack getPreviewOutput() {
      return this.previewOutput;
   }

   public IItemHandler getOutputs() {
      return this.outputs;
   }

   public boolean isOutputEmpty() {
      for (int i = 0; i < this.outputs.getSlots(); i++) {
         if (!this.outputs.getStackInSlot(i).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public float getProgressPercent() {
      float total = Math.max(this.rotSpeedTick, 1.0F);
      return (total - this.progress) / total;
   }

   private float fixRot(float value) {
      return !Float.isNaN(value) && !Float.isInfinite(value) ? Math.abs(value) % 360.0F : 0.0F;
   }
}
