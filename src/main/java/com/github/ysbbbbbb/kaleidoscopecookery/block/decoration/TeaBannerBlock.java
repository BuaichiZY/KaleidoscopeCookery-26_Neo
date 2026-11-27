package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TeaBannerBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock {
   private static final VoxelShape FLOOR_SHAPE = box(4, 0, 4, 12, 16, 12);
   private static final VoxelShape NORTH_SOUTH_SHAPE = box(0, 7, 4, 16, 15, 12);
   private static final VoxelShape EAST_WEST_SHAPE = box(4, 7, 0, 12, 15, 16);
   private static final MapCodec<TeaBannerBlock> CODEC = simpleCodec(p -> new TeaBannerBlock());

   public TeaBannerBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.COLOR_RED).strength(1.0F)
         .noCollision().noOcclusion().sound(SoundType.WOOD));
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.FLOOR));
   }

   @Override
   protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
      if (!(level.getBlockEntity(pos) instanceof TeaBannerBlockEntity banner)) return InteractionResult.TRY_WITH_EMPTY_HAND;
      DyeColor dye = stack.get(DataComponents.DYE);
      if (dye != null) {
         if (banner.getColor() == dye) return InteractionResult.TRY_WITH_EMPTY_HAND;
         if (!level.isClientSide()) {
            banner.setColor(dye);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
         }
         return InteractionResult.SUCCESS;
      }
      if (TeaBannerBlockEntity.isSupportedPattern(stack)) {
         if (ItemStack.isSameItemSameComponents(stack, banner.getPatternItem())) return InteractionResult.TRY_WITH_EMPTY_HAND;
         if (!level.isClientSide()) {
            ItemStack previous = banner.setPatternItem(stack.copyWithCount(1));
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!previous.isEmpty()) popResource(level, pos, previous);
            level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
         }
         return InteractionResult.SUCCESS;
      }
      if (stack.is(Items.SHEARS) && banner.hasPattern()) {
         if (!level.isClientSide()) {
            popResource(level, pos, banner.removePatternItem());
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
         }
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.TRY_WITH_EMPTY_HAND;
   }

   @Override protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() { return CODEC; }

   @Override
   public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockState state = super.getStateForPlacement(context);
      return state == null || state.getValue(FACE) == AttachFace.CEILING ? null : state;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      if (state.getValue(FACE) == AttachFace.FLOOR) return FLOOR_SHAPE;
      return state.getValue(FACING).getAxis() == Direction.Axis.Z ? NORTH_SOUTH_SHAPE : EAST_WEST_SHAPE;
   }

   @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }

   @Override
   public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
      return level.getBlockEntity(pos) instanceof TeaBannerBlockEntity banner
         ? banner.createItemStack(level) : super.getCloneItemStack(level, pos, state, includeData, player);
   }

   @Override
   public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
      List<ItemStack> drops = super.getDrops(state, builder);
      BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (blockEntity instanceof TeaBannerBlockEntity banner) {
         drops.removeIf(stack -> stack.is(this.asItem()));
         drops.add(banner.createItemStack(banner.getLevel()));
      }
      return drops;
   }

   @Override protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) { builder.add(FACING, FACE); }
   @Override public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new TeaBannerBlockEntity(pos, state); }
}
