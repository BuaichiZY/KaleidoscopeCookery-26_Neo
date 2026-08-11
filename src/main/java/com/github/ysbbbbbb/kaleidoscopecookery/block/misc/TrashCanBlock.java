package com.github.ysbbbbbb.kaleidoscopecookery.block.misc;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TrashCanBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
   public static final MapCodec<TrashCanBlock> CODEC = simpleCodec(p -> new TrashCanBlock());
   public static final VoxelShape SUCK_ZONE = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
   public static final VoxelShape AABB = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 15.0, 14.0), Block.box(1.0, 12.0, 1.0, 15.0, 15.0, 15.0));
   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public TrashCanBlock() {
      super(ModRegistrationProperties.blockProperties().sound(SoundType.METAL).mapColor(MapColor.COLOR_BLACK).noOcclusion().strength(1.5F, 6.0F));
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(POWERED, false))
            .setValue(WATERLOGGED, false)
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
      return createTickerHelper(blockEntityType, ModBlocks.TRASH_CAN_BE.get(), (lvl, blockPos, blockState, trashCan) -> {
         if (lvl.isClientSide()) {
            trashCan.clientTick(lvl);
         } else if (lvl instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            trashCan.serverTick(serverLevel);
         }
      });
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TrashCanBlockEntity(pos, state);
   }

   @Override
   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
      super.triggerEvent(state, level, pos, id, param);
      BlockEntity blockEntity = level.getBlockEntity(pos);
      return blockEntity != null && blockEntity.triggerEvent(id, param);
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      return super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
   }

   public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         if (player.getVehicle() instanceof SitEntity sit && sit.getSitType() == SitEntity.TRASH_CAN) {
            return InteractionResult.CONSUME;
         }
         ItemStack itemInHand = player.getItemInHand(hand);
         if (level.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCan) {
            if (itemInHand.isEmpty() && player.isSecondaryUseActive()) {
               trashCan.withdrawItem(player);
               return InteractionResult.SUCCESS;
            }

            if (!itemInHand.isEmpty()) {
               trashCan.putItem(itemInHand);
               return InteractionResult.SUCCESS;
            }
         }

         return InteractionResult.TRY_WITH_EMPTY_HAND;
      }
   }

   public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
      levelAccessor.getEntitiesOfClass(SitEntity.class, new AABB(pos)).forEach(Entity::discard);
   }

   @Override
   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
      super.fallOn(level, state, pos, entity, fallDistance);
      if (entity instanceof Player player && player.getVehicle() == null && fallDistance > 0.5D) {
         List<SitEntity> entities = level.getEntitiesOfClass(SitEntity.class, new AABB(pos));
         if (!entities.isEmpty()) {
            return;
         }

         if (!level.isClientSide()) {
            SitEntity entitySit = new SitEntity(level, pos, 0.875, 1);
            entitySit.setYRot(((Direction)state.getValue(FACING)).toYRot());
            level.addFreshEntity(entitySit);
            player.startRiding(entitySit);
         }

         level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(32.0)).forEach(e -> {
            if (e.getTarget() == player) {
               e.setTarget(null);
            }
         });
         if (level.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCan) {
            trashCan.enterState.start((int)level.getGameTime());
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
               serverLevel.blockEvent(pos, this, TrashCanBlockEntity.EVENT_ENTER, 0);
            }
         }
      }
   }

   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (level.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCan) {
         trashCan.entityInside(level, pos, entity);
      }
   }

   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean isMoving) {
      boolean isPowered = level.hasNeighborSignal(pos);
      if (isPowered != (Boolean)state.getValue(POWERED)) {
         level.setBlockAndUpdate(pos, (BlockState)state.setValue(POWERED, isPowered));
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      Level level = context.getLevel();
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, level.isWaterAt(context.getClickedPos()));
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, POWERED, WATERLOGGED});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return AABB;
   }

   @Override
   public VoxelShape getVisualShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
      if (context instanceof EntityCollisionContext entityContext
         && entityContext.getEntity() instanceof Player player
         && player.getVehicle() instanceof SitEntity seat
         && seat.getSitType() == SitEntity.TRASH_CAN) {
         // Detached-camera collision uses the visual shape.  Ignore only the
         // trash can occupied by the camera player so third-person can pull out.
         return Shapes.empty();
      }
      return super.getVisualShape(state, blockGetter, pos, context);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      List<ItemStack> drops = super.getDrops(state, params);
      BlockEntity parameter = (BlockEntity)params.getParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof TrashCanBlockEntity trashCanBlock) {
         for (int i = 0; i < trashCanBlock.getStorage().getSlots(); i++) {
            ItemStack stack = trashCanBlock.getStorage().getStackInSlot(i);
            if (!stack.isEmpty()) {
               drops.add(stack);
            }
         }
      }

      return drops;
   }
}
