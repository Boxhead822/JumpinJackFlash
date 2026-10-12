package com.example.jjf.zero_gravity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public final class SpreadContactSystem {

    private static final int SPREAD_ARM_TICKS = 4;
    private static final int SPREAD_DURATION_TICKS = 20 * 60;
    private static final int RESTORATION_TICKS = 20;

    private static final double CONTACT_DISTANCE = 0.40D;
    private static final double CONTACT_PUSH = 0.045D;

    private static final float FIELD_START_RADIUS = 0.75F;
    private static final float FIELD_MAX_RADIUS = 20.0F;
    private static final int FIELD_GROW_TICKS = 20 * 60;
    private static final int FIELD_SCAN_INTERVAL = 5;

    private SpreadContactSystem() {
    }

    public static void tick(
            LivingEntity source,
            ZeroGravityState sourceState
    ) {
        if (source.level().isClientSide()) {
            return;
        }

        if (sourceState.getSource() != ZeroGravitySource.SPREAD) {
            return;
        }

        UUID ownerUUID = sourceState.getOwnerUUID();

        if (ownerUUID != null && source.getUUID().equals(ownerUUID)) {
            sourceState.clearZeroGravity();
            source.setNoGravity(false);
            source.fallDistance = 0.0F;
            return;
        }

        if (sourceState.getZeroGravityTicks() >= SPREAD_DURATION_TICKS) {
            expire(source, sourceState);
            return;
        }

        if (sourceState.getZeroGravityTicks() < SPREAD_ARM_TICKS) {
            return;
        }

        spreadByContact(
                source,
                sourceState,
                ownerUUID
        );

        if (sourceState.isSpreadEmitter()) {
            tickExpandingField(
                    source,
                    sourceState,
                    ownerUUID
            );
        }
    }

    private static void spreadByContact(
            LivingEntity source,
            ZeroGravityState sourceState,
            UUID ownerUUID
    ) {
        AABB contactBox =
                source.getBoundingBox()
                        .inflate(CONTACT_DISTANCE);

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

        for (LivingEntity target : targets) {
            tryInfect(
                    source,
                    target,
                    ownerUUID,
                    true
            );
        }
    }

    private static void tickExpandingField(
            LivingEntity source,
            ZeroGravityState sourceState,
            UUID ownerUUID
    ) {
        int ticks =
                Math.min(
                        sourceState.getZeroGravityTicks(),
                        FIELD_GROW_TICKS
                );

        float progress =
                ticks / (float) FIELD_GROW_TICKS;

        float radius =
                FIELD_START_RADIUS
                        + (FIELD_MAX_RADIUS - FIELD_START_RADIUS)
                        * progress;

        sourceState.setRadius(radius);

        if (sourceState.getZeroGravityTicks()
                % FIELD_SCAN_INTERVAL != 0) {
            return;
        }

        AABB fieldBox =
                source.getBoundingBox()
                        .inflate(radius);

        List<LivingEntity> targets =
                source.level()
                        .getEntitiesOfClass(
                                LivingEntity.class,
                                fieldBox,
                                target ->
                                        target != source
                                                && target.isAlive()
                                                && !target.isSpectator()
                        );

        double radiusSquared =
                radius * radius;

        for (LivingEntity target : targets) {
            Vec3 difference =
                    target.getBoundingBox()
                            .getCenter()
                            .subtract(
                                    source.getBoundingBox()
                                            .getCenter()
                            );

            if (difference.lengthSqr() > radiusSquared) {
                continue;
            }

            tryInfect(
                    source,
                    target,
                    ownerUUID,
                    false
            );
        }
    }

    private static void tryInfect(
            LivingEntity source,
            LivingEntity target,
            UUID ownerUUID,
            boolean contact
    ) {
        if (ownerUUID != null
                && target.getUUID().equals(ownerUUID)) {
            return;
        }

        if (!(target instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState targetState =
                access.jjf$getZeroGravityState();

        if (targetState.isSafeUserZeroGravity()) {
            return;
        }

        if (targetState.getSource() == ZeroGravitySource.SPREAD) {
            return;
        }

        infect(
                source,
                target,
                targetState,
                ownerUUID,
                contact
        );
    }

    private static void infect(
            LivingEntity source,
            LivingEntity target,
            ZeroGravityState targetState,
            UUID ownerUUID,
            boolean contact
    ) {
        targetState.setSource(
                ZeroGravitySource.SPREAD
        );

        targetState.setOwnerUUID(
                ownerUUID
        );

        targetState.resetZeroGravityTicks();
        targetState.setRadius(0.0F);
        targetState.setSpreadEmitter(false);

        target.setNoGravity(true);
        target.fallDistance = 0.0F;

        if (contact) {
            applyContactPush(
                    source,
                    target
            );
        } else {
            applyFieldNudge(
                    source,
                    target
            );
        }
    }

    private static void applyContactPush(
            LivingEntity source,
            LivingEntity target
    ) {
        Vec3 direction =
                target.position()
                        .subtract(source.position());

        if (direction.lengthSqr() < 0.0001D) {
            direction = source.getDeltaMovement();
        }

        if (direction.lengthSqr() < 0.0001D) {
            direction =
                    new Vec3(
                            0.0D,
                            1.0D,
                            0.0D
                    );
        }

        direction = direction.normalize();

        Vec3 current =
                target.getDeltaMovement();

        target.setDeltaMovement(
                current.x
                        + direction.x * CONTACT_PUSH,
                current.y
                        + Math.max(
                        0.02D,
                        direction.y * CONTACT_PUSH
                ),
                current.z
                        + direction.z * CONTACT_PUSH
        );

        target.hasImpulse = true;
        target.hurtMarked = true;
    }

    private static void applyFieldNudge(
            LivingEntity source,
            LivingEntity target
    ) {
        Vec3 direction =
                target.position()
                        .subtract(source.position());

        if (direction.lengthSqr() < 0.0001D) {
            direction =
                    new Vec3(
                            0.0D,
                            1.0D,
                            0.0D
                    );
        }

        direction = direction.normalize();

        Vec3 current =
                target.getDeltaMovement();

        target.setDeltaMovement(
                current.x
                        + direction.x * 0.015D,
                current.y
                        + 0.025D,
                current.z
                        + direction.z * 0.015D
        );

        target.hasImpulse = true;
        target.hurtMarked = true;
    }

    private static void expire(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        state.clearZeroGravity();

        state.setRestorationTicks(
                RESTORATION_TICKS
        );

        entity.setNoGravity(false);
        entity.fallDistance = 0.0F;
        entity.hasImpulse = true;
    }
}