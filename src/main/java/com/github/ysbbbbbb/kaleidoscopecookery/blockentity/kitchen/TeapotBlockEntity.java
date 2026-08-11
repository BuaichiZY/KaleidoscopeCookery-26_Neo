package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.TeapotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.util.FluidUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class TeapotBlockEntity extends BaseBlockEntity implements ITeapot {
   public static final int INGREDIENT_TIME = 200;
   public static final String TEA_FLUID_ID = "TeaFluidId";
   public static final String RESULT = "Result";
   public static final String STATUS = "Status";
   private static final String INPUT = "Input";
   private static final String CURRENT_TICK = "CurrentTick";
   private final CachedCheck<TeapotInput, TeapotRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.TEAPOT_RECIPE);
   private ItemStack input = ItemStack.EMPTY;
   private Identifier teaFluidId = TeapotRecipeSerializer.EMPTY_TEA_FLUID;
   private ItemStack result = ItemStack.EMPTY;
   private int status = 0;
   private int currentTick = -1;
   public AnimationState boilingState = new AnimationState();

   public TeapotBlockEntity(BlockPos pos, BlockState state) {
      super(ModBlocks.TEAPOT_BE.get(), pos, state);
   }

   public void tick(Level level) {
      if (!(level instanceof ServerLevel serverLevel)) {
         return;
      }

      if (this.status == 0) {
         long offset = level.getGameTime() + this.worldPosition.hashCode();
         if (Math.floorMod(offset, 23) == 0) {
            if (this.teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
               return;
            }

            if (!this.hasHeatSource(level)) {
               return;
            }

            this.onProcessingEffects(level);
            if (this.input.isEmpty()) {
               return;
            }

            if (this.currentTick > 0) {
               this.currentTick = Math.max(-1, this.currentTick - 23);
               this.refresh();
               return;
            }

            TeapotInput container = new TeapotInput(this.input, this.teaFluidId);
            Optional<RecipeHolder<TeapotRecipe>> recipeOpt = this.quickCheck.getRecipeFor(container, serverLevel);
            if (recipeOpt.isPresent()) {
               TeapotRecipe teapotRecipe = (TeapotRecipe)recipeOpt.get().value();
               this.result = teapotRecipe.assemble(container, serverLevel.registryAccess());
               this.currentTick = teapotRecipe.time();
               this.status = 1;
               this.refresh();
               return;
            }

            Block.popResource(level, this.worldPosition, this.input);
            this.input = ItemStack.EMPTY;
            this.result = ItemStack.EMPTY;
            this.status = 0;
            this.currentTick = -1;
            this.refresh();
         }
      } else {
         if (this.status == 1) {
            long offset = level.getGameTime() + this.worldPosition.hashCode();
            if (Math.floorMod(offset, 23) == 0) {
               if (!this.hasHeatSource(level)) {
                  return;
               }

               this.onProcessingEffects(level);
               if (this.currentTick > 0) {
                  this.currentTick = Math.max(-1, this.currentTick - 23);
                  this.refresh();
                  return;
               }

               this.status = 2;
               this.currentTick = -1;
               this.refresh();
            }
         }

         if (this.status == 2) {
            long offset = level.getGameTime() + this.worldPosition.hashCode();
            if (Math.floorMod(offset, 11) == 0) {
               if (!this.hasHeatSource(level)) {
                  this.boilingState.stop();
                  this.onFinishEffects(level);
               } else {
                  this.boilingState.start((int)level.getGameTime());
                  this.onBoilingEffects(level);
               }
            }
         }
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
   public boolean addTeaFluid(Level level, LivingEntity user, ItemStack itemStack) {
      if (this.status != 0) {
         this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.state_incorrect", this.getStatusText());
         return false;
      } else {
         IFluidHandlerItem cap = FluidUtil.getFluidHandler(itemStack).orElse(null);
         if (cap == null) {
            return false;
         } else if (!this.teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.has_fluid");
            return false;
         } else {
            FluidStack fluidInTank = cap.getFluidInTank(0);
            Fluid fluid = fluidInTank.getFluid();
            Identifier id = BuiltInRegistries.FLUID.getKey(fluid);
            int amount = fluidInTank.getAmount();
            if (amount < 1000) {
               this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.fluid_not_enough");
               return false;
            } else {
               FluidTank needFluidHandler = new FluidTank(1000, stack -> FluidStack.isSameFluidSameComponents(stack, fluidInTank));
               if (!FluidUtils.emptyItem(user, itemStack, needFluidHandler, 1000)) {
                  return false;
               } else {
                  this.teaFluidId = id;
                  this.refresh();
                  return true;
               }
            }
         }
      }
   }

   @Override
   public boolean removeTeaFluid(Level level, LivingEntity user, ItemStack itemStack) {
      if (this.status == 0 && !this.teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID) && this.input.isEmpty()) {
         IFluidHandlerItem cap = FluidUtil.getFluidHandler(itemStack).orElse(null);
         if (cap == null) {
            return false;
         } else {
            Fluid fluid = (Fluid)BuiltInRegistries.FLUID.getValue(this.teaFluidId);
            if (fluid == Fluids.EMPTY) {
               return false;
            } else {
               FluidTank sourceFluidHandler = new FluidTank(1000);
               sourceFluidHandler.setFluid(new FluidStack(fluid, 1000));
               if (!FluidUtils.fillItem(user, itemStack, sourceFluidHandler, 1000)) {
                  return false;
               } else {
                  this.teaFluidId = TeapotRecipeSerializer.EMPTY_TEA_FLUID;
                  this.currentTick = -1;
                  this.refresh();
                  return true;
               }
            }
         }
      } else {
         this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.take_tea_fluid.blocked");
         return false;
      }
   }

   @Override
   public boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack) {
      if (!(level instanceof ServerLevel serverLevel)) {
         return true;
      }

      if (this.status != 0) {
         this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.state_incorrect", this.getStatusText());
         return false;
      } else if (this.teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
         this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.no_fluid");
         return false;
      } else if (!this.input.isEmpty()) {
         this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.has_ingredient");
         return false;
      } else {
         TeapotInput container = new TeapotInput(itemStack, this.teaFluidId);
         Optional<RecipeHolder<TeapotRecipe>> recipeOpt = this.quickCheck.getRecipeFor(container, serverLevel);
         if (recipeOpt.isPresent()) {
            TeapotRecipe recipe = (TeapotRecipe)recipeOpt.get().value();
            int count = recipe.ingredientCount();
            this.input = itemStack.copyWithCount(count);
            this.currentTick = 200;
            this.refresh();
            itemStack.shrink(count);
            return true;
         } else {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.recipe_incorrect");
            return false;
         }
      }
   }

   @Override
   public boolean removeIngredient(Level level, LivingEntity user) {
      if (this.status != 0) {
         return false;
      } else if (this.input.isEmpty()) {
         return false;
      } else {
         ItemUtils.getItemToLivingEntity(user, this.input.copyAndClear());
         this.refresh();
         return true;
      }
   }

   @Override
   public boolean takeTeapot(Level level, LivingEntity user) {
      if (this.status == 1) {
         this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.take_teapot.state_incorrect");
         return false;
      } else {
         for (ItemStack drop : this.getDrops()) {
            ItemUtils.getItemToLivingEntity(user, drop);
         }

         level.playSound(null, this.worldPosition, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 0.6F, 0.8F + level.getRandom().nextFloat() * 0.2F);
         level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
         return true;
      }
   }

   public List<ItemStack> getDrops() {
      List<ItemStack> drops = Lists.newArrayList();
      ItemStack teapot = ((Item)ModItems.TEAPOT.get()).getDefaultInstance();
      if (this.status == 0) {
         if (!this.input.isEmpty()) {
            drops.add(this.input.copy());
         }

         if (this.level != null) {
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.level.registryAccess());
            output.putString("TeaFluidId", this.teaFluidId.toString());
            BlockItem.setBlockEntityData(teapot, ModBlocks.TEAPOT_BE.get(), output);
         }
         drops.add(teapot);
         return drops;
      } else if (this.status == 1) {
         drops.add(teapot);
         return drops;
      } else {
         if (this.level != null) {
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.level.registryAccess());
            output.store("Result", ItemStack.OPTIONAL_CODEC, this.result.copy());
            output.putInt("Status", this.status);
            BlockItem.setBlockEntityData(teapot, ModBlocks.TEAPOT_BE.get(), output);
         }

         drops.add(teapot);
         return drops;
      }
   }

   private void onProcessingEffects(Level level) {
      RandomSource random = level.getRandom();
      level.playSound(null, this.worldPosition, (SoundEvent)ModSounds.BLOCK_TEAPOT_PROCESSING.get(), SoundSource.BLOCKS, 0.6F, 0.8F + random.nextFloat() * 0.2F);
      this.onFinishEffects(level);
   }

   private void onBoilingEffects(Level level) {
      RandomSource random = level.getRandom();
      level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.8F + random.nextFloat() * 0.2F);
      if (level instanceof ServerLevel serverLevel) {
         serverLevel.sendParticles(
            (SimpleParticleType)ModParticles.COOKING.get(),
            this.worldPosition.getX() + 0.5 + (level.getRandom().nextFloat() - 0.5F),
            this.worldPosition.getY() + 0.6 + level.getRandom().nextDouble() / 5.0,
            this.worldPosition.getZ() + 0.5 + (level.getRandom().nextFloat() - 0.5F),
            3,
            (level.getRandom().nextFloat() - 0.5) * 0.05F,
            0.1,
            (level.getRandom().nextFloat() - 0.5) * 0.05F,
            0.02
         );
      }
   }

   private void onFinishEffects(Level level) {
      RandomSource random = level.getRandom();
      if (level instanceof ServerLevel serverLevel) {
         serverLevel.sendParticles(
            (SimpleParticleType)ModParticles.COOKING.get(),
            this.worldPosition.getX() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
            this.worldPosition.getY() + 0.8 + random.nextDouble() / 3.0,
            this.worldPosition.getZ() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
            1,
            (level.getRandom().nextFloat() - 0.5) * 0.05F,
            0.1,
            (level.getRandom().nextFloat() - 0.5) * 0.05F,
            0.02
         );
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
      tag.store("Input", ItemStack.OPTIONAL_CODEC, this.input);
      tag.putString("TeaFluidId", this.teaFluidId.toString());
      tag.store("Result", ItemStack.OPTIONAL_CODEC, this.result);
      tag.putInt("Status", this.status);
      tag.putInt("CurrentTick", this.currentTick);
   }

   public void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      this.input = tag.read("Input", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      this.teaFluidId = Identifier.parse(tag.getStringOr("TeaFluidId", ""));
      this.result = tag.read("Result", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      this.status = tag.getIntOr("Status", 0);
      this.currentTick = tag.getIntOr("CurrentTick", 0);
   }

   @Override
   public int getStatus() {
      return this.status;
   }

   public Component getStatusText() {
      if (this.status == 0) {
         return Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.put_ingredient");
      } else if (this.status == 1) {
         return Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.processing");
      } else {
         return this.status == 2 ? Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.finished") : Component.empty();
      }
   }

   public ItemStack getInput() {
      return this.input;
   }

   public Identifier getTeaFluidId() {
      return this.teaFluidId;
   }

   public ItemStack getResult() {
      return this.result;
   }

   public int getCurrentTick() {
      return this.currentTick;
   }
}
