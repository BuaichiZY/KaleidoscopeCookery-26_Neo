package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;
import net.minecraft.world.level.storage.ValueInput;

import net.minecraft.world.level.storage.ValueOutput;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class RecipeBlockEntity extends BaseBlockEntity {
   private static final String SHOW_ITEMS = "ShowItems";
   private ItemStackHandler items = new ItemStackHandler(1);

   public RecipeBlockEntity(BlockPos pos, BlockState blockState) {
      super(ModBlocks.RECIPE_BLOCK_BE.get(), pos, blockState);
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.putChild("ShowItems", this.items);
   }

   public void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      if (tag.keySet().contains("ShowItems")) {
tag.readChild("ShowItems",          this.items);
      }
   }

   public ItemStackHandler getItems() {
      return this.items;
   }
}
