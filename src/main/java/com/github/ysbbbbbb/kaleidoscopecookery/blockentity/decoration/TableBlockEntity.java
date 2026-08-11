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
import net.neoforged.neoforge.items.ItemStackHandler;

public class TableBlockEntity extends BaseBlockEntity {
   private static final String COLOR_TAG = "CarpetColor";
   private static final String SHOW_ITEMS = "ShowItems";
   private DyeColor color = DyeColor.WHITE;
   private ItemStackHandler items = new ItemStackHandler(4);

   public TableBlockEntity(BlockPos pos, BlockState blockState) {
      super(ModBlocks.TABLE_BE.get(), pos, blockState);
   }

   protected void saveAdditional(ValueOutput tag) {
      super.saveAdditional(tag);
      tag.putInt("CarpetColor", this.color.getId());
      tag.putChild("ShowItems", this.items);
   }

   protected void loadAdditional(ValueInput tag) {
      super.loadAdditional(tag);
      if (tag.keySet().contains("CarpetColor")) {
         this.color = DyeColor.byId(tag.getIntOr("CarpetColor", 0));
      }

      if (tag.keySet().contains("ShowItems")) {
tag.readChild("ShowItems",          this.items);
      }
   }

   public DyeColor getColor() {
      return this.color;
   }

   public void setColor(DyeColor color) {
      this.color = color;
   }

   public ItemStackHandler getItems() {
      return this.items;
   }
}
