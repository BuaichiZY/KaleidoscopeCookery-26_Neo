package com.github.ysbbbbbb.kaleidoscopecookery.entity.ai;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.level.LevelReader;

public class CatLieOnBlockGoal extends MoveToBlockGoal {
   private final Cat cat;

   public CatLieOnBlockGoal(Cat cat, double speedModifier, int searchRange) {
      super(cat, speedModifier, searchRange, 6);
      this.cat = cat;
      this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
   }

   public boolean canUse() {
      return this.cat.isTame() && !this.cat.isOrderedToSit() && !this.cat.isLying() && super.canUse();
   }

   public void start() {
      super.start();
      this.cat.setInSittingPose(false);
   }

   protected int nextStartTick(PathfinderMob mob) {
      return 40;
   }

   public void stop() {
      super.stop();
      this.cat.setLying(false);
   }

   public double acceptedDistance() {
      return 1.5;
   }

   public void tick() {
      super.tick();
      this.cat.setInSittingPose(false);
      if (!this.isReachedTarget()) {
         this.cat.setLying(false);
      } else if (!this.cat.isLying()) {
         this.cat.setLying(true);
         this.cat.setPos(this.blockPos.getX() + 0.5, this.blockPos.getY() + 0.5, this.blockPos.getZ() + 0.5);
      }
   }

   protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
      return pLevel.isEmptyBlock(pPos.above()) && pLevel.getBlockState(pPos).is(TagMod.CAT_LIE_ON_BLOCKS);
   }
}
