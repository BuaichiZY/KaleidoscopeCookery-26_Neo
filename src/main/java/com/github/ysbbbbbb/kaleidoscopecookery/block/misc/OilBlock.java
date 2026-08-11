package com.github.ysbbbbbb.kaleidoscopecookery.block.misc;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class OilBlock extends Block {
   public OilBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.ICE).friction(0.985F).sound(SoundType.SLIME_BLOCK).noOcclusion().isValidSpawn(OilBlock::never));
   }

   private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entityType) {
      return false;
   }

   public boolean isFireSource(BlockState state, LevelReader levelReader, BlockPos pos, Direction direction) {
      return direction == Direction.UP;
   }

   public boolean isStickyBlock(BlockState state) {
      return false;
   }

   public boolean canStickTo(BlockState state, BlockState otherState) {
      return false;
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      if (random.nextInt(50) == 0) {
         double x = pos.getX() + 0.5;
         double y = pos.getY() + 1;
         double z = pos.getZ() + 0.5;
         level.addParticle(
            ParticleTypes.WAX_OFF,
            x + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            y + random.nextDouble() / 3.0,
            z + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
            0.3,
            0.1,
            0.3
         );
      }
   }
}
