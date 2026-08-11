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

public final class RuntimeTrashCanModel extends Model<RuntimeTrashCanModel.State> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "trash_can"), "main"
   );

   private final ModelPart body;
   private final ModelPart lid;
   private final ModelPart eye;
   private final ModelPart eyeFront;

   public RuntimeTrashCanModel(ModelPart root) {
      super(root, RenderTypes::entityCutout);
      ModelPart group = root.getChild("root");
      this.body = group.getChild("bone");
      this.lid = group.getChild("bone2");
      this.eye = group.getChild("eye");
      this.eyeFront = this.eye.getChild("bone3");
   }

   @Override
   public void setupAnim(State state) {
      super.setupAnim(state);
      this.body.yRot = state.bodyYaw();
      this.lid.xRot = state.lidPitch();
      this.lid.zRot = state.lidRoll();
      this.lid.y -= state.lidLift();
      this.eye.y -= state.eyeLift();
      this.eyeFront.x += state.eyeSlide();
      this.eyeFront.y -= state.blink() ? 2.0F : 0.0F;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition group = root.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
      group.addOrReplaceChild(
         "bone",
         CubeListBuilder.create().texOffs(16, 40)
            .addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -6.0F, 0.0F)
      );
      PartDefinition lid = group.addOrReplaceChild(
         "bone2",
         CubeListBuilder.create().texOffs(8, 0)
            .addBox(-7.0F, -0.25F, -7.0F, 14.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -14.75F, 0.0F)
      );
      lid.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create()
            .texOffs(6, 26).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(0, 20).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(6, 26).addBox(-1.0F, -0.5F, 3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F)
      );
      PartDefinition eye = group.addOrReplaceChild(
         "eye",
         CubeListBuilder.create().texOffs(24, 22)
            .addBox(-5.0F, -1.5F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -8.5F, 0.0F)
      );
      eye.addOrReplaceChild(
         "bone3",
         CubeListBuilder.create().texOffs(34, 36)
            .addBox(-5.0F, -3.0F, -5.0F, 10.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 1.5F, -0.1F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public record State(float bodyYaw, float lidPitch, float lidRoll, float lidLift, float eyeLift, float eyeSlide, boolean blink) {
   }
}
