package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LanternBlock;

public final class RuntimeScarecrowRenderer
   extends LivingEntityRenderer<ScarecrowEntity, RuntimeScarecrowRenderer.State, RuntimeScarecrowModel> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
      "kaleidoscope_cookery", "textures/entity/scarecrow.png"
   );
   private final BlockModelResolver blockModelResolver;

   public RuntimeScarecrowRenderer(EntityRendererProvider.Context context) {
      super(context, new RuntimeScarecrowModel(context.bakeLayer(RuntimeScarecrowModel.LAYER_LOCATION)), 0.0F);
      this.blockModelResolver = context.getBlockModelResolver();
      this.addLayer(new RuntimeScarecrowHandLayer(this));
      this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(ScarecrowEntity entity, State state, float partialTick) {
      super.extractRenderState(entity, state, partialTick);
      ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver, partialTick);
      state.showDefaultHead = entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
      state.hitAnimationTime = (float)(entity.level().getGameTime() - entity.lastHit) + partialTick;

      state.lanternBlock.clear();
      ItemStack offHandItem = entity.getOffhandItem();
      if (offHandItem.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof LanternBlock lanternBlock) {
         this.blockModelResolver.update(state.lanternBlock, lanternBlock.defaultBlockState(), BlockDisplayContext.create());
      }
   }

   @Override
   protected void setupRotations(State state, PoseStack poseStack, float bodyRot, float scale) {
      super.setupRotations(state, poseStack, bodyRot, scale);
      if (state.hitAnimationTime >= 0.0F && state.hitAnimationTime < 5.0F) {
         poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(state.hitAnimationTime / 1.5F * (float)Math.PI) * 3.0F));
      }
   }

   @Override
   public Identifier getTextureLocation(State state) {
      return TEXTURE;
   }

   public static final class State extends ArmedEntityRenderState {
      final BlockModelRenderState lanternBlock = new BlockModelRenderState();
      public boolean showDefaultHead = true;
      public float hitAnimationTime = Float.MAX_VALUE;
   }
}
