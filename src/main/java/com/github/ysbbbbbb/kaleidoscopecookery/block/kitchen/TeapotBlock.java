package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.util.FluidUtils;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public class TeapotBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
   public static final MapCodec<TeapotBlock> CODEC = simpleCodec(p -> new TeapotBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);
   public static final int COMMON = 0;
   public static final int BASED = 1;
   public static final int CHAINED = 2;
   public static final VoxelShape AABB = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 6.0, 13.0), Block.box(5.0, 6.0, 5.0, 11.0, 8.0, 11.0));

   public TeapotBlock() {
      super(ModRegistrationProperties.blockProperties().sound(SoundType.LANTERN).mapColor(MapColor.COLOR_ORANGE).noOcclusion().instabreak());
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, false))
            .setValue(VARIANT, 0)
      );
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
   ) {
      return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
      return createTickerHelper(blockEntityType, ModBlocks.TEAPOT_BE.get(), (lvl, blockPos, blockState, teapot) -> teapot.tick(lvl));
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TeapotBlockEntity(pos, state);
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      int variant = (Integer)state.getValue(VARIANT);
      if (direction == Direction.DOWN && variant != 2) {
         return !neighborState.isFaceSturdy(levelAccessor, neighborPos, Direction.UP)
            ? (BlockState)state.setValue(VARIANT, 1)
            : (BlockState)state.setValue(VARIANT, 0);
      } else if (direction != Direction.UP || variant == 1) {
         return super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
      } else {
         return canSupportCenter(levelAccessor, neighborPos, Direction.DOWN) ? (BlockState)state.setValue(VARIANT, 2) : (BlockState)state.setValue(VARIANT, 0);
      }
   }

   public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else if (level.getBlockEntity(pos) instanceof ITeapot teapot) {
         ItemStack mainHandItem = player.getMainHandItem();
         IFluidHandlerItem capability = FluidUtil.getFluidHandler(mainHandItem).orElse(null);
         if (capability != null) {
            if (FluidUtils.hasFluid(mainHandItem)) {
               boolean result = teapot.addTeaFluid(level, player, mainHandItem);
               return result ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
            } else {
               boolean result = teapot.removeTeaFluid(level, player, mainHandItem);
               return result ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
            }
         } else if (!mainHandItem.isEmpty()) {
            return teapot.addIngredient(level, player, mainHandItem) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
         } else if (mainHandItem.isEmpty() && player.isSecondaryUseActive()) {
            return teapot.removeIngredient(level, player) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
         } else if (mainHandItem.isEmpty() && !player.isSecondaryUseActive()) {
            return teapot.takeTeapot(level, player) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
         } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      } else {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      Level level = context.getLevel();
      FluidState fluidState = level.getFluidState(context.getClickedPos());
      Direction clickFace = context.getClickedFace();
      BlockState blockState = (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
      BlockPos abovePos = context.getClickedPos().above();
      if (clickFace == Direction.DOWN && canSupportCenter(level, abovePos, Direction.DOWN)) {
         return (BlockState)blockState.setValue(VARIANT, 2);
      } else {
         BlockPos belowPos = context.getClickedPos().below();
         BlockState belowState = level.getBlockState(belowPos);
         return !belowState.isFaceSturdy(level, belowPos, Direction.UP) ? (BlockState)blockState.setValue(VARIANT, 1) : blockState;
      }
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, WATERLOGGED, VARIANT});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return AABB;
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
      return parameter instanceof TeapotBlockEntity teapotBlockEntity ? teapotBlockEntity.getDrops() : super.getDrops(state, lootParamsBuilder);
   }
}
