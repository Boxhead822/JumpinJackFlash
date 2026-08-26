package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravityState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.nbt.CompoundTag;



@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ZeroGravityAccess {

    @Unique
    private final ZeroGravityState jjf$zeroGravityState =
            new ZeroGravityState();

    @Inject(method = "tick", at = @At("TAIL"))
    private void jjf$tickZeroGravity(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        ZeroGravitySystem.tickState(entity);
        ZeroGravitySystem.tickZeroGravity(entity);
    }

    @Override
    public ZeroGravityState jjf$getZeroGravityState() {
        return this.jjf$zeroGravityState;
    }

    @Inject(method = "knockback", at = @At("TAIL"))
    private void jjf$zeroGravityKnockback(
            double strength,
            double x,
            double z,
            CallbackInfo ci
    ) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        if (!access.jjf$getZeroGravityState().isZeroGravity()) {
            return;
        }

        Vec3 motion = entity.getDeltaMovement();

        entity.setDeltaMovement(
                motion.x * 1.20D,
                motion.y * 1.08D,
                motion.z * 1.20D
        );

        entity.hasImpulse = true;
    }
    @Inject(
            method = "swing(Lnet/minecraft/world/InteractionHand;Z)V",
            at = @At("HEAD")
    )
    private void jjf$zeroGravitySwingImpulse(
            InteractionHand hand,
            boolean updateSelf,
            CallbackInfo ci
    ) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }

        ZeroGravitySystem.applySwingImpulse(entity);
    }
    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void jjf$saveZeroGravityState(
            CompoundTag tag,
            CallbackInfo ci
    ) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        tag.putBoolean(
                "JJFZeroGravityActive",
                state.isZeroGravity()
        );
    }
    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void jjf$loadZeroGravityState(
            CompoundTag tag,
            CallbackInfo ci
    ) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!tag.getBoolean("JJFZeroGravityActive")) {
            return;
        }

        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        state.clearZeroGravity();
        state.setRestorationTicks(0);

        entity.setNoGravity(false);
        entity.fallDistance = 0.0F;
        entity.setDeltaMovement(Vec3.ZERO);
        entity.hasImpulse = true;
    }

}