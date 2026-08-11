package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.NinePart;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum MillstoneComponentProvider implements IBlockComponentProvider {
   INSTANCE;

   public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
      NinePart part = (NinePart)accessor.getBlockState().getValue(MillstoneBlock.PART);
      BlockPos pos = accessor.getPosition();
      BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
      if (accessor.getLevel().getBlockEntity(centerPos) instanceof MillstoneBlockEntity millstone) {
         if (!millstone.getInput().isEmpty() || !millstone.isOutputEmpty()) {
            IElementHelper helper = IElementHelper.get();
            tooltip.add(helper.item(millstone.getInput()));
            tooltip.append(helper.progress(millstone.getProgressPercent()));

            for (int i = 0; i < millstone.getOutputs().getSlots(); i++) {
               ItemStack outputStack = millstone.getOutputs().getStackInSlot(i);
               if (!outputStack.isEmpty()) {
                  tooltip.append(helper.item(outputStack));
               }
            }
         }
      }
   }

   public Identifier getUid() {
      return ModPlugin.MILLSTONE;
   }
}
