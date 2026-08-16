package com.github.ysbbbbbb.kaleidoscopecookery.client;

import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RawDoughItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SteamerItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.Nullable;

/**
 * Bridges the mod's legacy item predicates to Minecraft 26's item model system.
 */
@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class ClientItemModelProperties {
   private ClientItemModelProperties() {
   }

   @SubscribeEvent
   public static void register(RegisterConditionalItemModelPropertyEvent event) {
      event.register(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_oil"), HasOil.MAP_CODEC);
      event.register(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_recipe"), HasRecipe.MAP_CODEC);
      event.register(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_items"), HasItems.MAP_CODEC);
   }

   @SubscribeEvent
   public static void registerRangeProperties(RegisterRangeSelectItemModelPropertyEvent event) {
      event.register(RawDoughItem.PULL_PROPERTY, PullProgress.MAP_CODEC);
   }

   static {
      NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ClientItemModelProperties::renderRawDoughInHand);
   }

   private static void renderRawDoughInHand(RenderHandEvent event) {
      var player = Minecraft.getInstance().player;
      if (player == null
         || !player.isUsingItem()
         || player.getUseItem().getItem() != ModItems.RAW_DOUGH.get()) {
         return;
      }

      // The original 1.21.1 animation only replaces the actively used hand.
      // The optional Punchy compatibility mixin prevents its separate full-arm
      // renderer from drawing another copy before this event is fired.
      if (player.getUsedItemHand() != event.getHand()) {
         return;
      }

      int ticks = player.getTicksUsingItem();
      int stage = ticks >= 30 ? 4 : ticks >= 20 ? 3 : ticks >= 10 ? 2 : ticks >= 1 ? 1 : 0;
      ItemStack rendered = event.getItemStack().copy();
      rendered.set(
         DataComponents.ITEM_MODEL,
         Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "raw_dough_stage_" + stage)
      );

      boolean right = event.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND
         ? player.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT
         : player.getMainArm() != net.minecraft.world.entity.HumanoidArm.RIGHT;
      float sign = right ? 1.0F : -1.0F;
      var poseStack = event.getPoseStack();
      poseStack.pushPose();
      // Preserve the v29 positioning and timing, which match reference video 5.
      poseStack.translate(sign * 0.56F, -0.52F + event.getEquipProgress() * -0.6F, -0.72F);
      poseStack.translate(sign * -0.2785682F, 0.18344387F, 0.15731531F);
      poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-13.935F));
      poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(sign * 35.3F));
      poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(sign * -9.785F));

      float held = player.getTicksUsingItem() + event.getPartialTick();
      float power = held / 20.0F;
      power = (power * power + power * 2.0F) / 3.0F;
      power = Math.min(power, 1.0F);
      if (power > 0.1F) {
         float shake = net.minecraft.util.Mth.sin((held - 0.1F) * 1.3F) * (power - 0.1F);
         poseStack.translate(0.0F, shake * 0.004F, 0.0F);
      }
      poseStack.translate(0.0F, 0.0F, power * 0.04F);
      poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
      poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(sign * 45.0F));

      ItemStackRenderState state = new ItemStackRenderState();
      Minecraft.getInstance().getItemModelResolver().updateForTopItem(
         state,
         rendered,
         right ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
         player.level(),
         player,
         player.getId()
      );
      state.submit(
         poseStack,
         event.getSubmitNodeCollector(),
         event.getPackedLight(),
         net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
         0
      );
      poseStack.popPose();

      // Cancel vanilla's active-hand pass.  Punchy's earlier custom arm pass is
      // independently suppressed by PunchyRawDoughCompatMixin.
      event.setCanceled(true);
   }

   /**
    * Reproduces the original 1.21.1 raw-dough predicate.  Comparing only the
    * rendered stack by identity is unreliable in the new item-model pipeline,
    * so the active hand item is checked by item type before returning progress.
    */
   private static final class PullProgress implements net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty {
      private static final PullProgress INSTANCE = new PullProgress();
      private static final MapCodec<PullProgress> MAP_CODEC = MapCodec.unit(INSTANCE);

      @Override
      public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
         LivingEntity entity = owner == null ? null : owner.asLivingEntity();
         if (entity == null || !entity.isUsingItem() || entity.getUseItem().getItem() != stack.getItem()) {
            return 0.0F;
         }
         return entity.getTicksUsingItem() / 10.0F;
      }

      @Override
      public MapCodec<PullProgress> type() {
         return MAP_CODEC;
      }
   }

   private static final class HasOil implements ConditionalItemModelProperty {
      private static final HasOil INSTANCE = new HasOil();
      private static final MapCodec<HasOil> MAP_CODEC = MapCodec.unit(INSTANCE);

      @Override
      public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
         if (stack.getItem() instanceof KitchenShovelItem) {
            return KitchenShovelItem.hasOil(stack);
         }
         return stack.getItem() instanceof OilPotItem && OilPotItem.hasOil(stack);
      }

      @Override
      public MapCodec<HasOil> type() {
         return MAP_CODEC;
      }
   }

   private static final class HasRecipe implements ConditionalItemModelProperty {
      private static final HasRecipe INSTANCE = new HasRecipe();
      private static final MapCodec<HasRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);

      @Override
      public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
         return RecipeItem.hasRecipe(stack);
      }

      @Override
      public MapCodec<HasRecipe> type() {
         return MAP_CODEC;
      }
   }

   private static final class HasItems implements ConditionalItemModelProperty {
      private static final HasItems INSTANCE = new HasItems();
      private static final MapCodec<HasItems> MAP_CODEC = MapCodec.unit(INSTANCE);

      @Override
      public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
         if (stack.getItem() instanceof TransmutationLunchBagItem) {
            return TransmutationLunchBagItem.hasItems(stack);
         }
         return stack.getItem() instanceof SteamerItem && stack.has(DataComponents.BLOCK_ENTITY_DATA);
      }

      @Override
      public MapCodec<HasItems> type() {
         return MAP_CODEC;
      }
   }
}
