package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Recreates the original first-person dough-pulling render in one atomic hook.
 * Rendering and cancellation must happen at the same level on 26.1.2; doing
 * them from RenderHandEvent allowed the normal right arm/item pass to survive.
 */
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
   @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
   private void renderPulledDoughWithoutArm(
      AbstractClientPlayer player,
      float frameInterp,
      float xRot,
      InteractionHand hand,
      float attack,
      ItemStack itemStack,
      float inverseArmHeight,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int lightCoords,
      CallbackInfo ci
   ) {
      if (!player.isUsingItem()
         || player.getUseItem().getItem() != ModItems.RAW_DOUGH.get()
         || player.getUsedItemHand() != hand) {
         return;
      }

      int ticks = player.getTicksUsingItem();
      int stage = ticks >= 30 ? 4 : ticks >= 20 ? 3 : ticks >= 10 ? 2 : ticks >= 1 ? 1 : 0;
      ItemStack rendered = itemStack.copy();
      rendered.set(
         DataComponents.ITEM_MODEL,
         Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "raw_dough_stage_" + stage)
      );

      HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
      boolean right = arm == HumanoidArm.RIGHT;
      float sign = right ? 1.0F : -1.0F;

      poseStack.pushPose();
      // These are the v29/original transforms.  Do not apply the normal arm
      // transform afterwards: the centred model replaces that entire pass.
      poseStack.translate(sign * 0.56F, -0.52F + inverseArmHeight * -0.6F, -0.72F);
      poseStack.translate(sign * -0.2785682F, 0.18344387F, 0.15731531F);
      poseStack.mulPose(Axis.XP.rotationDegrees(-13.935F));
      poseStack.mulPose(Axis.YP.rotationDegrees(sign * 35.3F));
      poseStack.mulPose(Axis.ZP.rotationDegrees(sign * -9.785F));

      float held = player.getTicksUsingItem() + frameInterp;
      float power = held / 20.0F;
      power = (power * power + power * 2.0F) / 3.0F;
      power = Math.min(power, 1.0F);
      if (power > 0.1F) {
         float shake = Mth.sin((held - 0.1F) * 1.3F) * (power - 0.1F);
         poseStack.translate(0.0F, shake * 0.004F, 0.0F);
      }
      poseStack.translate(0.0F, 0.0F, power * 0.04F);
      poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
      poseStack.mulPose(Axis.YN.rotationDegrees(sign * 45.0F));

      ItemStackRenderState state = new ItemStackRenderState();
      Minecraft.getInstance().getItemModelResolver().updateForTopItem(
         state,
         rendered,
         right ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
         player.level(),
         player,
         player.getId()
      );
      state.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
      poseStack.popPose();

      // Suppress the vanilla arm and the ordinary held raw-dough copy.
      ci.cancel();
   }
}
