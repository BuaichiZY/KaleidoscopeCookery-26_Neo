package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimeTeapotOverlay implements GuiLayer {
   private static final Identifier ID = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "teapot_info");

   @SubscribeEvent
   public static void register(RegisterGuiLayersEvent event) {
      event.registerAbove(VanillaGuiLayers.CROSSHAIR, ID, new RuntimeTeapotOverlay());
   }

   @Override
   public void render(GuiGraphicsExtractor gui, DeltaTracker deltaTracker) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null || !(minecraft.hitResult instanceof BlockHitResult hit)) {
         return;
      }
      BlockPos pos = hit.getBlockPos();
      if (!minecraft.player.level().getBlockState(pos).is(ModBlocks.TEAPOT.get())
         || !(minecraft.player.level().getBlockEntity(pos) instanceof TeapotBlockEntity teapot)) {
         return;
      }

      int centerX = gui.guiWidth() / 2;
      int startY = gui.guiHeight() / 2 + 13;
      gui.centeredText(minecraft.font, teapot.getStatusText(), centerX, startY, 0xFFFFFF);
      Component detail = this.getDetail(teapot);
      if (detail != null) {
         gui.centeredText(minecraft.font, detail, centerX, startY + 11, 0xE8E8E8);
      }
   }

   private Component getDetail(TeapotBlockEntity teapot) {
      if (teapot.getStatus() == 0) {
         Component fluidText;
         if (teapot.getTeaFluidId().equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
            fluidText = Component.translatable("mco.configure.world.slot.empty");
         } else {
            Fluid fluid = BuiltInRegistries.FLUID.getValue(teapot.getTeaFluidId());
            fluidText = Component.translatable(fluid.getFluidType().getDescriptionId());
         }
         return Component.translatable(
            "tooltip.kaleidoscope_cookery.teapot.statue.fluid_ingredient",
            fluidText,
            stackText(teapot.getInput())
         );
      }
      if (teapot.getStatus() == 2) {
         return Component.translatable(
            "tooltip.kaleidoscope_cookery.teapot.statue.result",
            stackText(teapot.getResult())
         );
      }
      return null;
   }

   private static Component stackText(ItemStack stack) {
      return stack.isEmpty()
         ? Component.translatable("mco.configure.world.slot.empty")
         : stack.getHoverName().copy().append(Component.literal(" ×" + stack.getCount()));
   }
}
