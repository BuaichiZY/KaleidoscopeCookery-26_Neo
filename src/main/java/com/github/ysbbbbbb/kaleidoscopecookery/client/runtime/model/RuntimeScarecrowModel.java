package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model;

import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeScarecrowRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;

public final class RuntimeScarecrowModel extends EntityModel<RuntimeScarecrowRenderer.State>
   implements ArmedModel<RuntimeScarecrowRenderer.State>, HeadedModel {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "scarecrow"), "main"
   );

   private final ModelPart head;
   private final ModelPart leftArm;
   private final ModelPart rightArm;

   public RuntimeScarecrowModel(ModelPart root) {
      super(root);
      ModelPart group = root.getChild("group");
      this.head = group.getChild("head");
      this.rightArm = group.getChild("rightArm");
      this.leftArm = group.getChild("leftArm");
   }

   @Override
   public void setupAnim(RuntimeScarecrowRenderer.State state) {
      super.setupAnim(state);
      this.head.visible = state.showDefaultHead;
   }

   @Override
   public void translateToHand(RuntimeScarecrowRenderer.State state, HumanoidArm arm, PoseStack poseStack) {
      (arm == HumanoidArm.LEFT ? this.leftArm : this.rightArm).translateAndRotate(poseStack);
   }

   @Override
   public ModelPart getHead() {
      return this.head;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition group = root.addOrReplaceChild(
         "group",
         CubeListBuilder.create()
            .texOffs(85, 82)
            .addBox(-4.0F, -26.0278F, -2.0278F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(54, 69)
            .addBox(-4.0F, -17.0278F, -2.0278F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(73, 60)
            .addBox(8.0F, -26.0278F, -2.0278F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(73, 60)
            .mirror()
            .addBox(-8.0F, -26.0278F, -2.0278F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
            .mirror(false)
            .texOffs(10, 107)
            .addBox(-14.0F, -25.7778F, -1.0278F, 28.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(101, 65)
            .addBox(4.0F, -26.0278F, -2.0278F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(82, 65)
            .addBox(-9.0F, -26.0278F, -2.0278F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(28, 70)
            .addBox(-1.0F, -18.0278F, -1.0278F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 23.0278F, 0.0278F)
      );
      PartDefinition head = group.addOrReplaceChild(
         "head",
         CubeListBuilder.create().texOffs(13, 3).addBox(-4.5F, -32.2278F, -4.3778F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -2.8F, 0.1F)
      );
      PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.1198F, -30.4846F, 0.5972F));
      hat.addOrReplaceChild(
         "headwear_r1",
         CubeListBuilder.create()
            .texOffs(66, 8)
            .addBox(-5.0F, -3.775F, -5.025F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(77, 25)
            .addBox(-8.0F, 1.275F, -7.975F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -0.25F, -0.5F, 0.0F, 0.0F, 0.2182F)
      );
      group.addOrReplaceChild(
         "rightArm",
         CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
         PartPose.offset(-12.0F, -24.8F, -0.05F)
      );
      group.addOrReplaceChild(
         "leftArm",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offset(12.0F, -24.8F, -0.05F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }
}
