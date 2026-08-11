package com.github.ysbbbbbb.kaleidoscopecookery.block.drink;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeacupItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
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

public class TeacupBlock extends HorizontalDirectionalBlock {
   public static final MapCodec<TeacupBlock> CODEC = simpleCodec(p -> new TeacupBlock());
   public static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);
   private static final IntegerProperty CUP_COUNT = IntegerProperty.create("cup_count", 1, 16);
   private static final IntegerProperty TEA_COUNT = IntegerProperty.create("tea_count", 1, 16);
   protected final IntegerProperty cupCount;
   protected final IntegerProperty teaCount;
   protected final int maxCount;
   protected VoxelShape aabb = AABB;

   public TeacupBlock(int maxCount) {
      super(ModRegistrationProperties.blockProperties().forceSolidOn().instabreak().mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).noOcclusion());
      this.maxCount = maxCount;
      this.cupCount = CUP_COUNT;
      this.teaCount = TEA_COUNT;
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.cupCount, 1)).setValue(this.teaCount, 1))
            .setValue(FACING, Direction.SOUTH)
      );
   }

   public TeacupBlock() {
      this(4);
   }

   public TeacupBlock setAABB(VoxelShape aabb) {
      this.aabb = aabb;
      return this;
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   public int getMaxCount() {
      return this.maxCount;
   }

   public IntegerProperty getCupCountProperty() {
      return this.cupCount;
   }

   public IntegerProperty getTeaCountProperty() {
      return this.teaCount;
   }

   public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         ItemStack itemInHand = player.getItemInHand(hand);
         if (itemInHand.is((Item)ModItems.TEAPOT.get())) {
            ItemStack pourOut = TeapotItem.getPourOut(itemInHand, level);
            if (!pourOut.isEmpty() && pourOut.getItem() == this.asItem()) {
               int count = (Integer)state.getValue(this.teaCount);
               if (count < (Integer)state.getValue(this.cupCount)) {
                  level.setBlockAndUpdate(pos, (BlockState)state.setValue(this.teaCount, count + 1));
                  level.playSound(player, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
                  TeapotItem.pourOut(itemInHand, level);
                  spawnPourParticles(level, pos);
                  return InteractionResult.SUCCESS;
               } else {
                  return InteractionResult.CONSUME;
               }
            } else {
               return InteractionResult.CONSUME;
            }
         } else if (itemInHand.is((Item)ModItems.EMPTY_CUP.get())) {
            int count = (Integer)state.getValue(this.cupCount);
            if (count < this.maxCount) {
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(this.cupCount, count + 1));
               level.playSound(player, pos, this.soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
               itemInHand.shrink(1);
               return InteractionResult.SUCCESS;
            } else {
               return InteractionResult.CONSUME;
            }
         } else if (itemInHand.getItem() instanceof TeacupItem teacupItem) {
            if (teacupItem.getBlock() != this) {
               return InteractionResult.CONSUME;
            } else {
               int cupCountNum = (Integer)state.getValue(this.cupCount);
               int teaCountNum = (Integer)state.getValue(this.teaCount);
               if (cupCountNum < this.maxCount) {
                  level.setBlockAndUpdate(
                     pos, (BlockState)((BlockState)state.setValue(this.cupCount, cupCountNum + 1)).setValue(this.teaCount, teaCountNum + 1)
                  );
                  level.playSound(player, pos, this.soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                  itemInHand.shrink(1);
                  return InteractionResult.SUCCESS;
               } else {
                  return InteractionResult.CONSUME;
               }
            }
         } else {
            if (itemInHand.isEmpty()) {
               int cupCountNum = (Integer)state.getValue(this.cupCount);
               int teaCountNum = (Integer)state.getValue(this.teaCount);
               int emptyCountNum = cupCountNum - teaCountNum;
               if (emptyCountNum > 0) {
                  ItemStack cupStack = new ItemStack((ItemLike)ModItems.EMPTY_CUP.get());
                  ItemUtils.getItemToLivingEntity(player, cupStack);
                  if (cupCountNum == 1) {
                     level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                  } else {
                     level.setBlockAndUpdate(pos, (BlockState)state.setValue(this.cupCount, cupCountNum - 1));
                  }

                  level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                  return InteractionResult.SUCCESS;
               }

               if (teaCountNum > 0) {
                  ItemStack teaStack = new ItemStack(this);
                  ItemUtils.getItemToLivingEntity(player, teaStack);
                  if (cupCountNum == 1) {
                     level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                  } else {
                     level.setBlockAndUpdate(
                        pos, (BlockState)((BlockState)state.setValue(this.teaCount, teaCountNum - 1)).setValue(this.cupCount, cupCountNum - 1)
                     );
                     level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                  }

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

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      if (random.nextInt(20) == 0) {
         double x = pos.getX() + 0.5;
         double y = pos.getY() + 0.5;
         double z = pos.getZ() + 0.5;
         level.addParticle(
            (ParticleOptions)ModParticles.COOKING.get(),
            x + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            y + random.nextDouble() / 3.0,
            z + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            0.3,
            0.1,
            0.3
         );
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{CUP_COUNT, TEA_COUNT, FACING});
   }

   protected void createCountBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{CUP_COUNT, TEA_COUNT, FACING});
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return this.aabb;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      List<ItemStack> drops = Lists.newArrayList();
      int teaCountNum = (Integer)state.getValue(this.teaCount);
      int cupCountNum = (Integer)state.getValue(this.cupCount);
      int emptyCountNum = cupCountNum - teaCountNum;
      if (emptyCountNum > 0) {
         drops.add(new ItemStack((ItemLike)ModItems.EMPTY_CUP.get(), emptyCountNum));
      }

      if (teaCountNum > 0) {
         drops.add(new ItemStack(this, teaCountNum));
      }

      return drops;
   }
}
