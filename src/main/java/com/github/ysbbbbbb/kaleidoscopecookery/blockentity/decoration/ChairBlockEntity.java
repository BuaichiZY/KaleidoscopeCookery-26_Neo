package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

public class ChairBlockEntity extends BaseBlockEntity {
   private static final String COLOR_TAG = "CarpetColor";
   private DyeColor color = DyeColor.WHITE;

   public ChairBlockEntity(BlockPos pos, BlockState blockState) {
      super(ModBlocks.CHAIR_BE.get(), pos, blockState);
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.putInt("CarpetColor", this.color.getId());
   }

   protected void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      this.color = DyeColor.byId(tag.getIntOr("CarpetColor", 0));
   }

   public DyeColor getColor() {
      return this.color;
   }

   public void setColor(DyeColor color) {
      this.color = color;
   }
}
