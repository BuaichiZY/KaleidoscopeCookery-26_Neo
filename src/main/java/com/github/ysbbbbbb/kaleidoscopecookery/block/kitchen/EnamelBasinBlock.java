package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnamelBasinBlock extends Block implements SimpleWaterloggedBlock {
   public static final int MAX_OIL_COUNT = 32;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");
   public static final IntegerProperty OIL_COUNT = IntegerProperty.create("oil_count", 0, 32);
   private static final VoxelShape AABB_NO_LID = Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0);
   private static final VoxelShape AABB = Shapes.or(
      AABB_NO_LID, new VoxelShape[]{Block.box(2.5, 5.0, 2.5, 13.5, 6.0, 13.5), Block.box(7.0, 6.0, 7.0, 9.0, 7.0, 9.0)}
   );

   public EnamelBasinBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BELL).strength(1.0F, 1.5F).sound(SoundType.LANTERN));
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, false)).setValue(HAS_LID, true))
            .setValue(OIL_COUNT, 0)
      );
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
         super.useItemOn(stack, state, level, pos, player, hand, hitResult);
      }

      ItemStack mainHandItem = player.getMainHandItem();
      if (mainHandItem.is(Items.STICK)) {
         float pitch = 0.6F + (float)Math.random() * 0.2F;
         level.playSound(player, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 2.0F, pitch);
         return InteractionResult.SUCCESS;
      } else {
         boolean hasLid = (Boolean)state.getValue(HAS_LID);
         if (hasLid) {
            level.playSound(player, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 0.8F, 0.8F);
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(HAS_LID, false));
            return InteractionResult.SUCCESS;
         } else if (mainHandItem.isEmpty()) {
            level.playSound(player, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 0.8F, 0.4F);
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(HAS_LID, true));
            return InteractionResult.SUCCESS;
         } else if (mainHandItem.is((Item)ModItems.OIL.get())) {
            int value = (Integer)state.getValue(OIL_COUNT);
            if (value >= 32) {
               return InteractionResult.FAIL;
            } else {
               int needCount = 32 - value;
               int consumeCount = Math.min(needCount, mainHandItem.getCount());
               level.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.8F, 0.8F);
               mainHandItem.shrink(consumeCount);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(OIL_COUNT, value + consumeCount));
               return InteractionResult.SUCCESS;
            }
         } else {
            return mainHandItem.is((Item)ModItems.KITCHEN_SHOVEL.get())
               ? this.onShovelClick(state, level, pos, player, mainHandItem)
               : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
         }
      }
   }

   @NotNull
   private InteractionResult onShovelClick(BlockState state, Level level, BlockPos pos, Player player, ItemStack mainHandItem) {
      int value = (Integer)state.getValue(OIL_COUNT);
      boolean shovelHasOil = KitchenShovelItem.hasOil(mainHandItem);
      if (shovelHasOil) {
         if (value >= 32) {
            return InteractionResult.FAIL;
         } else {
            level.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.8F, 0.8F);
            KitchenShovelItem.setHasOil(mainHandItem, false);
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(OIL_COUNT, value + 1));
            return InteractionResult.SUCCESS;
         }
      } else if (value == 0) {
         return InteractionResult.FAIL;
      } else {
         level.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.8F, 1.2F);
         KitchenShovelItem.setHasOil(mainHandItem, true);
         level.setBlockAndUpdate(pos, (BlockState)state.setValue(OIL_COUNT, value - 1));
         return InteractionResult.SUCCESS;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
      return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{WATERLOGGED, HAS_LID, OIL_COUNT});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return state.getValue(HAS_LID) ? AABB : AABB_NO_LID;
   }

   public boolean hasAnalogOutputSignal(BlockState state) {
      return true;
   }

   public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
      int count = (Integer)state.getValue(OIL_COUNT);
      int baseValue = count > 0 ? 1 : 0;
      double ratio = count / 32.0;
      return Mth.floor(ratio * 14.0) + baseValue;
   }

   public List<ItemStack> getDrops(BlockState pState, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      List<ItemStack> stacks = super.getDrops(pState, params);
      BlockState state = (BlockState)params.getOptionalParameter(LootContextParams.BLOCK_STATE);
      if (state != null && state.is(this)) {
         int oilCount = (Integer)state.getValue(OIL_COUNT);
         if (oilCount > 0) {
            stacks.add(new ItemStack((ItemLike)ModItems.OIL.get(), oilCount));
         }

         return stacks;
      } else {
         return stacks;
      }
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.enamel_basin").withStyle(ChatFormatting.GRAY));
   }
}
