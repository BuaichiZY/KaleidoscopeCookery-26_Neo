package com.github.ysbbbbbb.kaleidoscopecookery.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class WarmthEffect extends BaseEffect {
   public WarmthEffect(int color) {
      super(color);
   }

   @Override
   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return duration % 20 == 0;
   }

   @Override
   public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
      if (livingEntity.getHealth() >= livingEntity.getMaxHealth()) {
         return true;
      } else {
         MutableBlockPos mutable = livingEntity.blockPosition().mutable();

         for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 1; y++) {
               for (int z = -2; z <= 2; z++) {
                  BlockState blockState = livingEntity.level().getBlockState(mutable.offset(x, y, z));
                  boolean hasLit = blockState.hasProperty(BlockStateProperties.LIT) && (Boolean)blockState.getValue(BlockStateProperties.LIT);
                  if (hasLit || blockState.is(TagMod.WARMTH_HEAT_SOURCE_BLOCKS)) {
                     livingEntity.heal(1.0F);
                     return true;
                  }
               }
            }
         }

         if (livingEntity.level().dimension().equals(Level.NETHER) && livingEntity.getRandom().nextInt(4) == 0) {
            livingEntity.heal(0.5F);
         }

         return true;
      }
   }
}
