package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ISteamer;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SteamerItem;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SteamerBlock extends FallingBlock implements EntityBlock, SimpleWaterloggedBlock {
   public static final MapCodec<SteamerBlock> CODEC = simpleCodec(p -> new SteamerBlock());
   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty HALF = BooleanProperty.create("half");
   public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");
   public static final BooleanProperty HAS_BASE = BooleanProperty.create("has_base");
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape HALF_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);
   private static final VoxelShape FULL_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

   public SteamerBlock() {
      super(
         ModRegistrationProperties.blockProperties()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .instabreak()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .sound(SoundType.BAMBOO)
      );
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH))
                     .setValue(HALF, true))
                  .setValue(HAS_LID, false))
               .setValue(HAS_BASE, false))
            .setValue(WATERLOGGED, false)
      );
   }

   @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
   ) {
      return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
      return createTickerHelper(blockEntityType, ModBlocks.STEAMER_BE.get(), (levelIn, blockPos, blockState, steamer) -> steamer.tick(levelIn));
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer instanceof ServerPlayer player && level.getBlockEntity(pos) instanceof ISteamer steamer && steamer.hasHeatSource(level)) {
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "use_steamer");
      }
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      BlockState below = level.getBlockState(pos.below());
      if (isFree(below) && pos.getY() >= level.getMinY()) {
         CompoundTag blockEntityTag = null;
         if (level.getBlockEntity(pos) instanceof SteamerBlockEntity steamer) {
            blockEntityTag = steamer.saveWithoutMetadata(level.registryAccess());
         }

         FallingBlockEntity fall = FallingBlockEntity.fall(level, pos, (BlockState)state.setValue(HAS_BASE, false));
         fall.blockData = blockEntityTag;
         this.falling(fall);
      }
   }

   protected MapCodec<? extends FallingBlock> codec() {
      return CODEC;
   }

   @Override
   public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
      return state.getMapColor(level, pos).col;
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      ticks.scheduleTick(pos, this, this.getDelayAfterPlace());
      if (direction == Direction.DOWN) {
         if (isFree(neighborState)) {
            state = (BlockState)state.setValue(HAS_BASE, false);
         } else {
            state = (BlockState)state.setValue(HAS_BASE, this.shouldHasBase(levelAccessor, pos));
         }
      }

      if (direction == Direction.UP && neighborState.is(this)) {
         state = (BlockState)state.setValue(HAS_LID, false);
      }

      return state;
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      ItemStack itemInHand = player.getItemInHand(hand);
      Boolean hasLid = (Boolean)state.getValue(HAS_LID);
      if (!itemInHand.isEmpty() || !player.isSecondaryUseActive() || !hasLid && level.getBlockState(pos.above()).is(this)) {
         if (!(itemInHand.getItem() instanceof SteamerItem steamerItem)) {
            if (level.isClientSide()) {
               return InteractionResult.SUCCESS;
            }
            if (level.getBlockEntity(pos) instanceof ISteamer steamer) {
               if (steamer.placeFood(level, player, itemInHand)) {
                  return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
               }

               if (steamer.takeFood(level, player)) {
                  return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
               }
            }

            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
         } else {
            if ((Boolean)state.getValue(HALF)
               && !(Boolean)state.getValue(HAS_LID)
               && !itemInHand.has(DataComponents.BLOCK_ENTITY_DATA)) {
               if (level.isClientSide()) {
                  return InteractionResult.SUCCESS;
               }

               level.setBlockAndUpdate(pos, (BlockState)state.setValue(HALF, false));
               if (!player.isCreative()) {
                  itemInHand.shrink(1);
               }
               level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
               if (level.getBlockEntity(pos) instanceof SteamerBlockEntity steamer) {
                  steamer.refresh();
               }
               return InteractionResult.SUCCESS_SERVER;
            }

            BlockPos placePos = hitResult.getBlockPos();

            BlockState blockState;
            for (blockState = state;
               blockState.is(this) && !blockState.getValue(HAS_LID) && !blockState.getValue(HALF);
               blockState = level.getBlockState(placePos)
            ) {
               placePos = placePos.above();
            }

            ItemStack toUse = player.isCreative() ? itemInHand.copy() : itemInHand;
            BlockHitResult newHitResult = new BlockHitResult(hitResult.getLocation(), Direction.UP, placePos, hitResult.isInside());
            BlockPlaceContext placeContext = new BlockPlaceContext(player, hand, toUse, newHitResult);
            if (blockState.canBeReplaced()) {
               InteractionResult result = steamerItem.place(placeContext);
               return result == InteractionResult.FAIL
                  ? InteractionResult.FAIL
                  : (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
            } else if (blockState.is(this) && (Boolean)blockState.getValue(HALF) && !(Boolean)blockState.getValue(HAS_LID)) {
               InteractionResult result = steamerItem.place(placeContext);
               return result == InteractionResult.FAIL
                  ? InteractionResult.FAIL
                  : (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
            } else {
               return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
         }
      } else {
         level.setBlock(pos, (BlockState)state.setValue(HAS_LID, !hasLid), 3);
         return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos clickedPos = context.getClickedPos();
      BlockState blockState = context.getLevel().getBlockState(clickedPos);
      FluidState fluidState = context.getLevel().getFluidState(clickedPos);
      BlockState resultState;
      if (blockState.is(this)) {
         resultState = (BlockState)blockState.setValue(HALF, false);
      } else {
         resultState = (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
            .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
      }

      return isFree(context.getLevel().getBlockState(clickedPos.below()))
         ? (BlockState)resultState.setValue(HAS_BASE, false)
         : (BlockState)resultState.setValue(HAS_BASE, this.shouldHasBase(context.getLevel(), clickedPos));
   }

   private boolean shouldHasBase(net.minecraft.world.level.LevelReader level, BlockPos pos) {
      BlockState belowState = level.getBlockState(pos.below());
      if (belowState.is(this)) {
         return false;
      } else if (belowState.hasProperty(BlockStateProperties.LIT)) {
         return true;
      } else {
         return belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT) ? true : !belowState.isFaceSturdy(level, pos.below(), Direction.UP);
      }
   }

   public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
      if (this.shouldHasBase(level, pos)) {
         level.setBlock(pos, (BlockState)state.setValue(HAS_BASE, true), 3);
      }
   }

   public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
      ItemStack itemInHand = context.getItemInHand();
      return (Boolean)state.getValue(HALF) && itemInHand.is(this.asItem());
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, HALF, HAS_LID, HAS_BASE, WATERLOGGED});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return state.getValue(HALF) ? HALF_AABB : FULL_AABB;
   }

   public BlockState rotate(BlockState pState, Rotation pRot) {
      return (BlockState)pState.setValue(FACING, pRot.rotate((Direction)pState.getValue(FACING)));
   }

   public BlockState mirror(BlockState pState, Mirror pMirror) {
      return pState.rotate(pMirror.getRotation((Direction)pState.getValue(FACING)));
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SteamerBlockEntity(pos, state);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
      return parameter instanceof SteamerBlockEntity steamer && steamer.getLevel() != null
         ? steamer.dropAsItem(steamer.getLevel())
         : super.getDrops(state, lootParamsBuilder);
   }
}
