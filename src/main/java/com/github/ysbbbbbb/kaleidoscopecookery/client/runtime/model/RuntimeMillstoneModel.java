package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public final class RuntimeMillstoneModel extends Model<RuntimeMillstoneModel.State> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone"), "main"
   );

   private final ModelPart wheel;
   private final ModelPart roll;
   private final ModelPart rotatingStick;

   public RuntimeMillstoneModel(ModelPart root) {
      super(root, RenderTypes::entityCutout);
      this.wheel = root.getChild("wheel");
      this.roll = this.wheel.getChild("roll");
      this.rotatingStick = this.wheel.getChild("stick").getChild("rotStick");
   }

   @Override
   public void setupAnim(State state) {
      super.setupAnim(state);
      this.wheel.yRot = state.wheelRotation();
      this.roll.zRot = state.rollRotation();
      this.rotatingStick.xRot = state.stickRotation();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "base",
         CubeListBuilder.create()
            .texOffs(6, 8).addBox(-13.0F, -15.0F, -10.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
            .texOffs(5, 8).addBox(-5.0F, -15.0F, -10.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
            .texOffs(5, 21).addBox(-13.0F, -12.0F, -10.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(0, 87).addBox(-24.0F, -14.0F, -8.0F, 32.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
            .texOffs(0, 31).addBox(-21.0F, -6.0F, -5.0F, 26.0F, 6.0F, 26.0F, new CubeDeformation(0.0F))
            .texOffs(22, 64).addBox(-18.0F, -15.0F, -2.0F, 20.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)),
         PartPose.offset(8.0F, 24.0F, -8.0F)
      );
      PartDefinition wheel = root.addOrReplaceChild("wheel", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));
      wheel.addOrReplaceChild(
         "roll",
         CubeListBuilder.create().texOffs(76, 0).addBox(-6.0F, -6.0F, -7.0F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 0.0F, -8.0F)
      );
      PartDefinition stick = wheel.addOrReplaceChild("stick", CubeListBuilder.create(), PartPose.ZERO);
      stick.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create().texOffs(83, 31).addBox(-8.0F, -2.0F, -2.0F, 16.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -0.5F, 1.0F, -1.5708F, 0.0F, 0.0F)
      );
      stick.addOrReplaceChild(
         "cube_r2",
         CubeListBuilder.create().texOffs(114, 72).addBox(-2.0F, -21.5F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F)),
         PartPose.offsetAndRotation(0.0F, 3.0F, -36.5F, -1.5708F, 0.0F, 0.0F)
      );
      stick.addOrReplaceChild(
         "cube_r3",
         CubeListBuilder.create().texOffs(111, 84).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 29.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, 0.0F, -17.0F, -1.5708F, 0.0F, 0.0F)
      );
      stick.addOrReplaceChild(
         "cube_r4",
         CubeListBuilder.create().texOffs(86, 32).addBox(-7.0F, -2.0F, -2.0F, 14.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -1.0F, -17.0F, -1.5708F, 0.0F, 0.0F)
      );
      PartDefinition rotatingStick = stick.addOrReplaceChild("rotStick", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));
      rotatingStick.addOrReplaceChild(
         "cube_r5",
         CubeListBuilder.create().texOffs(111, 84).addBox(-2.0F, -17.5F, -2.0F, 4.0F, 30.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, 3.5F, 0.0F, 0.0F, -0.7854F, 0.0F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }

   public record State(float wheelRotation, float rollRotation, float stickRotation) {
   }
}
