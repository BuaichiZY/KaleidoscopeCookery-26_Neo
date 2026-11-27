package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class TeaBannerBlockEntity extends BaseBlockEntity {
   public static final String COLOR_TAG = "BaseColor";
   public static final String PATTERN_ITEM_TAG = "PatternItem";
   private DyeColor color = DyeColor.RED;
   private ItemStack patternItem = ItemStack.EMPTY;

   public TeaBannerBlockEntity(BlockPos pos, BlockState state) {
      super(ModBlocks.TEA_BANNER_BE.get(), pos, state);
   }

   @Override
   protected void saveAdditional(ValueOutput output) {
      super.saveAdditional(output);
      output.putInt(COLOR_TAG, this.color.getId());
      output.store(PATTERN_ITEM_TAG, ItemStack.OPTIONAL_CODEC, this.patternItem);
   }

   @Override
   public void loadAdditional(ValueInput input) {
      super.loadAdditional(input);
      this.color = DyeColor.byId(input.getIntOr(COLOR_TAG, DyeColor.RED.getId()));
      this.patternItem = input.read(PATTERN_ITEM_TAG, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      if (!isSupportedPattern(this.patternItem)) this.patternItem = ItemStack.EMPTY;
   }

   public DyeColor getColor() { return this.color; }
   public void setColor(DyeColor color) { this.color = color; this.refresh(); }
   public ItemStack getPatternItem() { return this.patternItem; }
   public boolean hasPattern() { return !this.patternItem.isEmpty(); }

   public ItemStack setPatternItem(ItemStack pattern) {
      ItemStack previous = this.patternItem;
      this.patternItem = pattern.copyWithCount(1);
      this.refresh();
      return previous;
   }

   public ItemStack removePatternItem() {
      ItemStack previous = this.patternItem;
      this.patternItem = ItemStack.EMPTY;
      this.refresh();
      return previous;
   }

   public ItemStack createItemStack(@Nullable LevelReader level) {
      ItemStack stack = new ItemStack(ModItems.TEA_BANNER.get());
      if (level != null) {
         TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
         this.saveAdditional(output);
         BlockItem.setBlockEntityData(stack, ModBlocks.TEA_BANNER_BE.get(), output);
      }
      return stack;
   }

   public static DyeColor getColor(CompoundTag tag) {
      return DyeColor.byId(tag.getIntOr(COLOR_TAG, DyeColor.RED.getId()));
   }

   public static ItemStack getPatternItem(CompoundTag tag, LevelReader level) {
      return getPatternItem(tag, level.registryAccess());
   }

   public static ItemStack getPatternItem(CompoundTag tag, HolderLookup.Provider registries) {
      ItemStack pattern = TagValueInput.create(ProblemReporter.DISCARDING, registries, tag)
         .read(PATTERN_ITEM_TAG, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
      return isSupportedPattern(pattern) ? pattern : ItemStack.EMPTY;
   }

   public static boolean isSupportedPattern(ItemStack stack) { return getPatternTexture(stack) != null; }

   @Nullable
   public static String getPatternTexture(ItemStack stack) {
      if (stack.is(Items.CREEPER_BANNER_PATTERN)) return "creeper";
      if (stack.is(Items.SKULL_BANNER_PATTERN)) return "skull";
      if (stack.is(Items.FLOWER_BANNER_PATTERN)) return "flower";
      if (stack.is(Items.MOJANG_BANNER_PATTERN)) return "mojang";
      if (stack.is(Items.GLOBE_BANNER_PATTERN)) return "globe";
      if (stack.is(Items.PIGLIN_BANNER_PATTERN)) return "piglin";
      if (stack.is(Items.FLOW_BANNER_PATTERN)) return "flow";
      if (stack.is(Items.GUSTER_BANNER_PATTERN)) return "guster";
      return null;
   }
}
