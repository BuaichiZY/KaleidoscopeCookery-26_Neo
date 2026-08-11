package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IStockpot;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.StockpotMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.particle.StockpotParticleOptions;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra.TetraCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BaseRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.FluidSoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityEvaluator;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.BlockDrop;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Vector3f;

public class StockpotBlockEntity extends BaseBlockEntity implements IStockpot {
   public static final int MAX_TAKEOUT_COUNT = 9;
   private static final String INPUTS = "Inputs";
   private static final String RECIPE_ID = "RecipeId";
   private static final String SOUP_BASE_ID = "SoupBaseId";
   private static final String RESULT = "Result";
   private static final String STATUS = "Status";
   private static final String CURRENT_TICK = "CurrentTick";
   private static final String TAKEOUT_COUNT = "TakeoutCount";
   private static final String LID_ITEM = "LidItem";
   private final CachedCheck<StockpotInput, StockpotRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.STOCKPOT_RECIPE);
   private final CachedCheck<StockpotInput, FlexStockpotRecipe> flexQuickCheck = RecipeManager.createCheck(ModRecipes.FLEX_STOCKPOT_RECIPE);
   @Nullable
   public StockpotVisuals visuals;
   @Nullable
   public Entity renderEntity = null;
   private NonNullList<ItemStack> inputs = NonNullList.withSize(9, ItemStack.EMPTY);
   private Identifier recipeId = StockpotRecipeSerializer.EMPTY_ID;
   private Identifier soupBaseId = ModSoupBases.WATER;
   private ItemStack result = ItemStack.EMPTY;
   private int status = 0;
   private int currentTick = -1;
   private int takeoutCount = 0;
   private ItemStack lidItem = ItemStack.EMPTY;

   public StockpotBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(ModBlocks.STOCKPOT_BE.get(), pPos, pBlockState);
   }

   public void clientTick() {
      if (this.renderEntity != null) {
         this.renderEntity.tickCount++;
      }
   }

   @Override
   public boolean hasHeatSource(Level level) {
      BlockState belowState = level.getBlockState(this.worldPosition.below());
      return belowState.hasProperty(BlockStateProperties.LIT)
         ? (Boolean)belowState.getValue(BlockStateProperties.LIT)
         : belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT);
   }

   @Override
   public boolean hasLid() {
      if (this.level == null) {
         return false;
      } else {
         BlockState blockState = this.level.getBlockState(this.worldPosition);
         return this.level != null && blockState.hasProperty(StockpotBlock.HAS_LID) && (Boolean)blockState.getValue(StockpotBlock.HAS_LID);
      }
   }

   public void tick(Level level) {
      if (this.status != 0) {
         if (this.hasHeatSource(level)) {
            boolean hasLid = this.hasLid();
            if (level.getGameTime() % 15L == 0L) {
               float volume = hasLid ? 0.075F : 0.2F;
               float pitch = hasLid ? 0.1F + level.getRandom().nextFloat() * 0.05F : 1.0F + level.getRandom().nextFloat() * 0.1F;
               level.playSound(
                  null,
                  this.worldPosition.getX() + 0.5,
                  this.worldPosition.getY() + 0.5,
                  this.worldPosition.getZ() + 0.5,
                  (SoundEvent)ModSounds.BLOCK_STOCKPOT.get(),
                  SoundSource.BLOCKS,
                  volume,
                  pitch
               );
            }

            if (!hasLid) {
               this.spawnParticleWithoutLid(level);
            } else {
               this.spawnParticleWithLid(level);
               if (this.status == 1 && level.getGameTime() % 5L == 0L && !this.isEmpty()) {
                  this.setRecipe(level);
                  this.status = 2;
                  this.refresh();
               } else {
                  if (this.status == 2) {
                     if (this.currentTick > 0) {
                        this.currentTick--;
                        return;
                     }

                     this.status = 3;
                     this.currentTick = -1;
                     this.inputs.clear();
                     this.refresh();
                  }
               }
            }
         }
      }
   }

   private void spawnParticleWithLid(Level level) {
      if (level instanceof ServerLevel serverLevel && level.getRandom().nextFloat() < 0.05F) {
         RandomSource random = serverLevel.getRandom();
         serverLevel.sendParticles(
            (SimpleParticleType)ModParticles.COOKING.get(),
            this.worldPosition.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            this.worldPosition.getY() + 0.375 + random.nextDouble() / 3.0,
            this.worldPosition.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            1,
            0.0,
            0.0,
            0.0,
            0.05
         );
      }
   }

   private void spawnParticleWithoutLid(Level level) {
      if (level instanceof ServerLevel serverLevel && serverLevel.getRandom().nextFloat() < 0.25F) {
         int color = this.getBubbleColor();
         serverLevel.sendParticles(
            new StockpotParticleOptions(
               new Vector3f((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F), 1.0F
            ),
            this.worldPosition.getX() + 0.25 + level.getRandom().nextFloat() * 0.5F,
            this.worldPosition.getY() + 0.375,
            this.worldPosition.getZ() + 0.25 + level.getRandom().nextFloat() * 0.5F,
            2,
            (level.getRandom().nextFloat() - 0.5) * 0.1F,
            0.0,
            (level.getRandom().nextFloat() - 0.5) * 0.1F,
            0.0
         );
      }
   }

   private int getBubbleColor() {
      if (this.level != null && this.recipeId != StockpotRecipeSerializer.EMPTY_ID && this.visuals == null) {
         Either<RecipeHolder<StockpotRecipe>, RecipeHolder<FlexStockpotRecipe>> recipe = this.getRecipeById(this.level, this.recipeId);
         if (recipe != null) {
            recipe.ifLeft(r -> this.visuals = ((StockpotRecipe)r.value()).visuals()).ifRight(r -> this.visuals = ((FlexStockpotRecipe)r.value()).visuals());
         }
      }

      if (this.visuals == null) {
         this.visuals = StockpotVisuals.DEFAULT;
      }

      if (this.status == 2) {
         return this.visuals.cookingBubbleColor();
      } else if (this.status == 3) {
         return this.visuals.finishedBubbleColor();
      } else {
         ISoupBase soup = this.getSoupBase();
         return soup != null ? soup.getBubbleColor() : 16777215;
      }
   }

   @Override
   public boolean onLitClick(Level level, LivingEntity user, ItemStack stack) {
      BlockState blockState = level.getBlockState(this.worldPosition);
      boolean hasLid = this.hasLid();
      if (!hasLid && stack.is((Item)ModItems.STOCKPOT_LID.get())) {
         this.setLidItem(stack.split(1));
         this.setChanged();
         level.setBlockAndUpdate(this.worldPosition, (BlockState)blockState.setValue(StockpotBlock.HAS_LID, true));
         user.playSound(SoundEvents.LANTERN_PLACE, 0.5F, 0.5F);
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "use_lid_on_stockpot");
         return true;
      } else if (hasLid) {
         ItemStack lid = this.getLidItem().isEmpty() ? ((Item)ModItems.STOCKPOT_LID.get()).getDefaultInstance() : this.getLidItem().copy();
         this.setLidItem(ItemStack.EMPTY);
         if (stack.isEmpty()) {
            user.setItemInHand(InteractionHand.MAIN_HAND, lid);
         } else {
            BlockDrop.popResource(level, this.worldPosition, 0.5, lid);
         }

         this.setChanged();
         level.setBlockAndUpdate(this.worldPosition, (BlockState)blockState.setValue(StockpotBlock.HAS_LID, false));
         user.playSound(SoundEvents.LANTERN_BREAK, 0.5F, 0.5F);
         return true;
      } else {
         return false;
      }
   }

   public StockpotInput getInput() {
      return new StockpotInput(this.inputs, this.soupBaseId);
   }

   public void setRecipe(Level levelIn) {
      if (!(levelIn instanceof ServerLevel serverLevel)) {
         return;
      }

      StockpotInput input = this.getInput();
      StockpotMatchRecipeEvent.Pre preEvent = new StockpotMatchRecipeEvent.Pre(levelIn, this, input);
      NeoForge.EVENT_BUS.post(preEvent);
      if (preEvent.getOutput() != null) {
         this.applyRecipe(levelIn, input, preEvent.getOutput());
      }

      Optional<RecipeHolder<StockpotRecipe>> stockpotRecipe = this.quickCheck.getRecipeFor(input, serverLevel);
      if (stockpotRecipe.isPresent()) {
         this.applyRecipe(levelIn, input, stockpotRecipe.get());
         this.postEvent(levelIn, input);
      } else {
         Optional<RecipeHolder<FlexStockpotRecipe>> flexStockpotRecipe = this.flexQuickCheck.getRecipeFor(input, serverLevel);
         if (flexStockpotRecipe.isPresent()) {
            this.applyFlexRecipe(levelIn, input, flexStockpotRecipe.get());
            this.postEvent(levelIn, input);
         } else {
            this.applySuspiciousRecipe();
            this.postEvent(levelIn, input);
         }
      }
   }

   private void postEvent(Level levelIn, StockpotInput input) {
      StockpotMatchRecipeEvent.Post postEvent = new StockpotMatchRecipeEvent.Post(levelIn, this, input, this.recipeId);
      NeoForge.EVENT_BUS.post(postEvent);
      if (postEvent.getOutput() != null) {
         this.applyRecipe(levelIn, input, postEvent.getOutput());
      }
   }

   private void applySuspiciousRecipe() {
      this.recipeId = StockpotRecipeSerializer.EMPTY_ID;
      this.visuals = StockpotVisuals.DEFAULT;
      this.result = Items.SUSPICIOUS_STEW.getDefaultInstance();
      this.currentTick = 300;
      this.takeoutCount = 1;
   }

   private void applyRecipe(Level level, StockpotInput input, RecipeHolder<StockpotRecipe> recipe) {
      StockpotRecipe value = (StockpotRecipe)recipe.value();
      this.recipeId = recipe.id().identifier();
      this.visuals = value.visuals();
      this.result = value.assemble(input);
      this.currentTick = value.time();
      this.takeoutCount = Math.min(this.result.getCount(), 9);
   }

   private void applyFlexRecipe(Level level, StockpotInput input, RecipeHolder<FlexStockpotRecipe> recipe) {
      FlexStockpotRecipe value = (FlexStockpotRecipe)recipe.value();
      this.recipeId = recipe.id().identifier();
      this.visuals = value.visuals();
      this.result = value.assemble(input);
      this.currentTick = value.time();
      this.takeoutCount = Math.min(this.result.getCount(), 9);
      if (level instanceof ServerLevel serverLevel) {
         Quality quality = QualityEvaluator.evaluate(this.inputs, value.ingredients(), recipe.id().identifier(), serverLevel.getSeed());
         QualityUtils.setQuality(this.result, quality);
      }
   }

   @Nullable
   private Either<RecipeHolder<StockpotRecipe>, RecipeHolder<FlexStockpotRecipe>> getRecipeById(Level level, Identifier recipeId) {
      if (!(level instanceof ServerLevel serverLevel)) {
         return null;
      }

      RecipeManager manager = serverLevel.recipeAccess();
      ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, recipeId);
      RecipeHolder<?> holder = manager.byKey(key).orElse(null);
      if (holder == null) {
         return null;
      }
      if (holder.value() instanceof StockpotRecipe) {
         return Either.left((RecipeHolder<StockpotRecipe>)(RecipeHolder<?>)holder);
      }
      if (holder.value() instanceof FlexStockpotRecipe) {
         return Either.right((RecipeHolder<FlexStockpotRecipe>)(RecipeHolder<?>)holder);
      }
      return null;
   }

   @Override
   public boolean addSoupBase(Level level, LivingEntity user, ItemStack bucket) {
      if (this.hasLid()) {
         return false;
      } else if (this.status != 0) {
         return false;
      } else {
         for (Entry<Identifier, ISoupBase> entry : SoupBaseManager.getAllSoupBases().entrySet()) {
            Identifier key = entry.getKey();
            ISoupBase soupBase = entry.getValue();
            if (soupBase.isSoupBase(bucket)) {
               this.soupBaseId = key;
               this.status = 1;
               this.refresh();
               ItemStack container = soupBase.getReturnContainer(level, user, bucket);
               bucket.shrink(1);
               ItemUtils.getItemToLivingEntity(user, container);
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean removeSoupBase(Level level, LivingEntity user, ItemStack bucket) {
      if (this.status == 1 && this.isEmpty() && SoupBaseManager.containsSoupBase(this.soupBaseId)) {
         ISoupBase soupBase = this.getSoupBase();
         if (soupBase != null && soupBase.isContainer(bucket)) {
            this.renderEntity = null;
            this.soupBaseId = ModSoupBases.WATER;
            this.status = 0;
            this.refresh();
            ItemStack container = soupBase.getReturnSoupBase(level, user, bucket);
            bucket.shrink(1);
            ItemUtils.getItemToLivingEntity(user, container);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void addAllIngredients(List<ItemStack> ingredients, LivingEntity user) {
      if (this.level != null) {
         if (!this.hasLid()) {
            if (this.status == 1) {
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

               this.level
                  .playSound(
                     null,
                     this.worldPosition,
                     SoundEvents.ITEM_PICKUP,
                     SoundSource.PLAYERS,
                     0.2F,
                     ((this.level.getRandom().nextFloat() - this.level.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
                  );
               this.refresh();
            }
         }
      }
   }

   @Override
   public boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack) {
      if (this.hasLid()) {
         return false;
      } else if (this.status != 1) {
         return false;
      } else if (itemStack.is(TagMod.INGREDIENT_BLOCKLIST)) {
         return false;
      } else if (TetraCompat.isModularItem(itemStack)) {
         return false;
      } else {
         for (int i = 0; i < this.inputs.size(); i++) {
            if (((ItemStack)this.inputs.get(i)).isEmpty()) {
               Item containerItem = ItemUtils.getContainerItem(itemStack);
               if (containerItem != Items.AIR) {
                  ItemUtils.getItemToLivingEntity(user, containerItem.getDefaultInstance());
               }

               this.inputs.set(i, itemStack.split(1));
               level.playSound(
                  null,
                  user.getX(),
                  user.getY() + 0.5,
                  user.getZ(),
                  SoundEvents.ITEM_PICKUP,
                  SoundSource.PLAYERS,
                  0.2F,
                  ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
               );
               this.refresh();
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean removeIngredient(Level level, LivingEntity user) {
      if (this.hasLid()) {
         return false;
      } else if (this.status != 1) {
         return false;
      } else {
         for (int i = this.inputs.size() - 1; i >= 0; i--) {
            ItemStack stack = (ItemStack)this.inputs.get(i);
            if (!stack.isEmpty()) {
               if (!this.containerIsMatch(user, stack)) {
                  return false;
               }

               this.inputs.set(i, ItemStack.EMPTY);
               ItemUtils.getItemToLivingEntity(user, stack.copy());
               if (this.getSoupBase() instanceof FluidSoupBase fluidSoupBase && fluidSoupBase.getFluid().getFluidType().getTemperature() > 500) {
                  user.hurt(level.damageSources().inFire(), 1.0F);
                  ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(user, "hurt_when_takeout_from_stockpot");
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
         this.sendActionBarMessage(user, "tip.kaleidoscope_cookery.kitchen.remove_ingredient.need_container", containerItem.getDefaultInstance().getHoverName());
         return false;
      }
   }

   @Override
   public boolean takeOutProduct(Level level, LivingEntity user, ItemStack stack) {
      if (this.hasLid()) {
         return false;
      } else if (this.status == 3 && !this.result.isEmpty() && this.takeoutCount > 0) {
         Ingredient carrier;
         if (this.recipeId.equals(StockpotRecipeSerializer.EMPTY_ID)) {
            carrier = StockpotRecipeSerializer.DEFAULT_CARRIER;
         } else {
            Either<RecipeHolder<StockpotRecipe>, RecipeHolder<FlexStockpotRecipe>> recipe = this.getRecipeById(level, this.recipeId);
            if (recipe == null) {
               carrier = StockpotRecipeSerializer.DEFAULT_CARRIER;
            } else {
               carrier = (Ingredient)recipe.map(left -> ((StockpotRecipe)left.value()).carrier(), right -> ((FlexStockpotRecipe)right.value()).carrier());
            }
         }

         boolean needsCarrier = !ModRecipes.isEmptyIngredient(carrier);
         if (needsCarrier && !carrier.test(stack)) {
            Component carrierName = BaseRecipe.displayStack(carrier).getHoverName();
            this.sendActionBarMessage(user, "tip.kaleidoscope_cookery.pot.need_carrier", carrierName);
            return false;
         } else {
            if (needsCarrier) {
               stack.shrink(1);
            }

            ItemStack resultCopy = this.result.copyWithCount(1);
            ItemUtils.getItemToLivingEntity(user, resultCopy);
            this.takeoutCount--;
            if (this.takeoutCount <= 0) {
               this.status = 0;
               this.inputs.clear();
               this.recipeId = StockpotRecipeSerializer.EMPTY_ID;
               this.soupBaseId = ModSoupBases.WATER;
               this.result = ItemStack.EMPTY;
               this.currentTick = -1;
               this.renderEntity = null;
            }

            this.refresh();
            return true;
         }
      } else {
         return false;
      }
   }

   private void sendActionBarMessage(LivingEntity user, String key, Object... args) {
      if (user instanceof ServerPlayer serverPlayer) {
         MutableComponent message = Component.translatable(key, args);
         serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
      }
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      ContainerHelper.saveAllItems(tag.child("Inputs"), this.inputs);
      tag.putString("RecipeId", this.recipeId.toString());
      tag.putString("SoupBaseId", this.soupBaseId.toString());
      tag.store("Result", ItemStack.OPTIONAL_CODEC, this.result);
      tag.putInt("Status", this.status);
      tag.putInt("CurrentTick", this.currentTick);
      tag.putInt("TakeoutCount", this.takeoutCount);
      tag.store("LidItem", ItemStack.OPTIONAL_CODEC, this.lidItem);
   }

   protected void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      if (tag.keySet().contains("Inputs")) {
         this.inputs = NonNullList.withSize(9, ItemStack.EMPTY);
         ContainerHelper.loadAllItems(tag.childOrEmpty("Inputs"), this.inputs);
      }

      if (tag.keySet().contains("RecipeId")) {
         this.recipeId = Identifier.tryParse(tag.getStringOr("RecipeId", ""));
         if (this.level != null && this.recipeId != null) {
            Either<RecipeHolder<StockpotRecipe>, RecipeHolder<FlexStockpotRecipe>> recipe = this.getRecipeById(this.level, this.recipeId);
            if (recipe != null) {
               recipe.ifLeft(r -> this.visuals = ((StockpotRecipe)r.value()).visuals()).ifRight(r -> this.visuals = ((FlexStockpotRecipe)r.value()).visuals());
            }
         }
      }

      if (tag.keySet().contains("SoupBaseId")) {
         this.soupBaseId = Identifier.tryParse(tag.getStringOr("SoupBaseId", ""));
      }

      if (tag.keySet().contains("Result")) {
         this.result = tag.read("Result", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      }

      this.status = tag.getIntOr("Status", 0);
      this.currentTick = tag.getIntOr("CurrentTick", 0);
      this.takeoutCount = tag.getIntOr("TakeoutCount", 0);
      if (tag.keySet().contains("LidItem")) {
         this.lidItem = tag.read("LidItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      }
   }

   public boolean isEmpty() {
      for (ItemStack stack : this.inputs) {
         if (!stack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public NonNullList<ItemStack> getInputs() {
      return this.inputs;
   }

   @Override
   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public int getTakeoutCount() {
      return this.takeoutCount;
   }

   public ItemStack getResult() {
      return this.result;
   }

   public Identifier getSoupBaseId() {
      return this.soupBaseId;
   }

   @Nullable
   public ISoupBase getSoupBase() {
      return SoupBaseManager.getSoupBase(this.soupBaseId);
   }

   public ItemStack getLidItem() {
      return this.lidItem;
   }

   public void setLidItem(ItemStack lidItem) {
      this.lidItem = lidItem;
   }
}
