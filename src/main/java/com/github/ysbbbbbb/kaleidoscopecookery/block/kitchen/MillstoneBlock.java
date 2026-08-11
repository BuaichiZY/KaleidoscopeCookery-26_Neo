package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IMillstone;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MillstoneBlock extends HorizontalDirectionalBlock implements EntityBlock {
   public static final MapCodec<MillstoneBlock> CODEC = simpleCodec(p -> new MillstoneBlock());
   public static final EnumProperty<NinePart> PART = EnumProperty.create("part", NinePart.class);
   private static final VoxelShape CENTER = Block.box(-2.0, 0.0, -2.0, 18.0, 15.0, 18.0);
   private static final VoxelShape LEFT_UP = Shapes.or(Block.box(11.0, 0.0, 11.0, 16.0, 6.0, 16.0), Block.box(8.0, 6.0, 8.0, 16.0, 14.0, 16.0));
   private static final VoxelShape UP = Shapes.or(Block.box(0.0, 0.0, 11.0, 16.0, 6.0, 16.0), Block.box(0.0, 6.0, 8.0, 16.0, 14.0, 16.0));
   private static final VoxelShape RIGHT_UP = Shapes.or(Block.box(0.0, 0.0, 11.0, 5.0, 6.0, 16.0), Block.box(0.0, 6.0, 8.0, 8.0, 14.0, 16.0));
   private static final VoxelShape LEFT_CENTER = Shapes.or(Block.box(11.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(8.0, 6.0, 0.0, 16.0, 14.0, 16.0));
   private static final VoxelShape RIGHT_CENTER = Shapes.or(Block.box(0.0, 0.0, 0.0, 5.0, 6.0, 16.0), Block.box(0.0, 6.0, 0.0, 8.0, 14.0, 16.0));
   private static final VoxelShape LEFT_DOWN = Shapes.or(Block.box(11.0, 0.0, 0.0, 16.0, 6.0, 5.0), Block.box(8.0, 6.0, 0.0, 16.0, 14.0, 8.0));
   private static final VoxelShape DOWN = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 5.0), Block.box(0.0, 6.0, 0.0, 16.0, 14.0, 8.0));
   private static final VoxelShape RIGHT_DOWN = Shapes.or(Block.box(0.0, 0.0, 0.0, 5.0, 6.0, 5.0), Block.box(0.0, 6.0, 0.0, 8.0, 14.0, 8.0));

   public MillstoneBlock() {
      super(
         ModRegistrationProperties.blockProperties()
            .mapColor(MapColor.STONE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .sound(SoundType.STONE)
            .forceSolidOn()
            .noOcclusion()
      );
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(PART, NinePart.CENTER)).setValue(FACING, Direction.NORTH)
      );
   }

   private static void handleRemove(Level world, BlockPos pos, BlockState state, @Nullable Player player) {
      if (!world.isClientSide()) {
         NinePart part = (NinePart)state.getValue(PART);
         BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
         if (world.getBlockEntity(centerPos) instanceof MillstoneBlockEntity millstone) {
            for (int i = -1; i < 2; i++) {
               for (int j = -1; j < 2; j++) {
                  BlockPos offsetPos = centerPos.offset(i, 0, j);
                  world.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 35);
               }
            }

            if (player != null && !player.isCreative()) {
               Block.popResource(world, pos, ((Item)ModItems.MILLSTONE.get()).getDefaultInstance());
            }

            for (int i = 0; i < millstone.getOutputs().getSlots(); i++) {
               ItemStack outputStack = millstone.getOutputs().getStackInSlot(i);
               if (!outputStack.isEmpty()) {
                  Block.popResource(world, pos, outputStack);
               }
            }

            if (!millstone.getInput().isEmpty()) {
               Block.popResource(world, pos, millstone.getInput());
            }
         }
      }
   }

   @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
   ) {
      return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
      return level.isClientSide()
         ? null
         : createTickerHelper(blockEntityType, ModBlocks.MILLSTONE_BE.get(), (levelIn, pos, stateIn, millstone) -> millstone.tick(levelIn));
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         NinePart part = (NinePart)state.getValue(PART);
         BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
         if (level.getBlockEntity(centerPos) instanceof IMillstone millstone) {
            ItemStack mainHandItem = player.getMainHandItem();
            return millstone.onPutItem(level, mainHandItem)
               ? InteractionResult.SUCCESS
               : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
         } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      }
   }

   public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
      if (pEntity instanceof Mob mob && !pLevel.isClientSide() && pLevel.getGameTime() % 5L == 4L) {
         NinePart part = (NinePart)pState.getValue(PART);
         BlockPos centerPos = pPos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
         if (pLevel.getBlockEntity(centerPos) instanceof MillstoneBlockEntity millstone && !millstone.hasEntity() && millstone.canBindEntity(mob)) {
            millstone.bindEntity(mob);
            if (mob instanceof OwnableEntity ownable && ownable.getOwner() instanceof ServerPlayer player) {
               ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "drive_the_millstone");
            }
         }
      }
   }

   public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
      handleRemove(world, pos, state, player);
      return super.playerWillDestroy(world, pos, state, player);
   }

   public void onBlockExploded(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, Explosion explosion) {
      handleRemove(world, pos, state, null);
      super.onBlockExploded(state, world, pos, explosion);
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos centerPos = context.getClickedPos();

      for (int i = -1; i < 2; i++) {
         for (int j = -1; j < 2; j++) {
            BlockPos searchPos = centerPos.offset(i, 0, j);
            if (!context.getLevel().getBlockState(searchPos).canBeReplaced(context)) {
               return null;
            }
         }
      }

      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      super.setPlacedBy(worldIn, pos, state, placer, stack);
      if (!worldIn.isClientSide()) {
         for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
               BlockPos searchPos = pos.offset(i, 0, j);
               NinePart part = NinePart.getPartByPos(i, j);
               if (part != null && !part.isCenter()) {
                  worldIn.setBlock(searchPos, (BlockState)state.setValue(PART, part), 3);
               }
            }
         }
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
      pBuilder.add(new Property[]{PART, FACING});
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return ((NinePart)state.getValue(PART)).isCenter() ? new MillstoneBlockEntity(pos, state) : null;
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.INVISIBLE;
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      NinePart value = (NinePart)pState.getValue(PART);

      return switch (value) {
         case LEFT_UP -> LEFT_UP;
         case UP -> UP;
         case RIGHT_UP -> RIGHT_UP;
         case LEFT_CENTER -> LEFT_CENTER;
         case CENTER -> CENTER;
         case RIGHT_CENTER -> RIGHT_CENTER;
         case LEFT_DOWN -> LEFT_DOWN;
         case DOWN -> DOWN;
         case RIGHT_DOWN -> RIGHT_DOWN;
      };
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.millstone").withStyle(ChatFormatting.GRAY));
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }
}
