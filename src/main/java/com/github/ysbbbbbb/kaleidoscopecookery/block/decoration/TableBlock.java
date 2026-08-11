package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.BlockDrop;
import com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TableBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
   public static final EnumProperty<Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
   public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 3);
   public static final BooleanProperty HAS_CARPET = BooleanProperty.create("has_carpet");
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final int SINGLE = 0;
   public static final int LEFT = 1;
   public static final int MIDDLE = 2;
   public static final int RIGHT = 3;
   private static final VoxelShape FACE = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);

   public TableBlock() {
      super(
         ModRegistrationProperties.blockProperties().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().ignitedByLava()
      );
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(AXIS, Axis.Z)).setValue(POSITION, 0))
               .setValue(HAS_CARPET, false))
            .setValue(WATERLOGGED, false)
      );
   }

   public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
       ItemStack itemInHand = stack;
       if (hand == InteractionHand.MAIN_HAND) {
          if (CarpetColor.getColorByCarpet(itemInHand.getItem()) != null) {
             return this.useWithCarpets(state, level, pos, player, itemInHand);
         }

         if (level.getBlockEntity(pos) instanceof TableBlockEntity table) {
            return this.useWithOther(level, pos, player, hand, table, itemInHand);
         }
      }

      return super.useItemOn(stack, state, level, pos, player, hand, hit);
   }

   @NotNull
   private InteractionResult useWithOther(Level level, BlockPos pos, Player player, InteractionHand hand, TableBlockEntity table, ItemStack itemInHand) {
      ItemStackHandler tableItems = table.getItems();
      Pair<Integer, ItemStack> lastStack = ItemUtils.getLastStack(tableItems);
      Integer tableIndex = (Integer)lastStack.getLeft();
      ItemStack tableItem = (ItemStack)lastStack.getRight();
      boolean handEmpty = itemInHand.isEmpty();
      if (handEmpty && !tableItem.isEmpty()) {
         level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, player.getSoundSource(), 1.0F, 1.0F);
         BlockDrop.popResource(level, pos, 0.75, tableItem.copy());
         tableItems.setStackInSlot(tableIndex, ItemStack.EMPTY);
         table.refresh();
         return InteractionResult.SUCCESS;
      } else if (!handEmpty && tableIndex < tableItems.getSlots() - 1) {
         ItemStack split = itemInHand.split(1);
         if (tableItem.isEmpty()) {
            tableItems.setStackInSlot(tableIndex, split);
         } else {
            tableItems.setStackInSlot(tableIndex + 1, split);
         }

         table.refresh();
         level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, player.getSoundSource(), 1.0F, 1.0F);
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
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
            if (level.getBlockEntity(pos) instanceof TableBlockEntity tableBlockEntity) {
               level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);
               tableBlockEntity.setColor(dyeColor);
               tableBlockEntity.refresh();
               itemInHand.shrink(1);
               return InteractionResult.SUCCESS;
            }
         }

         if (hasCarpet && level.getBlockEntity(pos) instanceof TableBlockEntity tableBlockEntity && tableBlockEntity.getColor() != dyeColor) {
            DyeColor originalColor = tableBlockEntity.getColor();
            ItemStack carpetItem = CarpetColor.getCarpetByColor(originalColor).getDefaultInstance();
            BlockDrop.popResource(level, pos, 0.75, carpetItem);
            level.playSound(null, pos, SoundType.WOOL.getPlaceSound(), player.getSoundSource(), 1.0F, 1.0F);
            tableBlockEntity.setColor(dyeColor);
            tableBlockEntity.refresh();
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(HAS_CARPET, true));
            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
          } else {
             return InteractionResult.SUCCESS;
          }
       }
   }

   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide() && player.isCreative() && level.getBlockEntity(pos) instanceof TableBlockEntity tableBlockEntity) {
         ItemStackHandler items = tableBlockEntity.getItems();

         for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stack = items.getStackInSlot(i);
            if (!stack.isEmpty()) {
               popResource(level, pos, stack);
               items.setStackInSlot(i, ItemStack.EMPTY);
            }
         }
      }

      return super.playerWillDestroy(level, pos, state, player);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof TableBlockEntity tableBlockEntity) {
         if ((Boolean)state.getValue(HAS_CARPET)) {
            Item carpet = CarpetColor.getCarpetByColor(tableBlockEntity.getColor());
            drops.add(new ItemStack(carpet));
         }

         ItemStackHandler items = tableBlockEntity.getItems();

         for (int i = 0; i < items.getSlots(); i++) {
            drops.add(items.getStackInSlot(i).copy());
         }
      }

      return drops;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{AXIS, POSITION, HAS_CARPET, WATERLOGGED});
   }

   private BlockState checkEastWestState(net.minecraft.world.level.LevelReader levelAccessor, BlockPos pos, BlockState baseState) {
      if (baseState.getValue(AXIS) == Axis.Z && (Integer)baseState.getValue(POSITION) != 0) {
         return baseState;
      } else {
         BlockState westState = levelAccessor.getBlockState(pos.west());
         BlockState eastState = levelAccessor.getBlockState(pos.east());
         if (this.checkIfShouldLink(eastState, Axis.Z) && this.checkIfShouldLink(westState, Axis.Z)) {
            return (BlockState)((BlockState)baseState.setValue(POSITION, 2)).setValue(AXIS, Axis.X);
         } else if (this.checkIfShouldLink(eastState, Axis.Z) && !this.checkIfShouldLink(westState, Axis.Z)) {
            return (BlockState)((BlockState)baseState.setValue(POSITION, 1)).setValue(AXIS, Axis.X);
         } else {
            return !this.checkIfShouldLink(eastState, Axis.Z) && this.checkIfShouldLink(westState, Axis.Z)
               ? (BlockState)((BlockState)baseState.setValue(POSITION, 3)).setValue(AXIS, Axis.X)
               : (BlockState)baseState.setValue(POSITION, 0);
         }
      }
   }

   private BlockState checkNorthSouthState(net.minecraft.world.level.LevelReader levelAccessor, BlockPos pos, BlockState baseState) {
      if (baseState.getValue(AXIS) == Axis.X && (Integer)baseState.getValue(POSITION) != 0) {
         return baseState;
      } else {
         BlockState northState = levelAccessor.getBlockState(pos.north());
         BlockState southState = levelAccessor.getBlockState(pos.south());
         if (this.checkIfShouldLink(southState, Axis.X) && this.checkIfShouldLink(northState, Axis.X)) {
            return (BlockState)((BlockState)baseState.setValue(POSITION, 2)).setValue(AXIS, Axis.Z);
         } else if (this.checkIfShouldLink(southState, Axis.X) && !this.checkIfShouldLink(northState, Axis.X)) {
            return (BlockState)((BlockState)baseState.setValue(POSITION, 1)).setValue(AXIS, Axis.Z);
         } else {
            return !this.checkIfShouldLink(southState, Axis.X) && this.checkIfShouldLink(northState, Axis.X)
               ? (BlockState)((BlockState)baseState.setValue(POSITION, 3)).setValue(AXIS, Axis.Z)
               : (BlockState)baseState.setValue(POSITION, 0);
         }
      }
   }

   private boolean checkIfShouldLink(BlockState state, Axis axis) {
      if (!state.is(this)) {
         return false;
      } else {
         return state.getValue(AXIS) == axis ? (Integer)state.getValue(POSITION) == 0 : true;
      }
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      if (direction.getAxis() == Axis.X) {
         return this.checkEastWestState(levelAccessor, pos, state);
      } else {
         return direction.getAxis() == Axis.Z ? this.checkNorthSouthState(levelAccessor, pos, state) : state;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      Level level = context.getLevel();
      BlockPos clickedPos = context.getClickedPos();
      Direction direction = context.getHorizontalDirection();
      boolean hasWater = level.getFluidState(clickedPos).getType() == Fluids.WATER;
      BlockState base = (BlockState)this.defaultBlockState().setValue(WATERLOGGED, hasWater);
      if (direction.getAxis() == Axis.X) {
         return this.checkNorthSouthState(level, clickedPos, base);
      } else {
         return direction.getAxis() == Axis.Z ? this.checkEastWestState(level, clickedPos, base) : base;
      }
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return FACE;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TableBlockEntity(pos, state);
   }

   public boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
      return false;
   }
}
