package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.NinePart;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

public enum RuntimeMillstoneComponentProvider implements IBlockComponentProvider {
   INSTANCE;

   @Override
   public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
      NinePart part = accessor.getBlockState().getValue(MillstoneBlock.PART);
      BlockPos centerPos = accessor.getPosition().subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
      if (!(accessor.getLevel().getBlockEntity(centerPos) instanceof MillstoneBlockEntity millstone)) {
         return;
      }

      ItemStack input = millstone.getInput();
      if (input.isEmpty() && millstone.isOutputEmpty()) {
         return;
      }

      if (!input.isEmpty()) {
         tooltip.add(JadeUI.item(input));
         tooltip.append(JadeUI.progressArrow(millstone.getProgressPercent()));
         ItemStack previewOutput = millstone.getPreviewOutput();
         if (!previewOutput.isEmpty()) {
            tooltip.append(JadeUI.item(previewOutput));
         }
      }

      for (int i = 0; i < millstone.getOutputs().getSlots(); i++) {
         ItemStack output = millstone.getOutputs().getStackInSlot(i);
         if (!output.isEmpty()) {
            tooltip.append(JadeUI.item(output));
         }
      }
   }

   @Override
   public Identifier getUid() {
      return RuntimeJadePlugin.MILLSTONE;
   }
}
