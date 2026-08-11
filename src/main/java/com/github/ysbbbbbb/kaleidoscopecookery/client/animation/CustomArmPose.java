package com.github.ysbbbbbb.kaleidoscopecookery.client.animation;

import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class CustomArmPose {
   public static final EnumProxy<ArmPose> LIFT_POSE = new EnumProxy(ArmPose.class, new Object[]{false, (IArmPoseTransformer)(model, entity, arm) -> {
      if (arm == HumanoidArm.RIGHT) {
         model.rightArm.xRot = (float) -Math.PI;
         model.rightArm.zRot = -0.07853982F;
      } else {
         model.leftArm.xRot = (float) (-Math.PI / 2);
         model.leftArm.zRot = 0.07853982F;
      }
   }});
}
