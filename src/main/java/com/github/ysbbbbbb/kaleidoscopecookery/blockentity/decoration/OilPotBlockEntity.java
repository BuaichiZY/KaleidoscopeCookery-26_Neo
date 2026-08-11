package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.OilPotHandler;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class OilPotBlockEntity extends BaseBlockEntity {
   public static final int MAX_OIL_COUNT = 256;
   private static final String OIL_COUNT = "OilCount";
   private final OilPotHandler invHandler = new OilPotHandler(this);
   private int oilCount = 0;

   public OilPotBlockEntity(BlockPos pos, BlockState state) {
      super(ModBlocks.OIL_POT_BE.get(), pos, state);
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.putInt("OilCount", this.oilCount);
   }

   public void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      if (tag.keySet().contains("OilCount")) {
         this.oilCount = tag.getIntOr("OilCount", 0);
         this.updateCap();
      }
   }

   public int getOilCount() {
      return this.oilCount;
   }

   public void updateCap() {
      this.invHandler.setOilCount(this.oilCount);
   }

   public void setOilCountWithoutCapUpdate(int oilCount) {
      if (this.oilCount != oilCount) {
         this.oilCount = oilCount;
         this.refresh();
      }

      if (this.level != null) {
         BlockState state = this.getBlockState();
         boolean hasOil = (Boolean)state.getValue(OilPotBlock.HAS_OIL);
         if (!hasOil && oilCount > 0) {
            this.level.setBlock(this.worldPosition, (BlockState)state.setValue(OilPotBlock.HAS_OIL, true), 3);
         } else {
            if (hasOil && oilCount <= 0) {
               this.level.setBlock(this.worldPosition, (BlockState)state.setValue(OilPotBlock.HAS_OIL, false), 3);
            }
         }
      }
   }

   public void setOilCount(int oilCount) {
      this.setOilCountWithoutCapUpdate(oilCount);
      this.updateCap();
   }

   @Nullable
   public ResourceHandler<ItemResource> createHandler() {
      BlockState state = this.getBlockState();
      return state.getBlock() instanceof OilPotBlock ? this.invHandler : null;
   }
}
