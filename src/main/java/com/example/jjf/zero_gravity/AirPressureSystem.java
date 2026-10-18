package com.example.jjf.zero_gravity;

import com.example.jjf.registry.ModItems;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class AirPressureSystem {

    public static final int GRACE_TICKS = 20 * 15;
    public static final int FULL_PRESSURE_TICKS = 20 * 60;

    public static final double MAX_OWNER_DISTANCE = 20.0D;

    private static final int DAMAGE_INTERVAL_TICKS = 20;

    private static final float MIN_DAMAGE = 0.15F;
    private static final float MAX_DAMAGE = 0.80F;

    private AirPressureSystem() {
    }

    public static void tick(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        if (!isAirPressureActive(entity, state)) {
            state.resetAirPressureTicks();
            return;
        }

        if (!isWithinOwnerRange(entity, state)) {
            state.resetAirPressureTicks();
            return;
        }

        state.incrementAirPressureTicks();

        if (entity.level().isClientSide()) {
            return;
        }

        if (state.getAirPressureTicks() <= GRACE_TICKS) {
            return;
        }

        int damageTicks =
                state.getAirPressureTicks()
                        - GRACE_TICKS;

        if (damageTicks % DAMAGE_INTERVAL_TICKS != 0) {
            return;
        }

        float pressure =
                getPressure(state);

        float damage =
                MIN_DAMAGE
                        + (MAX_DAMAGE - MIN_DAMAGE)
                        * pressure
                        * pressure;

        entity.hurt(
                entity.damageSources().drown(),
                damage
        );
    }

    public static boolean shouldShowMeter(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        return isAirPressureActive(entity, state)
                && isWithinOwnerRange(entity, state)
                && state.getAirPressureTicks() >= GRACE_TICKS;
    }

    public static boolean isAirPressureActive(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        return state.isSpread()
                && !isJJFUser(entity);
    }

    public static float getPressure(
            ZeroGravityState state
    ) {
        int pressureTicks =
                state.getAirPressureTicks()
                        - GRACE_TICKS;

        int pressureDuration =
                FULL_PRESSURE_TICKS
                        - GRACE_TICKS;

        if (pressureTicks <= 0) {
            return 0.0F;
        }

        if (pressureTicks >= pressureDuration) {
            return 1.0F;
        }

        return pressureTicks
                / (float) pressureDuration;
    }

    public static int getPressurePercent(
            ZeroGravityState state
    ) {
        return Math.round(
                getPressure(state) * 100.0F
        );
    }

    public static boolean isWithinOwnerRange(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        UUID ownerUUID =
                state.getOwnerUUID();

        if (ownerUUID == null) {
            return false;
        }

        if (!(entity.level() instanceof ServerLevel server)) {
            return true;
        }

        Entity owner =
                server.getEntity(ownerUUID);

        if (owner == null || !owner.isAlive()) {
            return false;
        }

        return entity.distanceToSqr(owner)
                <= MAX_OWNER_DISTANCE
                * MAX_OWNER_DISTANCE;
    }

    public static boolean isJJFUser(
            LivingEntity entity
    ) {
        if (!(entity instanceof StandUser standUser)) {
            return false;
        }

        ItemStack disc =
                standUser.roundabout$getStandDisc();

        if (!disc.isEmpty()) {
            if (ModItems.JJF_STAND_DISC != null
                    && disc.is(ModItems.JJF_STAND_DISC)) {
                return true;
            }

            if (ModItems.JJF_STAND_DISC_MAX != null
                    && disc.is(ModItems.JJF_STAND_DISC_MAX)) {
                return true;
            }
        }

        return standUser.roundabout$getStandPowers() != null
                && standUser.roundabout$getStandPowers()
                .getClass()
                .getName()
                .equals(
                        "com.example.jjf.stand.powers.PowersJumpinJackFlash"
                );
    }
}