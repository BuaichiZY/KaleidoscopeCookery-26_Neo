package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IChoppingBoard;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChoppingBoardBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
   public static final MapCodec<ChoppingBoardBlock> CODEC = simpleCodec(p -> new ChoppingBoardBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final VoxelShape NORTH_SOUTH = Block.box(1.0, 0.0, 2.0, 15.0, 2.0, 14.0);
   public static final VoxelShape EAST_WEST = Block.box(2.0, 0.0, 1.0, 14.0, 2.0, 15.0);
   private static final double DURABILITY_COST_PROBABILITY = 0.25;

   public ChoppingBoardBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava());
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
      if (level.getBlockEntity(pos) instanceof IChoppingBoard choppingBoard) {
         ItemStack itemInHand = player.getItemInHand(hand);
         if (choppingBoard.onPutItem(level, player, itemInHand)) {
            return InteractionResult.SUCCESS;
         }

         ItemStack mainHandItem = player.getMainHandItem();
         if (hand == InteractionHand.OFF_HAND) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         }

         if (choppingBoard.onCutItem(level, player, itemInHand)) {
            if (level.getRandom().nextDouble() < 0.25) {
               itemInHand.hurtAndBreak(1, player, (hand == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND));
            }

            ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "use_chopping_board");
            return InteractionResult.SUCCESS;
         }

         if (player.isSecondaryUseActive() && choppingBoard.onTakeOut(level, player)) {
            return InteractionResult.SUCCESS;
         }

         if (mainHandItem.is(TagMod.KITCHEN_KNIFE) && player.getOffhandItem().isEmpty()) {
            choppingBoard.playParticlesSound();
            return InteractionResult.SUCCESS;
         }
      }

      return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new ChoppingBoardBlockEntity(pos, state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, WATERLOGGED});
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return ((Direction)state.getValue(FACING)).getAxis() == Axis.Z ? NORTH_SOUTH : EAST_WEST;
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      List<ItemStack> drops = super.getDrops(state, params);
      BlockEntity parameter = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof ChoppingBoardBlockEntity choppingBoard
         && choppingBoard.getCurrentCutCount() == 0
         && !choppingBoard.getCurrentCutStack().isEmpty()) {
         drops.add(choppingBoard.getCurrentCutStack().copyWithCount(1));
      }

      return drops;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.chopping_board").withStyle(ChatFormatting.GRAY));
   }
}
