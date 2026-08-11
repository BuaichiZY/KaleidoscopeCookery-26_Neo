package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class SickleHarvestEvent extends PlayerEvent implements ICancellableEvent {
   private final ItemStack sickle;
   private final BlockPos harvestPos;
   private final BlockState harvestState;
   private boolean costDurability = false;

   public SickleHarvestEvent(Player player, ItemStack sickle, BlockPos harvestPos, BlockState harvestState) {
      super(player);
      this.sickle = sickle;
      this.harvestPos = harvestPos;
      this.harvestState = harvestState;
   }

   public ItemStack getSickle() {
      return this.sickle;
   }

   public BlockPos getHarvestPos() {
      return this.harvestPos;
   }

   public BlockState getHarvestState() {
      return this.harvestState;
   }

   public boolean isCostDurability() {
      return this.costDurability;
   }

   public void setCostDurability(boolean costDurability) {
      this.costDurability = costDurability;
   }
}
