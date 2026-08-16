package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

public final class RuntimeStrawHatModel extends HumanoidModel<HumanoidRenderState> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "straw_hat_runtime"), "main"
   );

   public RuntimeStrawHatModel(ModelPart root) {
      super(root);
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
      // addOrReplaceChild preserves children from the replaced vanilla head.
      // Explicitly replace its inherited 8x8 "hat" skin layer as well, or that
      // player-head overlay is rendered using the straw-hat texture.
      head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
      head.addOrReplaceChild(
         "bone",
         CubeListBuilder.create()
            .texOffs(-10, 17)
            .addBox(-7.5F, -4.0F, -7.5F, 15.0F, 0.0F, 15.0F, new CubeDeformation(0.01F))
            .texOffs(30, 0)
            .addBox(-4.0F, -8.0F, -4.5F, 8.0F, 4.0F, 9.0F, CubeDeformation.NONE)
            .texOffs(0, 41)
            .addBox(-4.0F, -7.25F, -4.5F, 8.0F, 4.0F, 9.0F, new CubeDeformation(0.1F)),
         PartPose.offsetAndRotation(0.3825F, 0.6375F, 0.0F, -0.0873F, 0.0F, -0.0873F).withScale(1.275F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }
}
