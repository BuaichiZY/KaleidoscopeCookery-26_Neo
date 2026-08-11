package com.github.ysbbbbbb.kaleidoscopecookery.block.decoration;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.util.VoxelShapeUtils;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class StackableFoodBlock extends HorizontalDirectionalBlock {
   protected static final MapCodec<StackableFoodBlock> STACKABLE_FOOD_CODEC = simpleCodec(p -> new StackableFoodBlock(p, 1, () -> Items.AIR));
   private static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 16);
   protected final IntegerProperty countProperty;
   protected final int maxCount;
   protected final Supplier<Item> item;
   protected final EnumMap<Direction, VoxelShape>[] shapes;

   public StackableFoodBlock(Properties properties, int maxCount, Supplier<Item> item, VoxelShape... shapes) {
      super(properties);
      this.maxCount = maxCount;
      this.countProperty = COUNT;
      this.item = item;
      this.shapes = new EnumMap[shapes.length];

      for (int i = 0; i < shapes.length; i++) {
         this.shapes[i] = VoxelShapeUtils.horizontalShapes(shapes[i]);
      }

      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.countProperty, 1)).setValue(FACING, Direction.NORTH)
      );
   }

   public IntegerProperty getCountProperty() {
      return this.countProperty;
   }

   public int getMaxCount() {
      return this.maxCount;
   }

   public InteractionResult useItemOn(
      ItemStack itemInHand, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         if (itemInHand.is(this.item.get())) {
            int count = (Integer)state.getValue(this.countProperty);
            if (count < this.maxCount) {
               level.setBlockAndUpdate(pos, (BlockState)state.cycle(this.countProperty));
               SoundType soundType = state.getSoundType(level, pos, player);
               SoundEvent sound = soundType.getPlaceSound();
               level.playSound(player, pos, sound, SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
               if (!player.isCreative()) {
                  itemInHand.shrink(1);
               }

               return InteractionResult.SUCCESS;
            }
         }

         if (itemInHand.isEmpty()) {
            int count = (Integer)state.getValue(this.countProperty);
            if (count > 1) {
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(this.countProperty, count - 1));
            } else {
               level.removeBlock(pos, false);
            }

            ItemHandlerHelper.giveItemToPlayer(player, this.item.get().getDefaultInstance());
            return InteractionResult.SUCCESS;
         } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      }
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      List<ItemStack> drops = Lists.newArrayList();
      int count = (Integer)state.getValue(this.countProperty);
      drops.add(new ItemStack((ItemLike)this.item.get(), count));
      return drops;
   }

   protected void createCountBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, COUNT});
   }

   @Override
   protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, COUNT);
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      if (this.shapes.length == 0) {
         return super.getShape(state, level, pos, context);
      } else {
         int count = (Integer)state.getValue(this.countProperty);
         if (count > this.shapes.length) {
            count = this.shapes.length;
         }

         Direction direction = (Direction)state.getValue(FACING);
         return this.shapes[count - 1].getOrDefault(direction, super.getShape(state, level, pos, context));
      }
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return STACKABLE_FOOD_CODEC;
   }

   public static StackableFoodBlock.Builder create() {
      return new StackableFoodBlock.Builder();
   }

   public static class Builder {
      private int maxCount;
      private Supplier<Item> item;
      private VoxelShape[] shapes;
      private Properties properties = ModRegistrationProperties.blockProperties()
         .forceSolidOn()
         .instabreak()
         .mapColor(MapColor.WOOD)
         .sound(SoundType.WOOD)
         .pushReaction(PushReaction.DESTROY)
         .noOcclusion();

      private Builder() {
      }

      public static StackableFoodBlock.Builder create() {
         return new StackableFoodBlock.Builder();
      }

      public StackableFoodBlock.Builder maxCount(int maxCount) {
         this.maxCount = maxCount;
         return this;
      }

      public StackableFoodBlock.Builder item(Supplier<Item> item) {
         this.item = item;
         return this;
      }

      public StackableFoodBlock.Builder shapes(VoxelShape... shapes) {
         this.shapes = shapes;
         return this;
      }

      public StackableFoodBlock.Builder soundType(SoundType soundType) {
         this.properties = this.properties.sound(soundType);
         return this;
      }

      public StackableFoodBlock.Builder mapColor(MapColor mapColor) {
         this.properties = this.properties.mapColor(mapColor);
         return this;
      }

      public Supplier<Block> build() {
         return () -> new StackableFoodBlock(this.properties, this.maxCount, this.item, this.shapes);
      }
   }
}
