package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IKitchenwareRacks;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class KitchenwareRacksBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
   public static final MapCodec<KitchenwareRacksBlock> CODEC = simpleCodec(p -> new KitchenwareRacksBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape NORTH = Block.box(1.0, 9.0, 14.0, 15.0, 14.0, 16.0);
   private static final VoxelShape SOUTH = Block.box(1.0, 9.0, 0.0, 15.0, 14.0, 2.0);
   private static final VoxelShape EAST = Block.box(0.0, 9.0, 1.0, 2.0, 14.0, 15.0);
   private static final VoxelShape WEST = Block.box(14.0, 9.0, 1.0, 16.0, 14.0, 15.0);

   public KitchenwareRacksBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava());
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)).setValue(WATERLOGGED, false)
      );
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      return super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         ItemStack mainHandItem = player.getMainHandItem();
         double yRotDeg = ((Direction)state.getValue(FACING)).getOpposite().toYRot();
         float yRotRad = (float)Math.toRadians(yRotDeg);
         Vec3 location = hitResult.getLocation().subtract(Vec3.atCenterOf(pos)).yRot(yRotRad);
         boolean isLeftClick = location.x > 0.0;
         return level.getBlockEntity(pos) instanceof IKitchenwareRacks racks && racks.onClick(player, mainHandItem, isLeftClick)
            ? InteractionResult.SUCCESS
            : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, WATERLOGGED});
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return switch ((Direction)state.getValue(FACING)) {
         case SOUTH -> SOUTH;
         case EAST -> EAST;
         case WEST -> WEST;
         default -> NORTH;
      };
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new KitchenwareRacksBlockEntity(pos, state);
   }

   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide() && player.isCreative() && level.getBlockEntity(pos) instanceof KitchenwareRacksBlockEntity racks) {
         if (!racks.getItemLeft().isEmpty()) {
            popResource(level, pos, racks.getItemLeft());
            racks.setItemLeft(ItemStack.EMPTY);
         }

         if (!racks.getItemRight().isEmpty()) {
            popResource(level, pos, racks.getItemRight());
            racks.setItemRight(ItemStack.EMPTY);
         }
      }

      return super.playerWillDestroy(level, pos, state, player);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof KitchenwareRacksBlockEntity racks) {
         if (!racks.getItemLeft().isEmpty()) {
            drops.add(racks.getItemLeft());
         }

         if (!racks.getItemRight().isEmpty()) {
            drops.add(racks.getItemRight());
         }
      }

      return drops;
   }
}
