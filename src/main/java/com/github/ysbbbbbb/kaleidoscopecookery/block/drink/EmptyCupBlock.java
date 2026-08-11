package com.github.ysbbbbbb.kaleidoscopecookery.block.drink;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeacupItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EmptyCupBlock extends HorizontalDirectionalBlock {
   public static final MapCodec<EmptyCupBlock> CODEC = simpleCodec(p -> new EmptyCupBlock());
   public static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);
   public static final int MAX_COUNT = 4;
   public static final IntegerProperty CUP_COUNT = IntegerProperty.create("cup_count", 1, 4);

   public EmptyCupBlock() {
      super(ModRegistrationProperties.blockProperties().forceSolidOn().instabreak().mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).noOcclusion());
      this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(CUP_COUNT, 1)).setValue(FACING, Direction.SOUTH));
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         ItemStack itemInHand = player.getItemInHand(hand);
         if (itemInHand.is((Item)ModItems.TEAPOT.get())) {
            ItemStack pourOut = TeapotItem.getPourOut(itemInHand, level);
            if (pourOut.isEmpty()) {
               return InteractionResult.CONSUME;
            } else if (pourOut.getItem() instanceof TeacupItem teacupItem && teacupItem.getBlock() instanceof TeacupBlock teacupBlock) {
               int currentCount = (Integer)state.getValue(CUP_COUNT);
               if (currentCount > teacupBlock.getMaxCount()) {
                  ItemStack returnStack = new ItemStack((ItemLike)ModItems.EMPTY_CUP.get(), currentCount - teacupBlock.getMaxCount());
                  ItemUtils.getItemToLivingEntity(player, returnStack);
               }

               level.playSound(player, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
               TeapotItem.pourOut(itemInHand, level);
               spawnPourParticles(level, pos);
               level.setBlockAndUpdate(
                  pos,
                  (BlockState)((BlockState)((BlockState)teacupBlock.defaultBlockState()
                           .setValue(teacupBlock.getCupCountProperty(), Math.min(currentCount, teacupBlock.getMaxCount())))
                        .setValue(teacupBlock.getTeaCountProperty(), 1))
                     .setValue(FACING, (Direction)state.getValue(FACING))
               );
               return InteractionResult.SUCCESS;
            } else {
               return InteractionResult.CONSUME;
            }
         } else if (itemInHand.is((Item)ModItems.EMPTY_CUP.get())) {
            int count = (Integer)state.getValue(CUP_COUNT);
            if (count < 4) {
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(CUP_COUNT, count + 1));
               level.playSound(player, pos, this.soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
               itemInHand.shrink(1);
               return InteractionResult.SUCCESS;
            } else {
               return InteractionResult.CONSUME;
            }
         } else {
            if (itemInHand.isEmpty()) {
               int cupCountNum = (Integer)state.getValue(CUP_COUNT);
               if (cupCountNum > 0) {
                  ItemStack cupStack = new ItemStack((ItemLike)ModItems.EMPTY_CUP.get());
                  ItemUtils.getItemToLivingEntity(player, cupStack);
                  if (cupCountNum == 1) {
                     level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                  } else {
                     level.setBlockAndUpdate(pos, (BlockState)state.setValue(CUP_COUNT, cupCountNum - 1));
                  }

                  level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                  return InteractionResult.SUCCESS;
               }
            }

            return InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      }
   }

   private static void spawnPourParticles(Level level, BlockPos pos) {
      if (level instanceof ServerLevel serverLevel) {
         RandomSource random = level.getRandom();
         serverLevel.sendParticles(
            (SimpleParticleType)ModParticles.COOKING.get(),
            pos.getX() + 0.5,
            pos.getY() + 0.35,
            pos.getZ() + 0.5,
            4,
            0.12 + random.nextDouble() * 0.04,
            0.08,
            0.12 + random.nextDouble() * 0.04,
            0.02
         );
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{CUP_COUNT, FACING});
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return AABB;
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      int cupCount = (Integer)state.getValue(CUP_COUNT);
      return cupCount > 0 ? List.of(new ItemStack((ItemLike)ModItems.EMPTY_CUP.get(), cupCount)) : super.getDrops(state, params);
   }
}
