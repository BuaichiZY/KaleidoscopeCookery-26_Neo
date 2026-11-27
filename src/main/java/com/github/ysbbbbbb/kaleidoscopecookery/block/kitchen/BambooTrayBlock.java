package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IBambooTray;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.BambooTrayBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BambooTrayBlock extends Block implements EntityBlock {
    public static final BooleanProperty STAND = BooleanProperty.create("stand");
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    public BambooTrayBlock() {
        super(ModRegistrationProperties.blockProperties()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(0.5F)
                .sound(SoundType.BAMBOO)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any().setValue(STAND, false));
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || hitResult.getDirection() != Direction.UP) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!(level.getBlockEntity(pos) instanceof IBambooTray tray)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        // 禁止自己摆放为物品
        if (player.getMainHandItem().is(this.asItem())) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        double localX = hitResult.getLocation().x - pos.getX();
        double localZ = hitResult.getLocation().z - pos.getZ();
        int slot = (localZ >= 0.5 ? 2 : 0) + (localX >= 0.5 ? 1 : 0);

        ItemStack held = player.getMainHandItem();
        boolean handled = held.isEmpty()
                ? tray.onTakeOut(level, player, slot, player.isShiftKeyDown())
                : tray.onPutItem(level, player, held, slot);
        return handled ? InteractionResult.SUCCESS : InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
                                  BlockPos pos, Direction direction, BlockPos neighborPos,
                                  BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN) {
            return state.setValue(STAND, neighborState.is(this));
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAND);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos below = context.getClickedPos().below();
        boolean sameBlock = context.getLevel().getBlockState(below).is(this);
        return super.defaultBlockState().setValue(STAND, sameBlock);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BambooTrayBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> blockEntityType
    ) {
        return createTickerHelper(blockEntityType, ModBlocks.BAMBOO_TRAY_BE.get(), BambooTrayBlockEntity::serverTick);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> actualType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return actualType == expectedType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BambooTrayBlockEntity tray) {
            for (ItemStack stack : tray.getItems()) {
                if (!stack.isEmpty()) {
                    drops.add(stack.copy());
                }
            }
        }
        return drops;
    }
}
