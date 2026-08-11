package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class HoeUseEvent {
   @SubscribeEvent
   public static void onRightClickBlock(RightClickBlock event) {
      Level level = event.getLevel();
      BlockPos pos = event.getPos();
      Player player = event.getEntity();
      ItemStack stack = event.getItemStack();
      if (stack.getItem() instanceof HoeItem) {
         BlockState state = level.getBlockState(pos);
         Block block = state.getBlock();
         if (block == Blocks.DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.DIRT_PATH) {
            BlockPos above = pos.above();
            FluidState fluidState = level.getFluidState(above);
            boolean isWater = fluidState.is(FluidTags.WATER);
            if (isWater) {
               if (!level.isClientSide()) {
                  level.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
                  level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                  stack.hurtAndBreak(
                     1,
                     player,
                     event.getHand() == InteractionHand.MAIN_HAND
                        ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                        : net.minecraft.world.entity.EquipmentSlot.OFFHAND
                  );
                  ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "use_hoe_on_water_field");
               }

               event.setCanceled(true);
               event.setCancellationResult(InteractionResult.SUCCESS);
            }
         }
      }
   }
}
