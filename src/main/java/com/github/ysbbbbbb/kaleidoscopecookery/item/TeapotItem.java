package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.apache.commons.lang3.StringUtils;

public class TeapotItem extends BlockItem {
   public TeapotItem() {
      super((Block)ModBlocks.TEAPOT.get(), ModRegistrationProperties.itemProperties().stacksTo(1));
   }

   private static CompoundTag getBlockData(ItemStack stack) {
      TypedEntityData<BlockEntityType<?>> data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
      return data == null ? null : data.copyTagWithoutId();
   }

   private static ItemStack readItem(CompoundTag tag, String key, HolderLookup.Provider registries) {
      return TagValueInput.create(ProblemReporter.DISCARDING, registries, tag)
         .read(key, ItemStack.OPTIONAL_CODEC)
         .orElse(ItemStack.EMPTY);
   }

   private static TagValueOutput blockDataOutput(Level level, CompoundTag tag) {
      TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
      output.store(tag);
      return output;
   }

   private static void setBlockData(ItemStack stack, Level level, CompoundTag tag) {
      BlockItem.setBlockEntityData(stack, ModBlocks.TEAPOT_BE.get(), blockDataOutput(level, tag));
   }

   private static void setBlockDataWithItem(ItemStack stack, Level level, CompoundTag tag, String key, ItemStack value) {
      TagValueOutput output = blockDataOutput(level, tag);
      output.store(key, ItemStack.OPTIONAL_CODEC, value);
      BlockItem.setBlockEntityData(stack, ModBlocks.TEAPOT_BE.get(), output);
   }

   public static ItemStack getPourOut(ItemStack stack, Level level) {
      CompoundTag tag = getBlockData(stack);
      if (tag == null) {
         return ItemStack.EMPTY;
      } else {
         int status = tag.getIntOr("Status", 0);
         return status != 2 ? ItemStack.EMPTY : readItem(tag, "Result", level.registryAccess());
      }
   }

   public static void pourOut(ItemStack stack, Level level) {
      CompoundTag tag = getBlockData(stack);
      if (tag != null) {
         int status = tag.getIntOr("Status", 0);
         if (status == 2) {
            ItemStack result = readItem(tag, "Result", level.registryAccess());
            if (!result.isEmpty()) {
               result.shrink(1);
               if (result.isEmpty()) {
                  stack.remove(DataComponents.BLOCK_ENTITY_DATA);
               } else {
                  setBlockDataWithItem(stack, level, tag, "Result", result);
               }
            }
         }
      }
   }

   public static boolean fillFluid(ItemStack stack, Fluid fluid, LivingEntity user) {
      CompoundTag tag = getBlockData(stack);
      if (tag == null) {
         tag = new CompoundTag();
      }
      int status = tag.getIntOr("Status", 0);
      if (status != 0) {
         return false;
      } else {
         String fluidId = (String)StringUtils.defaultIfBlank(tag.getStringOr("TeaFluidId", ""), TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
         if (!fluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString())) {
            return false;
         } else {
            Identifier key = BuiltInRegistries.FLUID.getKey(fluid);
            tag.putString("TeaFluidId", key.toString());
            setBlockData(stack, user.level(), tag);
            SoundEvent sound = fluid.getFluidType().getSound(user, SoundActions.BUCKET_FILL);
            if (sound != null) {
               user.playSound(sound);
            }

            return true;
         }
      }
   }

