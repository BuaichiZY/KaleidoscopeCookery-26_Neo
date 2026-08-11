package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class LeftClickEvent {
   @SubscribeEvent
   public static void onLeftClickItem(LeftClickEmpty event) {
      Player player = event.getEntity();
      if (player.isSecondaryUseActive() && event.getHand() == InteractionHand.MAIN_HAND) {
         ItemStack mainHandItem = player.getMainHandItem();
         if (mainHandItem.is((Item)ModItems.BAOZI.get())) {
            ClientPacketDistributor.sendToServer(new SimpleC2SModMessage(1));
         }
      }
   }

   @SubscribeEvent
   public static void onLeftClickBlock(LeftClickBlock event) {
      Player player = event.getEntity();
      if (player.isSecondaryUseActive() && event.getHand() == InteractionHand.MAIN_HAND) {
         ItemStack mainHandItem = player.getMainHandItem();
         if (mainHandItem.is((Item)ModItems.TEAPOT.get())) {
            TeapotItem.clearAll(mainHandItem, event.getEntity());
            event.setCanceled(true);
         }
      }
   }
}
