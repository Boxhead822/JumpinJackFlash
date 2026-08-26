package com.example.jjf.client.models.stand;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import com.example.jjf.entity.stand.JumpinJackFlashEntity;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class JumpinJackFlashModel<T extends JumpinJackFlashEntity> extends StandModel<T> {

    private final ModelPart rightSpinOff;
    private final ModelPart rightSpinOn;
    private final ModelPart leftSpinOff;
    private final ModelPart leftSpinOn;

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation("jjf", "jumpin_jack_flash"),
                    "main"
            );

    public JumpinJackFlashModel(ModelPart root) {
        this.stand = root.getChild("stand");

        ModelPart stand2 = this.stand.getChild("stand2");

        this.head = stand2.getChild("head");
        this.body = stand2.getChild("body");

        ModelPart upperChest = this.body
                .getChild("body2")
                .getChild("torso")
                .getChild("upper_chest");

        ModelPart rightArm = upperChest.getChild("right_arm");
        ModelPart leftArm = upperChest.getChild("left_arm");

        this.rightHand = rightArm.getChild("lower_right_arm");
        this.leftHand = leftArm.getChild("lower_left_arm");

        this.rightSpinOff = this.rightHand.getChild("right_spin_off");
        this.rightSpinOn = this.rightHand.getChild("right_spin_on");
        this.leftSpinOff = this.leftHand.getChild("left_spin_off");
        this.leftSpinOn = this.leftHand.getChild("left_spin_on");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
                .texOffs(0, 32).addBox(-4.0F, -4.85F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 20).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.101F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(48, 47).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 18).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(32, 51).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.175F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(18, 42).addBox(-1.0F, -1.25F, 2.25F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.21F))
                .texOffs(16, 49).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition right_spin_off = lower_right_arm.addOrReplaceChild("right_spin_off", CubeListBuilder.create().texOffs(0, 65).mirror().addBox(-4.5F, 0.0F, -2.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.251F)).mirror(false), PartPose.offset(2.0F, 1.25F, -0.5F));

        PartDefinition right_spin_on = lower_right_arm.addOrReplaceChild("right_spin_on", CubeListBuilder.create().texOffs(48, 40).mirror().addBox(-2.5F, 2.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.65F)).mirror(false), PartPose.offset(0.0F, 0.25F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(56, 18).mirror().addBox(0.0F, -0.75F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false)
                .texOffs(48, 47).mirror().addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 51).mirror().addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.175F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(18, 42).mirror().addBox(-1.0F, -1.25F, 2.25F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 49).mirror().addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.21F)).mirror(false)
                .texOffs(16, 49).mirror().addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition left_spin_off = lower_left_arm.addOrReplaceChild("left_spin_off", CubeListBuilder.create().texOffs(0, 65).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.65F)), PartPose.offset(0.0F, 0.25F, 0.0F));

        PartDefinition left_spin_on = lower_left_arm.addOrReplaceChild("left_spin_on", CubeListBuilder.create().texOffs(48, 40).addBox(-3.0F, 0.0F, -2.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.5F, 1.25F, -0.5F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(32, 10).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 30).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-1.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(56, 0).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(24, 42).addBox(-1.0F, -1.0F, -2.25F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(56, 9).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F))
                .texOffs(32, 40).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(3.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(24, 42).mirror().addBox(-1.0F, -1.0F, -2.25F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 9).mirror().addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)).mirror(false)
                .texOffs(32, 40).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    
    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        super.setupAnim(
                entity,
                limbSwing,
                limbSwingAmount,
                ageInTicks,
                netHeadYaw,
                headPitch
        );

        defaultModifiers(entity);
        defaultAnimations(entity, ageInTicks, 1.0F);
    }
}