   public static void clearAll(ItemStack stack, Player player) {
      stack.remove(DataComponents.BLOCK_ENTITY_DATA);
      player.playSound(SoundEvents.BUCKET_EMPTY, 1.0F, 1.0F);
   }

   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      return player != null && !player.isSecondaryUseActive() ? InteractionResult.PASS : super.useOn(context);
   }

   public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
      CompoundTag tag = getBlockData(stack);
      if (tag == null) {
         return InteractionResult.PASS;
      } else {
         Level level = player.level();
         int status = tag.getIntOr("Status", 0);
         if (status == 2) {
            pourOut(stack, level);
         } else if (status == 0) {
            String fluidId = (String)StringUtils.defaultIfBlank(tag.getStringOr("TeaFluidId", ""), TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
            if (!"minecraft:lava".equals(fluidId)) {
               return InteractionResult.PASS;
            }

            if (player.getRandom().nextFloat() < 0.3F) {
               clearAll(stack, player);
            }
         }

         RandomSource random = level.getRandom();
         target.hurt(level.damageSources().inFire(), 3.0F);
         double x = target.getX();
         double y = target.getY() + target.getEyeHeight() + 0.25;
         double z = target.getZ();
         player.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);

         for (int i = 0; i < 10; i++) {
            level.addParticle(
               ParticleTypes.LAVA,
               x + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
               y + random.nextDouble() / 3.0,
               z + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
               0.3,
               0.1,
               0.3
            );
         }

         return InteractionResult.SUCCESS;
      }
   }

   public InteractionResult use(Level level, Player player, InteractionHand hand) {
      ItemStack itemInHand = player.getItemInHand(hand);
      CompoundTag tag = getBlockData(itemInHand);
      if (tag != null) {
         String fluidId = (String)StringUtils.defaultIfBlank(tag.getStringOr("TeaFluidId", ""), TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
         if (!fluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString())) {
            return InteractionResult.FAIL;
         }
      }

      BlockHitResult hitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);
      if (hitResult.getType() == Type.MISS) {
         return InteractionResult.PASS;
      } else if (hitResult.getType() != Type.BLOCK) {
         return InteractionResult.PASS;
      } else {
         BlockPos pos = hitResult.getBlockPos();
         Direction direction = hitResult.getDirection();
         BlockPos relative = pos.relative(direction);
         if (level.mayInteract(player, pos) && player.mayUseItemAt(relative, direction, itemInHand)) {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.getBlock() instanceof BucketPickup bucketpickup) {
               ItemStack pickup = bucketpickup.pickupBlock(player, level, pos, blockState);
               if (pickup.isEmpty()) {
                  return InteractionResult.FAIL;
               } else {
                  IFluidHandlerItem capability = FluidUtil.getFluidHandler(pickup).orElse(null);
                  if (capability == null) {
                     return InteractionResult.FAIL;
                  } else {
                     Fluid fluid = capability.getFluidInTank(0).getFluid();
                     boolean result = fillFluid(itemInHand, fluid, player);
                     return result
                        ? (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER)
                        : InteractionResult.FAIL;
                  }
               }
            } else {
               return InteractionResult.FAIL;
            }
         } else {
            return InteractionResult.FAIL;
         }
      }
   }

   public boolean isBarVisible(ItemStack stack) {
      CompoundTag tag = getBlockData(stack);
      if (tag == null) {
         return false;
      } else {
         int status = tag.getIntOr("Status", 0);
         if (status == 0) {
            String fluidId = (String)StringUtils.defaultIfBlank(tag.getStringOr("TeaFluidId", ""), TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
            return !fluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
         } else {
            return status == 2;
         }
      }
   }

   public int getBarColor(ItemStack stack) {
      CompoundTag tag = getBlockData(stack);
      if (tag == null) {
         return 10352639;
      } else {
         int status = tag.getIntOr("Status", 0);
         if (status == 0) {
            String fluidId = (String)StringUtils.defaultIfBlank(tag.getStringOr("TeaFluidId", ""), TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
            if (fluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString())) {
               return 10352639;
            } else {
               Fluid fluid = (Fluid)BuiltInRegistries.FLUID.getValue(Identifier.parse(fluidId));
               return fluid.equals(Fluids.LAVA) ? 16492544 : 4159204;
            }
         } else {
            return 10352639;
         }
      }
   }

   public int getBarWidth(ItemStack stack) {
      CompoundTag tag = getBlockData(stack);
      if (tag == null) {
         return 0;
      } else {
         int status = tag.getIntOr("Status", 0);
         if (status == 0) {
            String fluidId = (String)StringUtils.defaultIfBlank(tag.getStringOr("TeaFluidId", ""), TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
            return fluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString()) ? 0 : 13;
         } else if (status == 2) {
            int count = tag.getCompoundOrEmpty("Result").getIntOr("count", 0);
            return count <= 0 ? 0 : Math.round(13.0F * count / 12.0F);
         } else {
            return 0;
         }
      }
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag pFlag) {
      CompoundTag tag = getBlockData(stack);
      if (tag != null) {
         int status = tag.getIntOr("Status", 0);
         if (status == 0) {
            String fluidId = (String)StringUtils.defaultIfBlank(tag.getStringOr("TeaFluidId", ""), TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString());
            if (fluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID.toString())) {
               return;
            }

            Identifier key = Identifier.parse(fluidId);
            Fluid fluid = (Fluid)BuiltInRegistries.FLUID.getValue(key);
            list.add(Component.translatable(fluid.getFluidType().getDescriptionId()).withStyle(ChatFormatting.GRAY));
         }

         if (status == 2) {
            ItemStack result = readItem(tag, "Result", context.registries());
            if (result.isEmpty()) {
               return;
            }

            Component resultComponent = ComponentUtils.formatList(
                  Arrays.asList(result.getHoverName(), Component.literal("x%d".formatted(result.getCount()))), CommonComponents.space(), Function.identity()
               )
               .withStyle(ChatFormatting.GRAY);
            list.add(resultComponent);
         }
      }
   }
}
