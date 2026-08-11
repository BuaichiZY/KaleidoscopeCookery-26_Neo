package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IPot;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra.TetraCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexPotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BaseRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityEvaluator;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PotBlockEntity extends BaseBlockEntity implements IPot {
   private static final int PUT_INGREDIENT_TIME = 1200;
   private static final int TAKEOUT_TIME = 800;
   private static final int BURNT_TIME = 400;
   private static final String INPUTS = "Inputs";
   private static final String CARRIER = "Carrier";
   private static final String RESULT = "Result";
   private static final String STATUS = "Status";
   private static final String CURRENT_TICK = "CurrentTick";
   private static final String STIR_FRY_COUNT = "StirFryCount";
   private static final String SEED = "Seed";
   public long seed;
   public PotBlockEntity.StirFryAnimationData animationData = new PotBlockEntity.StirFryAnimationData();
   private NonNullList<ItemStack> inputs = NonNullList.withSize(9, ItemStack.EMPTY);
   private Ingredient carrier = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
   private ItemStack result = ItemStack.EMPTY;
   private int status = 0;
   private int currentTick = 0;
   private int stirFryCount = 0;

   public PotBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(ModBlocks.POT_BE.get(), pPos, pBlockState);
      this.seed = System.currentTimeMillis();
   }

   @Override
   public boolean hasHeatSource(Level level) {
      BlockState belowState = level.getBlockState(this.worldPosition.below());
      return belowState.hasProperty(BlockStateProperties.LIT)
         ? (Boolean)belowState.getValue(BlockStateProperties.LIT)
         : belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT);
   }

   public void tick(Level level) {
      if (this.hasHeatSource(level)) {
         RandomSource random = level.getRandom();
         if (this.currentTick > 0) {
            this.currentTick--;
            if (this.currentTick % 5 == 0) {
               this.refresh();
            }

            if (this.currentTick % 20 == 0) {
               level.playSound(
                  null, this.worldPosition, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.5F + random.nextFloat() / 0.5F, 0.8F + random.nextFloat() / 0.5F
               );
            }
         }

         if (this.status == 0) {
            this.tickPutIngredient(level, random);
         } else if (this.status == 1) {
            this.tickCooking(level, random);
         } else if (this.status == 2) {
            this.tickFinished(random);
         } else {
            if (this.status == 3) {
               this.tickBurnt(level, random);
            }
         }
      }
   }

   private void tickBurnt(Level level, RandomSource random) {
      int particleCount = 10 - this.currentTick / 5;
      if (this.currentTick % 2 == 0 && this.level instanceof ServerLevel serverLevel) {
         serverLevel.sendParticles(
            ParticleTypes.SMOKE,
            this.worldPosition.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            this.worldPosition.getY() + 0.25 + random.nextDouble() / 3.0,
            this.worldPosition.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            particleCount,
            0.0,
            0.0,
            0.0,
            0.05
         );
      }

      if (this.currentTick == 0) {
         this.reset();
         level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.8F);
         if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
               ParticleTypes.SMOKE,
               this.worldPosition.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
               this.worldPosition.getY() + 0.25 + random.nextDouble() / 3.0,
               this.worldPosition.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
               8,
               0.0,
               0.0,
               0.0,
               0.05
            );
            int count = 1 + random.nextInt(3);
            Block.popResource(level, this.worldPosition, new ItemStack(Items.CHARCOAL, count));
         }
      }
   }

   private void tickFinished(RandomSource random) {
      if (this.currentTick % 10 == 0 && this.level instanceof ServerLevel serverLevel) {
         serverLevel.sendParticles(
            (SimpleParticleType)ModParticles.COOKING.get(),
            this.worldPosition.getX() + 0.5,
            this.worldPosition.getY() + 0.1 + random.nextDouble() / 2.0,
            this.worldPosition.getZ() + 0.5,
            1,
            0.0,
            0.0,
            0.0,
            0.0
         );
      }

      if (this.currentTick == 0) {
         this.status = 3;
         this.currentTick = 400;
         this.setChanged();
      }
   }

   private void tickCooking(Level level, RandomSource random) {
      if (this.currentTick == 0) {
         level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.8F);
         this.status = 2;
         if (this.stirFryCount > 0) {
            this.result = FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY).getDefaultInstance();
            this.carrier = Ingredient.of(new ItemLike[]{Items.BOWL});
         }

         this.currentTick = 800;
         this.setChanged();
         BlockState state = level.getBlockState(this.worldPosition);
         level.setBlockAndUpdate(this.worldPosition, (BlockState)state.setValue(PotBlock.SHOW_OIL, false));
      }
   }

   private void tickPutIngredient(Level level, RandomSource random) {
      if (this.currentTick % 10 == 0 && level instanceof ServerLevel serverLevel) {
         serverLevel.sendParticles(
            (SimpleParticleType)ModParticles.COOKING.get(),
            this.worldPosition.getX() + 0.5 + random.nextDouble() / 5.0 * (random.nextBoolean() ? 1 : -1),
            this.worldPosition.getY() + 0.1 + random.nextDouble() / 3.0,
            this.worldPosition.getZ() + 0.5 + random.nextDouble() / 5.0 * (random.nextBoolean() ? 1 : -1),
            1,
            0.0,
            0.0,
            0.0,
            0.0
         );
      }

      if (this.currentTick == 0) {
         if (this.isEmpty()) {
            this.reset();
            level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.8F);
            if (this.level instanceof ServerLevel serverLevel) {
               serverLevel.sendParticles(
                  (SimpleParticleType)ModParticles.COOKING.get(),
                  this.worldPosition.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                  this.worldPosition.getY() + 0.1 + random.nextDouble() / 3.0,
                  this.worldPosition.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                  8,
                  0.0,
                  0.0,
                  0.0,
                  0.05
               );
            }
         } else {
            this.startCooking(level);
         }
      }
   }

   @Override
   public boolean onPlaceOil(Level level, LivingEntity user, ItemStack stack) {
      if (stack.is(TagMod.OIL)) {
         this.placeOil(level, user, level.getRandom());
         stack.shrink(1);
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "put_oil_in_pot");
         return true;
      } else if (stack.is((Item)ModItems.KITCHEN_SHOVEL.get()) && KitchenShovelItem.hasOil(stack)) {
         this.placeOil(level, user, level.getRandom());
         KitchenShovelItem.setHasOil(stack, false);
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "put_oil_in_pot");
         return true;
      } else if (stack.is((Item)ModItems.OIL_POT.get()) && OilPotItem.hasOil(stack)) {
         this.placeOil(level, user, level.getRandom());
         OilPotItem.shrinkOilCount(stack);
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "put_oil_in_pot");
         return true;
      } else {
         return false;
      }
   }

   private void placeOil(Level level, LivingEntity user, RandomSource random) {
      this.currentTick = 1200;
      BlockState state = level.getBlockState(this.worldPosition);
      level.setBlockAndUpdate(this.worldPosition, (BlockState)((BlockState)state.setValue(PotBlock.HAS_OIL, true)).setValue(PotBlock.SHOW_OIL, true));
      level.playSound(user, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.8F);

      for (int i = 0; i < 10; i++) {
         level.addParticle(
            ParticleTypes.SMOKE,
            this.worldPosition.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            this.worldPosition.getY() + 0.25 + random.nextDouble() / 3.0,
            this.worldPosition.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            0.0,
            0.05,
            0.0
         );
      }
   }

   @Override
   public void onShovelHit(Level level, LivingEntity user, ItemStack shovel) {
      if (!level.isClientSide()) {
         this.seed = System.currentTimeMillis();
         this.refresh();
      }

      if (this.level instanceof ServerLevel serverLevel) {
         RandomSource random = serverLevel.getRandom();
         serverLevel.sendParticles(
            (SimpleParticleType)ModParticles.COOKING.get(),
            this.worldPosition.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            this.worldPosition.getY() + 0.1 + random.nextDouble() / 3.0,
            this.worldPosition.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            1,
            0.0,
            0.0,
            0.0,
            0.05
         );
      }

      if (this.status == 0 && !this.isEmpty()) {
         this.startCooking(level);
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "stir_fry_in_pot");
      }

      if (this.status == 1 && this.stirFryCount > 0) {
         this.stirFryCount--;
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "stir_fry_in_pot");
      }
   }

   private void startCooking(Level level) {
      if (!(level instanceof ServerLevel serverLevel)) {
         return;
      }

      SimpleInput simpleInput = new SimpleInput(this.inputs);
      RecipeManager manager = serverLevel.recipeAccess();
      Optional<RecipeHolder<PotRecipe>> potRecipe = manager.getRecipeFor(ModRecipes.POT_RECIPE, simpleInput, serverLevel);
      if (potRecipe.isPresent()) {
         this.applyRecipe(level, simpleInput, potRecipe.get());
      } else {
         Optional<RecipeHolder<FlexPotRecipe>> flexPotRecipe = manager.getRecipeFor(ModRecipes.FLEX_POT_RECIPE, simpleInput, serverLevel);
         if (flexPotRecipe.isPresent()) {
            this.applyFlexRecipe(level, simpleInput, flexPotRecipe.get());
         } else {
            this.applySuspiciousRecipe();
         }
      }
   }

   private void applyRecipe(Level level, SimpleInput input, RecipeHolder<PotRecipe> recipe) {
      PotRecipe value = (PotRecipe)recipe.value();
      this.carrier = value.carrier();
      this.result = value.assemble(input);
      this.currentTick = value.time();
      this.stirFryCount = value.stirFryCount();
      this.status = 1;
      this.refresh();
   }

   private void applyFlexRecipe(Level level, SimpleInput input, RecipeHolder<FlexPotRecipe> recipe) {
      FlexPotRecipe value = (FlexPotRecipe)recipe.value();
      this.carrier = value.carrier();
      this.result = value.assemble(input);
      this.currentTick = value.time();
      this.stirFryCount = value.stirFryCount();
      if (level instanceof ServerLevel serverLevel) {
         Quality quality = QualityEvaluator.evaluate(this.inputs, value.ingredients(), recipe.id().identifier(), serverLevel.getSeed());
         QualityUtils.setQuality(this.result, quality);
      }

      this.status = 1;
      this.refresh();
   }

   private void applySuspiciousRecipe() {
      this.carrier = Ingredient.of(new ItemLike[]{Items.BOWL});
      this.result = FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY).getDefaultInstance();
      this.currentTick = 200;
      this.stirFryCount = 0;
      this.status = 1;
      this.refresh();
   }

   @Override
   public boolean takeOutProduct(Level level, LivingEntity user, ItemStack stack) {
      if (this.status != 2 && this.status != 3) {
         return false;
      } else {
         ItemStack finallyResult = this.status == 2 ? this.result : FoodBiteRegistry.getItem(FoodBiteRegistry.DARK_CUISINE).getDefaultInstance();
         return !ModRecipes.isEmptyIngredient(this.carrier)
            ? this.takeOutWithCarrier(level, user, stack, finallyResult)
            : this.takeOutWithoutCarrier(level, user, stack, finallyResult);
      }
   }

   private boolean takeOutWithoutCarrier(Level level, LivingEntity user, ItemStack stack, ItemStack finallyResult) {
      if (stack.is(TagMod.KITCHEN_SHOVEL)) {
         if (user instanceof Player player && !player.isSecondaryUseActive()) {
            return false;
         } else {
            ItemUtils.getItemToLivingEntity(user, finallyResult);
            this.reset();
            return true;
         }
      } else {
         if (this.hasHeatSource(level)) {
            user.hurt(level.damageSources().inFire(), 1.0F);
            ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "hurt_when_takeout_from_pot");
         }

         this.sendActionBarMessage(user, "need_kitchen_shovel");
         return false;
      }
   }

   private boolean takeOutWithCarrier(Level level, LivingEntity user, ItemStack mainHandItem, ItemStack finallyResult) {
      Component carrierName = BaseRecipe.displayStack(this.carrier).getHoverName();
      if (this.carrier.test(mainHandItem)) {
         if (mainHandItem.getCount() < finallyResult.getCount()) {
            this.sendActionBarMessage(user, "carrier_count_not_enough", finallyResult.getCount(), carrierName);
            return false;
         } else {
            mainHandItem.shrink(finallyResult.getCount());
            ItemUtils.getItemToLivingEntity(user, finallyResult);
            this.reset();
            return true;
         }
      } else {
         if (!mainHandItem.is(TagMod.KITCHEN_SHOVEL)) {
            if (this.hasHeatSource(level)) {
               user.hurt(level.damageSources().inFire(), 1.0F);
               ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "hurt_when_takeout_from_pot");
            }

            this.sendActionBarMessage(user, "need_carrier", carrierName);
         }

         return false;
      }
   }

   private void sendActionBarMessage(LivingEntity user, String type, Object... args) {
      if (user instanceof ServerPlayer serverPlayer) {
         String key = "tip.kaleidoscope_cookery.pot." + type;
         MutableComponent message = Component.translatable(key, args);
         serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
      }
   }

   public void addAllIngredients(List<ItemStack> ingredients, LivingEntity user) {
      if (this.level != null) {
         if (this.status == 0) {
            for (int i = 0; i < Math.min(ingredients.size(), this.inputs.size()); i++) {
               ItemStack stack = ingredients.get(i);
               if (!stack.isEmpty()) {
                  Item containerItem = ItemUtils.getContainerItem(stack);
                  if (containerItem != Items.AIR) {
                     ItemUtils.getItemToLivingEntity(user, containerItem.getDefaultInstance());
                  }

                  this.inputs.set(i, stack.copyWithCount(1));
               }
            }

            this.level.playSound(null, this.worldPosition, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
            this.refresh();
         }
      }
   }

   @Override
   public boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack) {
      if (this.status != 0) {
         return false;
      } else if (itemStack.is(TagMod.INGREDIENT_BLOCKLIST)) {
         return false;
      } else if (TetraCompat.isModularItem(itemStack)) {
         return false;
      } else {
         for (int i = 0; i < this.inputs.size(); i++) {
            ItemStack item = (ItemStack)this.inputs.get(i);
            if (item.isEmpty()) {
               Item containerItem = ItemUtils.getContainerItem(itemStack);
               if (containerItem != Items.AIR) {
                  ItemUtils.getItemToLivingEntity(user, containerItem.getDefaultInstance());
               }

               this.inputs.set(i, itemStack.split(1));
               level.playSound(null, this.worldPosition, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
               this.refresh();
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean removeIngredient(Level level, LivingEntity user) {
      if (this.status != 0) {
         return false;
      } else {
         for (int i = this.inputs.size() - 1; i >= 0; i--) {
            ItemStack stack = (ItemStack)this.inputs.get(i);
            if (!stack.isEmpty()) {
               if (!this.containerIsMatch(user, stack)) {
                  return false;
               }

               this.inputs.set(i, ItemStack.EMPTY);
               ItemUtils.getItemToLivingEntity(user, stack);
               if (this.hasHeatSource(level)) {
                  user.hurt(level.damageSources().inFire(), 1.0F);
                  ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "hurt_when_takeout_from_pot");
               }

               this.refresh();
               return true;
            }
         }

         return false;
      }
   }

   private boolean containerIsMatch(LivingEntity user, ItemStack stack) {
      Item containerItem = ItemUtils.getContainerItem(stack);
      if (containerItem == Items.AIR) {
         return true;
      } else if (user.getMainHandItem().is(containerItem)) {
         user.getMainHandItem().shrink(1);
         return true;
      } else {
         if (user instanceof ServerPlayer player) {
            player.sendSystemMessage(
               Component.translatable(
                  "tip.kaleidoscope_cookery.kitchen.remove_ingredient.need_container", new Object[]{containerItem.getDefaultInstance().getHoverName()}
               )
            );
         }

         return false;
      }
   }

   public void reset() {
      this.inputs.clear();
      this.carrier = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
      this.result = ItemStack.EMPTY;
      this.status = 0;
      this.currentTick = 0;
      this.stirFryCount = 0;
      this.setChanged();
      if (this.level != null) {
         BlockState state = this.level.getBlockState(this.worldPosition);
         this.level.setBlockAndUpdate(this.worldPosition, (BlockState)((BlockState)state.setValue(PotBlock.HAS_OIL, false)).setValue(PotBlock.SHOW_OIL, false));
      }
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      ContainerHelper.saveAllItems(tag.child("Inputs"), this.inputs);
      tag.store("Carrier", Ingredient.CODEC, this.carrier);
      tag.store("Result", ItemStack.OPTIONAL_CODEC, this.result);
      tag.putInt("Status", this.status);
      tag.putInt("CurrentTick", this.currentTick);
      tag.putInt("StirFryCount", this.stirFryCount);
      tag.putLong("Seed", this.seed);
   }

   protected void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      this.inputs = NonNullList.withSize(9, ItemStack.EMPTY);
      if (tag.keySet().contains("Inputs")) {
         ContainerHelper.loadAllItems(tag.childOrEmpty("Inputs"), this.inputs);
      }

      if (tag.keySet().contains("Carrier")) {
         this.carrier = tag.read("Carrier", Ingredient.CODEC).orElse(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT);
      }

      if (tag.keySet().contains("Result")) {
         this.result = tag.read("Result", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      }

      this.status = tag.getIntOr("Status", 0);
      this.currentTick = tag.getIntOr("CurrentTick", 0);
      this.stirFryCount = tag.getIntOr("StirFryCount", 0);
      this.seed = tag.getLongOr("Seed", 0L);
   }

   public List<ItemStack> getInputs() {
      return this.inputs;
   }

   public SimpleInput getContainer() {
      return new SimpleInput(this.inputs);
   }

   public boolean isEmpty() {
      for (ItemStack stack : this.inputs) {
         if (!stack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public int getStatus() {
      return this.status;
   }

   public boolean hasCarrier() {
      return !ModRecipes.isEmptyIngredient(this.carrier);
   }

   public ItemStack getResult() {
      return this.result;
   }

   public long getSeed() {
      return this.seed;
   }

   public int getCurrentTick() {
      return this.currentTick;
   }

   public static class StirFryAnimationData {
      public long preSeed = -1L;
      public long timestamp = -1L;
      public float[] randomHeights = new float[0];
   }
}
