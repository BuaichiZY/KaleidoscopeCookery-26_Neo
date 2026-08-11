package com.github.ysbbbbbb.kaleidoscopecookery.block.misc;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class StrawBlocks extends RotatedPillarBlock {
   public StrawBlocks() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(0.5F).sound(SoundType.GRASS));
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AXIS, Axis.Y));
   }

   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
      if (!level.isClientSide()) {
         if (entity instanceof LivingEntity) {
            level.playSound(null, pos, SoundEvents.GRASS_FALL, entity.getSoundSource(), 1.0F, 1.0F);
            if (!(fallDistance < 3.0F)) {
               float possibility = Mth.clamp(fallDistance / 30.0F, 0.0F, 1.0F);
               if (level.getRandom().nextFloat() < possibility) {
                  level.destroyBlock(pos, false);
                  popResource(level, pos, new ItemStack((ItemLike)ModItems.RICE_PANICLE.get(), 5));
                  popResource(level, pos, new ItemStack((ItemLike)ModItems.RICE_SEED.get(), 4));
                  if (level instanceof ServerLevel serverLevel) {
                     serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.1, 0.1, 0.1, 0.05);
                  }
               }
            }
         }
      }
   }
}
