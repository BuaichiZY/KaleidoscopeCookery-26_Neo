package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.ChairBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.BlockDrop;
import com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChairBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
   public static final MapCodec<ChairBlock> CODEC = simpleCodec(p -> new ChairBlock());
   public static final BooleanProperty HAS_CARPET = BooleanProperty.create("has_carpet");
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape BASE = Block.box(2.0, 0.0, 2.0, 14.0, 9.0, 14.0);
   private static final VoxelShape NORTH = Shapes.or(BASE, Block.box(2.0, 0.0, 12.0, 14.0, 19.0, 14.0));
   private static final VoxelShape SOUTH = Shapes.or(BASE, Block.box(2.0, 0.0, 2.0, 14.0, 19.0, 4.0));
   private static final VoxelShape WEST = Shapes.or(BASE, Block.box(12.0, 0.0, 2.0, 14.0, 19.0, 14.0));
   private static final VoxelShape EAST = Shapes.or(BASE, Block.box(2.0, 0.0, 2.0, 4.0, 19.0, 14.0));

   public ChairBlock() {
      super(
         ModRegistrationProperties.blockProperties().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().ignitedByLava()
      );
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)).setValue(HAS_CARPET, false))
            .setValue(WATERLOGGED, false)
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
       ItemStack itemInHand = stack;
       return hand == InteractionHand.MAIN_HAND && CarpetColor.getColorByCarpet(itemInHand.getItem()) != null
          ? this.useWithCarpets(state, level, pos, player, itemInHand)
         : this.tryToSitOn(state, level, pos, player);
   }

   @NotNull
   private InteractionResult tryToSitOn(BlockState state, Level level, BlockPos pos, Player player) {
      if (!level.isClientSide()) {
         List<SitEntity> entities = level.getEntitiesOfClass(SitEntity.class, new AABB(pos));
         if (entities.isEmpty()) {
            SitEntity entitySit = new SitEntity(level, pos, 0.5125);
            entitySit.setYRot(((Direction)state.getValue(FACING)).toYRot());
            level.addFreshEntity(entitySit);
            player.startRiding(entitySit);
            return InteractionResult.SUCCESS;
         } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      } else {
         return InteractionResult.SUCCESS;
      }
   }

   @NotNull
   private InteractionResult useWithCarpets(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemInHand) {
      DyeColor dyeColor = CarpetColor.getColorByCarpet(itemInHand.getItem());
      boolean hasCarpet = (Boolean)state.getValue(HAS_CARPET);
      if (dyeColor == null) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else if (level.isClientSide()) {
         return InteractionResult.SUCCESS;
      } else {
         if (!hasCarpet) {
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(HAS_CARPET, true));
            if (level.getBlockEntity(pos) instanceof ChairBlockEntity chairBlockEntity) {
               level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);
               chairBlockEntity.setColor(dyeColor);
               chairBlockEntity.refresh();
               itemInHand.shrink(1);
               return InteractionResult.SUCCESS;
            }
         }

         if (hasCarpet && level.getBlockEntity(pos) instanceof ChairBlockEntity chairBlockEntity && chairBlockEntity.getColor() != dyeColor) {
            DyeColor originalColor = chairBlockEntity.getColor();
            ItemStack carpetItem = CarpetColor.getCarpetByColor(originalColor).getDefaultInstance();
            BlockDrop.popResource(level, pos, 0.25, carpetItem);
            level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);
            chairBlockEntity.setColor(dyeColor);
            chairBlockEntity.refresh();
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(HAS_CARPET, true));
            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
          } else {
             return InteractionResult.SUCCESS;
          }
       }
   }

   public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
      levelAccessor.getEntitiesOfClass(SitEntity.class, new AABB(pos)).forEach(Entity::discard);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if ((Boolean)state.getValue(HAS_CARPET) && parameter instanceof ChairBlockEntity chairBlockEntity) {
         Item carpet = CarpetColor.getCarpetByColor(chairBlockEntity.getColor());
         drops.add(new ItemStack(carpet));
      }

      return drops;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, HAS_CARPET, WATERLOGGED});
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
      return new ChairBlockEntity(pos, state);
   }
}
