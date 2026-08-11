package com.github.ysbbbbbb.kaleidoscopecookery.block.food;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FoodBlock extends Block {
   public static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);

   public FoodBlock() {
      super(ModRegistrationProperties.blockProperties().forceSolidOn().instabreak().mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).noOcclusion());
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return AABB;
   }
}
