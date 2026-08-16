package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.tooltip;

import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimeClientTooltipRegistration {
   private RuntimeClientTooltipRegistration() {
   }

   @SubscribeEvent
   public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
      event.register(RecipeItemTooltip.class, RuntimeRecipeItemTooltip::new);
      event.register(ItemContainerTooltip.class, RuntimeItemContainerTooltip::new);
   }
}
