package noobanidus.libs.noobutil.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorBaseModel extends HumanoidModel<LivingEntity> {
  public EquipmentSlot slot;

  public ArmorBaseModel(EquipmentSlot slot, float modelSize, float yOffset, int width, int height) {
    super(modelSize, yOffset, width, height);
    this.texHeight = 64;
    this.texWidth = 64;
    this.slot = slot;
    this.young = false;
  }

  @Override
  public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    if (!(entity instanceof ArmorStand)) {
      super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      return;
    }

    ArmorStand entityIn = (ArmorStand) entity;
    this.head.xRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getX();
    this.head.yRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getY();
    this.head.zRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getZ();
    this.head.setPos(0.0F, 1.0F, 0.0F);
    this.body.xRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getX();
    this.body.yRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getY();
    this.body.zRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getZ();
    this.leftArm.xRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getX();
    this.leftArm.yRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getY();
    this.leftArm.zRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getZ();
    this.rightArm.xRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getX();
    this.rightArm.yRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getY();
    this.rightArm.zRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getZ();
    this.leftLeg.xRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getX();
    this.leftLeg.yRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getY();
    this.leftLeg.zRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getZ();
    this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
    this.rightLeg.xRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getX();
    this.rightLeg.yRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getY();
    this.rightLeg.zRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getZ();
    this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
    this.hat.copyFrom(this.head);
  }
}
