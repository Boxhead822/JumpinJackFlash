package com.example.jjf.stand.powers;

import com.example.jjf.registry.ModEntities;
import com.example.jjf.registry.ModSounds;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravitySource;
import com.example.jjf.zero_gravity.ZeroGravityState;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.hydra.jojomod.client.StandIcons;
import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.minecraft.world.phys.Vec3;


public class PowersJumpinJackFlash extends NewPunchingStand {

    public static final byte POWER_SELF_ZERO_GRAVITY = PowerIndex.POWER_1;

    private static final ResourceLocation NORMAL_GRAVITY_ICON =
            ResourceLocation.fromNamespaceAndPath(
                    "roundabout",
                    "textures/mob_effect/gravity_flip.png"
            );

    private static final ResourceLocation ZERO_GRAVITY_ON =
            ResourceLocation.fromNamespaceAndPath(
                    "jjf",
                    "textures/gui/icons/jjf/zero_gravity_on.png"
            );


    public PowersJumpinJackFlash(LivingEntity self) {
        super(self);
    }

    private boolean isLookingAtEntity() {
        return Minecraft.getInstance().hitResult
                instanceof EntityHitResult;
    }

    @Override
    public boolean interceptAttack() {
        if (shouldUseZeroGravityBlockPush()) {
            return false;
        }

        if (shouldUseZeroGravityItemSwing()) {
            return false;
        }

        return super.interceptAttack();
    }
    @Override
    public boolean canUseMiningStand() {
        if (this.getSelf() instanceof ZeroGravityAccess access) {
            ZeroGravityState state = access.jjf$getZeroGravityState();

            if (state.isZeroGravity()) {
                return false;
            }
        }

        return super.canUseMiningStand();
    }
    private boolean shouldUseZeroGravityBlockPush() {
        if (!(this.getSelf() instanceof Player player)) {
            return false;
        }

        if (!(player instanceof ZeroGravityAccess access)) {
            return false;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return false;
        }

        if (player.onGround()) {
            return false;
        }

        HitResult hit = player.pick(
                4.5D,
                0.0F,
                false
        );

        if (hit.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockHitResult blockHit =
                (BlockHitResult) hit;

        BlockState blockState =
                player.level().getBlockState(
                        blockHit.getBlockPos()
                );

        return blockState.isCollisionShapeFullBlock(
                player.level(),
                blockHit.getBlockPos()
        );
    }

    private boolean shouldUseZeroGravityItemSwing() {
        if (!(this.getSelf() instanceof Player player)) {
            return false;
        }

        if (!(player instanceof ZeroGravityAccess access)) {
            return false;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return false;
        }

        if (player.onGround()) {
            return false;
        }

        if (player.getMainHandItem().isEmpty()) {
            return false;
        }

        if (shouldUseZeroGravityBlockPush()) {
            return false;
        }

        return getTargetEntity(
                player,
                -1,
                getPunchAngle()
        ) == null;
    }

    @Override
    public boolean setPowerAttack() {
        if (shouldUseZeroGravityBlockPush()) {
            return false;
        }

        if (shouldUseZeroGravityItemSwing()) {
            return false;
        }

        return super.setPowerAttack();
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersJumpinJackFlash(entity);
    }


    @Override
    public boolean canSummonStand() {
        return true;
    }


    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.JUMPIN_JACK_FLASH.get().create(
                this.getSelf().level()
        );
    }


    @Override
    public void playSummonSound() {
        if (this.getSelf().isCrouching()) {
            return;
        }

        this.getSelf().level().playSound(
                null,
                this.getSelf().blockPosition(),
                ModSounds.JUMPIN_JACK_FLASH_SUMMON.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            case SKILL_1_NORMAL -> {
                if (onCooldown(PowerIndex.SKILL_1)) {
                    return;
                }
                if (this.getSelf().level().isClientSide()) {
                    toggleSelfZeroGravity();
                }
                tryPowerPacket(POWER_SELF_ZERO_GRAVITY);
            }

            case SKILL_3_NORMAL -> dash();
            default -> {
            }
        }
    }

    @Override
    public void dash() {
        if (!canJJFDash()) {
            return;
        }

        boolean zeroGravityGroundDash =
                !getSelf().onGround()
                        && getSelf() instanceof ZeroGravityAccess access
                        && access.jjf$getZeroGravityState().isZeroGravity()
                        && ZeroGravitySystem.isTouchingGround(getSelf());

        if (zeroGravityGroundDash) {
            getSelf().setOnGround(true);
        }

        super.dash();

        if (zeroGravityGroundDash) {
            Vec3 motion = getSelf().getDeltaMovement();

            getSelf().setDeltaMovement(
                    motion.x,
                    0.0D,
                    motion.z
            );

            getSelf().setOnGround(false);
            getSelf().hasImpulse = true;
        }
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 3 && !canJJFDash()) {
            return true;
        }

        return super.isAttackIneptVisually(activeP, slot);
    }


    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == POWER_SELF_ZERO_GRAVITY) {

            if (onCooldown(PowerIndex.SKILL_1)) {
                return false;
            }

            if (!this.getSelf().level().isClientSide()) {
                toggleSelfZeroGravity();
            }

            return true;
        }

        return super.setPowerOther(move, lastMove);
    }


    private void toggleSelfZeroGravity() {
        if (!(this.getSelf() instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (state.getSource() == ZeroGravitySource.SPREAD) {
            return;
        }

        if (state.getSource() == ZeroGravitySource.USER_TOGGLE) {
            state.clearZeroGravity();
            state.setRestorationTicks(1);

            this.getSelf().setNoGravity(false);

            setCooldown(
                    PowerIndex.SKILL_1,
                    40
            );

            return;
        }

        state.setSource(
                ZeroGravitySource.USER_TOGGLE
        );

        state.setOwnerUUID(
                this.getSelf().getUUID()
        );

        state.resetZeroGravityTicks();

        this.getSelf().setNoGravity(true);

        setCooldown(
                PowerIndex.SKILL_1,
                40
        );
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ResourceLocation icon =
                NORMAL_GRAVITY_ICON;
        if (this.getSelf() instanceof ZeroGravityAccess access) {
            ZeroGravityState state =
                    access.jjf$getZeroGravityState();
            if (state.isSafeUserZeroGravity()) {
                icon = ZERO_GRAVITY_ON;
            }
        }

        setSkillIcon(context, x, y, 1, icon, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

    private boolean canJJFDash() {
        if (getSelf().onGround()) {
            return true;
        }

        if (getSelf() instanceof ZeroGravityAccess access) {
            return access.jjf$getZeroGravityState().isZeroGravity()
                    && ZeroGravitySystem.isTouchingGround(getSelf());
        }

        return false;
    }


    @Override
    public boolean isWip() {
        return true;
    }


    @Override
    public Component ifWipListDevStatus() {
        return Component.literal(
                "In Development"
        );
    }


    @Override
    public Component ifWipListDev() {
        return Component.literal(
                "BoxHead"
        );
    }
}