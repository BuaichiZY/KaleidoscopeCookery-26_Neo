package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class OilPotHandler extends ItemStacksResourceHandler {
   private final OilPotBlockEntity oilPot;

   public OilPotHandler(OilPotBlockEntity oilPot) {
      super(1);
      this.oilPot = oilPot;
      int count = oilPot.getOilCount();
      if (count > 0) {
         this.set(0, ItemResource.of(ModItems.OIL.get()), count);
      }
   }

   public void setOilCount(int count) {
      this.set(0, count > 0 ? ItemResource.of(ModItems.OIL.get()) : ItemResource.EMPTY, count);
   }

   @Override
   public boolean isValid(int slot, ItemResource resource) {
      return resource.is((Item)ModItems.OIL.get());
   }

   @Override
   protected int getCapacity(int slot, ItemResource resource) {
      return 256;
   }

   @Override
   protected void onContentsChanged(int slot, net.minecraft.world.item.ItemStack previousContents) {
      this.oilPot.setOilCountWithoutCapUpdate(this.getAmountAsInt(slot));
   }
}
