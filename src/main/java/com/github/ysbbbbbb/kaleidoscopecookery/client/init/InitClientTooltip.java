package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.client.tooltip.ClientItemContainerTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.client.tooltip.ClientRecipeItemTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class InitClientTooltip {
   @SubscribeEvent
   public static void onRegisterClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
      event.register(ItemContainerTooltip.class, ClientItemContainerTooltip::new);
      event.register(RecipeItemTooltip.class, ClientRecipeItemTooltip::new);
   }
}
