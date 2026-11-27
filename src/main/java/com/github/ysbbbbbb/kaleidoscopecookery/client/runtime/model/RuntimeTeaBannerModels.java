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

public final class RuntimeTeaBannerModels {
   private RuntimeTeaBannerModels() {}
   public record State(float waveAngle) {}

   private abstract static class BannerModel extends Model<State> {
      private final ModelPart front;
      private final ModelPart back;
      protected BannerModel(ModelPart root) {
         super(root, RenderTypes::entityCutout);
         ModelPart bone = root.getChild("bone");
         this.front = bone.getChild("front");
         this.back = bone.getChild("back");
      }
      public void setupAnim(State state) {
         super.setupAnim(state);
         this.front.zRot = state.waveAngle;
         this.back.zRot = state.waveAngle;
      }
   }

   public static final class Normal extends BannerModel {
      public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "runtime_normal_banner"), "main");
      public Normal(ModelPart root) { super(root); }
      public static LayerDefinition createLayer() {
         MeshDefinition mesh = new MeshDefinition(); PartDefinition root = mesh.getRoot();
         PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(64, 0).addBox(-1,-60,-1,2,60,2,new CubeDeformation(0)).texOffs(0,64).addBox(-2,-64,-14,4,4,28,new CubeDeformation(0)), PartPose.offsetAndRotation(0,24,0,0,-1.5708F,0));
         bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0,-28).addBox(0,0,-14,0,41,28,new CubeDeformation(0)), PartPose.offset(-2,-60,0));
         bone.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0,-28).addBox(0,0,-14,0,41,28,new CubeDeformation(0)), PartPose.offset(2,-60,0));
         return LayerDefinition.create(mesh,128,128);
      }
   }

   public static final class Wall extends BannerModel {
      public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "runtime_wall_banner"), "main");
      public Wall(ModelPart root) { super(root); }
      public static LayerDefinition createLayer() {
         MeshDefinition mesh = new MeshDefinition(); PartDefinition root = mesh.getRoot();
         PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(64,107).addBox(-1,-3,-16,2,2,10,new CubeDeformation(0)).texOffs(36,79).addBox(-1,3,-16,2,2,38,new CubeDeformation(0)).texOffs(0,64).addBox(-2,-4,-6,4,4,28,new CubeDeformation(0)), PartPose.offsetAndRotation(-8,12,0,0,-1.5708F,0));
         bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0,-28).addBox(0,0,-14,0,41,28,new CubeDeformation(0)), PartPose.offset(-2,0,8));
         bone.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0,-28).addBox(0,0,-14,0,41,28,new CubeDeformation(0)), PartPose.offset(2,0,8));
         return LayerDefinition.create(mesh,128,128);
      }
   }

   public static final class Pattern extends BannerModel {
      public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "runtime_banner_pattern"), "main");
      public Pattern(ModelPart root) { super(root); }
      public static LayerDefinition createLayer() {
         MeshDefinition mesh = new MeshDefinition(); PartDefinition root = mesh.getRoot();
         PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0,24,0,0,-1.5708F,0));
         bone.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0,16).addBox(0,0,-16,0,48,32,new CubeDeformation(0)), PartPose.offset(2.25F,-65,0));
         bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0,-32).addBox(0,0,-16,0,48,32,new CubeDeformation(0)), PartPose.offset(-2.25F,-65,0));
         return LayerDefinition.create(mesh,64,96);
      }
   }
}
