package com.example.jjf.zero_gravity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = "jjf",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class SpreadContactSystem {

    private static final int SPREAD_ARM_TICKS = 4;
    private static final double CONTACT_DISTANCE = 0.40D;
    private static final double CONTACT_PUSH = 0.045D;

    private SpreadContactSystem() {
    }

    @SubscribeEvent
    public static void onLivingTick(
            LivingEvent.LivingTickEvent event
    ) {
        LivingEntity source =
                event.getEntity();

        if (source.level().isClientSide()) {
            return;
        }

        if (!(source
                instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (state.getSource()
                != ZeroGravitySource.SPREAD) {
            return;
        }

        tick(
                source,
                state
        );
    }

    public static void tick(
            LivingEntity source,
            ZeroGravityState sourceState
    ) {
        if (source.level().isClientSide()) {
            return;
        }

        if (sourceState.getSource()
                != ZeroGravitySource.SPREAD) {
            return;
        }

        if (sourceState.getZeroGravityTicks()
                < SPREAD_ARM_TICKS) {
            return;
        }

        AABB contactBox =
                source.getBoundingBox()
                        .inflate(
                                CONTACT_DISTANCE
                        );

        List<LivingEntity> targets =
                source.level()
                        .getEntitiesOfClass(
                                LivingEntity.class,
                                contactBox,
                                target ->
                                        target != source
                                                && target.isAlive()
                                                && !target.isSpectator()
                        );

        UUID ownerUUID =
                sourceState.getOwnerUUID();

        for (LivingEntity target : targets) {
            if (!(target
                    instanceof ZeroGravityAccess access)) {
                continue;
            }

            ZeroGravityState targetState =
                    access.jjf$getZeroGravityState();

            if (targetState.isSafeUserZeroGravity()) {
                continue;
            }

            if (targetState.getSource()
                    == ZeroGravitySource.SPREAD) {
                continue;
            }

            infect(
                    source,
                    target,
                    targetState,
                    ownerUUID
            );
        }
    }

    private static void infect(
            LivingEntity source,
            LivingEntity target,
            ZeroGravityState targetState,
            UUID ownerUUID
    ) {
        targetState.setSource(
                ZeroGravitySource.SPREAD
        );

        if (ownerUUID != null) {
            targetState.setOwnerUUID(
                    ownerUUID
            );
        }

        targetState.resetZeroGravityTicks();

        target.setNoGravity(
                true
        );

        target.fallDistance =
                0.0F;

        applyContactPush(
                source,
                target
        );
    }

    private static void applyContactPush(
            LivingEntity source,
            LivingEntity target
    ) {
        Vec3 direction =
                target.position()
                        .subtract(
                                source.position()
                        );

        if (direction.lengthSqr()
                < 0.0001D) {
            direction =
                    source.getDeltaMovement();
        }

        if (direction.lengthSqr()
                < 0.0001D) {
            direction =
                    new Vec3(
                            0.0D,
                            1.0D,
                            0.0D
                    );
        }

        direction =
                direction.normalize();

        Vec3 current =
                target.getDeltaMovement();

        target.setDeltaMovement(
                current.x
                        + direction.x
                        * CONTACT_PUSH,
                current.y
                        + Math.max(
                        0.02D,
                        direction.y
                                * CONTACT_PUSH
                ),
                current.z
                        + direction.z
                        * CONTACT_PUSH
        );

        target.hasImpulse =
                true;

        target.hurtMarked =
                true;
    }
}
