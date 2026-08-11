package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class StoveBlock extends HorizontalDirectionalBlock {
   public static final MapCodec<StoveBlock> CODEC = simpleCodec(p -> new StoveBlock());
   public static final BooleanProperty LIT = BlockStateProperties.LIT;

   public StoveBlock() {
      super(
         ModRegistrationProperties.blockProperties()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
            .lightLevel(state -> state.getValue(LIT) ? 13 : 0)
            .randomTicks()
            .strength(1.5F, 6.0F)
      );
      this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)).setValue(LIT, false));
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      if ((Boolean)state.getValue(LIT)) {
         double x = pos.getX() + 0.5;
         double y = pos.getY() + 0.5;
         double z = pos.getZ() + 0.5;
         if (random.nextInt(10) == 0) {
            level.playLocalSound(x, y, z, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
         }

         level.addParticle(
            ParticleTypes.SMOKE,
            x + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            y + 0.5 + random.nextDouble() / 3.0,
            z + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            0.0,
            0.02,
            0.0
         );
         Direction direction = (Direction)state.getValue(FACING);
         Axis axis = direction.getAxis();
         double offsetRandom = random.nextDouble() * 0.6 - 0.3;
         double xOffset = axis == Axis.X ? direction.getStepX() * 0.52 : offsetRandom;
         double yOffset = 0.25 + random.nextDouble() * 6.0 / 16.0;
         double zOffset = axis == Axis.Z ? direction.getStepZ() * 0.52 : offsetRandom;
         level.addParticle(ParticleTypes.FLAME, x + xOffset, pos.getY() + yOffset, z + zOffset, 0.0, 0.0, 0.0);
      }
   }

   public void randomTick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random) {
      if ((Boolean)blockState.getValue(LIT) && level.isRainingAt(pos.above())) {
         level.setBlockAndUpdate(pos, (BlockState)blockState.setValue(LIT, false));
         level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
      }
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(LIT) && levelAccessor.isWaterAt(pos.above()) && levelAccessor instanceof ServerLevel serverLevel) {
         serverLevel.setBlockAndUpdate(pos, (BlockState)state.setValue(LIT, false));
         serverLevel.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      return super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      ItemStack itemInHand = player.getItemInHand(hand);
      if (!(Boolean)state.getValue(LIT) && itemInHand.is(TagMod.LIT_STOVE)) {
         level.setBlockAndUpdate(pos, (BlockState)state.setValue(LIT, true));
         if (itemInHand.is(Items.FIRE_CHARGE)) {
            level.playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            itemInHand.shrink(1);
         } else {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            itemInHand.hurtAndBreak(1, player, (hand == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND));
         }

         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "lit_the_stove");
         return InteractionResult.SUCCESS;
      } else if ((Boolean)state.getValue(LIT) && itemInHand.is(TagMod.EXTINGUISH_STOVE)) {
         if (itemInHand.is((Item)ModItems.KITCHEN_SHOVEL.get()) && KitchenShovelItem.hasOil(itemInHand)) {
            KitchenShovelItem.setHasOil(itemInHand, false);
         }

         level.setBlockAndUpdate(pos, (BlockState)state.setValue(LIT, false));
         level.playSound(
            player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F
         );
         itemInHand.hurtAndBreak(1, player, (hand == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND));
         return InteractionResult.SUCCESS;
      } else {
         return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
      }
   }

   public void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
      BlockPos hitBlockPos = hitResult.getBlockPos();
      if (level instanceof ServerLevel serverLevel
         && projectile.isOnFire()
         && projectile.mayInteract(serverLevel, hitBlockPos)
         && !(Boolean)state.getValue(LIT)) {
         level.setBlock(hitBlockPos, (BlockState)state.setValue(BlockStateProperties.LIT, true), 11);
         if (projectile.getOwner() instanceof Player player) {
            ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "lit_the_stove");
         }
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{LIT, FACING});
   }

   public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltip, TooltipFlag pFlag) {
      pTooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.stove").withStyle(ChatFormatting.GRAY));
   }

   @Nullable
   public PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
      return PathType.FIRE;
   }
}
