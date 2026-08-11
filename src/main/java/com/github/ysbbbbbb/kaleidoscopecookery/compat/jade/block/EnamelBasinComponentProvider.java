package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EnamelBasinComponentProvider implements IBlockComponentProvider {
   INSTANCE;

   public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
      BlockState blockState = accessor.getBlockState();
      int oilCount = (Integer)blockState.getValue(EnamelBasinBlock.OIL_COUNT);
      MutableComponent info = Component.translatable("jade.kaleidoscope_cookery.enamel_basin.oil_count", new Object[]{oilCount});
      tooltip.add(info);
   }

   public Identifier getUid() {
      return ModPlugin.ENAMEL_BASIN;
   }
}
