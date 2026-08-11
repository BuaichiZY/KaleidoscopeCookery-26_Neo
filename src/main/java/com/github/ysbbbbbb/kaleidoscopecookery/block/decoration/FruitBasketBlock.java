package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FruitBasketItem;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FruitBasketBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
   public static final MapCodec<FruitBasketBlock> CODEC = simpleCodec(p -> new FruitBasketBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape NORTH_SOUTH = Shapes.join(
      Block.box(1.0, 0.0, 2.0, 15.0, 8.0, 14.0), Block.box(2.0, 1.0, 3.0, 14.0, 8.0, 13.0), BooleanOp.ONLY_FIRST
   );
   private static final VoxelShape EAST_WEST = Shapes.join(
      Block.box(2.0, 0.0, 1.0, 14.0, 8.0, 15.0), Block.box(3.0, 1.0, 2.0, 13.0, 8.0, 14.0), BooleanOp.ONLY_FIRST
   );

   public FruitBasketBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).sound(SoundType.BAMBOO));
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
      if (hand == InteractionHand.OFF_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         if (level.getBlockEntity(pos) instanceof FruitBasketBlockEntity fruitBasket) {
            if (player.isSecondaryUseActive()) {
               fruitBasket.takeOut(player);
               return InteractionResult.SUCCESS;
            }

            ItemStack mainHandItem = player.getMainHandItem();
            if (!mainHandItem.isEmpty() && !mainHandItem.is((Item)ModItems.TRANSMUTATION_LUNCH_BAG.get())) {
               fruitBasket.putOn(mainHandItem);
               return InteractionResult.SUCCESS;
            }
         }

         return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
      }
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (stack.has(ModDataComponents.FRUIT_BASKET_ITEMS) && level.getBlockEntity(pos) instanceof FruitBasketBlockEntity basket) {
         FruitBasketItem.ItemContainer handler = (FruitBasketItem.ItemContainer)stack.get(ModDataComponents.FRUIT_BASKET_ITEMS);
         if (handler != null) {
            basket.setItems(handler.items(), level.registryAccess());
         }
      }
   }

   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide() && player.isCreative()) {
         dropResources(state, level, pos, level.getBlockEntity(pos), player, player.getMainHandItem());
      }

      return super.playerWillDestroy(level, pos, state, player);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof FruitBasketBlockEntity fruitBasket) {
         drops.stream()
            .filter(stack -> stack.is((Item)ModItems.FRUIT_BASKET.get()))
            .findFirst()
            .ifPresent(stack -> stack.set(ModDataComponents.FRUIT_BASKET_ITEMS, new FruitBasketItem.ItemContainer(fruitBasket.getItems())));
      }

      return drops;
   }

   public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
      ItemStack cloneItemStack = super.getCloneItemStack(level, pos, state, includeData, player);
      level.getBlockEntity(pos, ModBlocks.FRUIT_BASKET_BE.get()).ifPresent(e -> {
         net.minecraft.world.level.storage.TagValueOutput output = net.minecraft.world.level.storage.TagValueOutput.createWithContext(
            net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess()
         );
         e.saveCustomOnly(output);
         net.minecraft.world.item.BlockItem.setBlockEntityData(cloneItemStack, ModBlocks.FRUIT_BASKET_BE.get(), output);
      });
      return cloneItemStack;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new FruitBasketBlockEntity(pos, state);
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
      return ((Direction)state.getValue(FACING)).getAxis() == Axis.Z ? NORTH_SOUTH : EAST_WEST;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.fruit_basket").withStyle(ChatFormatting.GRAY));
   }
}
