package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class PlateBlock extends HorizontalDirectionalBlock {
   public static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);
   protected static final MapCodec<PlateBlock> PLATE_BLOCK_CODEC = simpleCodec(p -> new PlateBlock(1, List.of()));
   private static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 16);
   protected final IntegerProperty servings;
   protected final List<Supplier<Item>> items;
   protected final int maxCount;
   protected VoxelShape aabb = AABB;

   public PlateBlock(int maxCount, List<Supplier<Item>> items) {
      super(ModRegistrationProperties.blockProperties().forceSolidOn().instabreak().mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).noOcclusion());
      this.servings = SERVINGS;
      this.maxCount = maxCount;
      this.items = items;
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)).setValue(this.servings, maxCount)
      );
   }

   public PlateBlock setAABB(VoxelShape aabb) {
      this.aabb = aabb;
      return this;
   }

   public int getMaxCount() {
      return this.maxCount;
   }

   public IntegerProperty getServingsProperty() {
      return this.servings;
   }

   public InteractionResult useItemOn(
      ItemStack itemInHand, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         int count = (Integer)state.getValue(this.servings);
         if (!itemInHand.isEmpty() && count < this.maxCount && this.canRefill(itemInHand)) {
            itemInHand.shrink(1);
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlockAndUpdate(pos, (BlockState)state.cycle(this.servings));
            return InteractionResult.SUCCESS;
         } else {
            if (count > 0) {
               List<ItemStack> stacks = this.items.stream().map(s -> s.get().getDefaultInstance()).toList();
               if (itemInHand.isEmpty()) {
                  stacks.forEach(s -> ItemHandlerHelper.giveItemToPlayer(player, s));
               } else {
                  stacks.forEach(s -> Block.popResourceFromFace(level, pos, Direction.UP, s));
               }

               level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(this.servings, count - 1));
            } else {
               level.destroyBlock(pos, true, player);
            }

            return InteractionResult.SUCCESS;
         }
      }
   }

   private boolean canRefill(ItemStack itemStack) {
      return this.items.size() == 1 ? itemStack.is(this.items.getFirst().get()) : false;
   }

   protected void createServingBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, SERVINGS});
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(FACING, SERVINGS);
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return this.aabb;
   }

   protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
      return false;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      List<ItemStack> drops = Lists.newArrayList(super.getDrops(state, params));
      int count = (Integer)state.getValue(this.servings);
      if (count > 0) {
         List<ItemStack> stacks = this.items.stream().map(s -> s.get().getDefaultInstance().copyWithCount(count)).toList();
         drops.addAll(stacks);
      }

      return drops;
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return PLATE_BLOCK_CODEC;
   }
}
