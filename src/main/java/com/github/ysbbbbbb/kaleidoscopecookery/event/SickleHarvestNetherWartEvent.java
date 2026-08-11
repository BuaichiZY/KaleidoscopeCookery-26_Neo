package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.SickleHarvestEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class SickleHarvestNetherWartEvent {
   @SubscribeEvent
   public static void onSickleHarvestNetherWart(SickleHarvestEvent event) {
      BlockState harvestState = event.getHarvestState();
      if (harvestState.is(Blocks.NETHER_WART)) {
         boolean isMaxAge = (Integer)harvestState.getValue(NetherWartBlock.AGE) >= 3;
         if (isMaxAge) {
            Player player = event.getEntity();
            BlockPos pos = event.getHarvestPos();
            Level level = player.level();
            if (player instanceof ServerPlayer serverPlayer) {
               serverPlayer.gameMode.destroyBlock(pos);
               level.levelEvent(null, 2001, pos, Block.getId(harvestState));
               level.setBlock(pos, Blocks.NETHER_WART.defaultBlockState(), 3);
            }

            event.setCostDurability(true);
         }

         event.setCanceled(true);
      }
   }
}
