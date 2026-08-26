package com.example.jjf.zero_gravity;

import java.util.UUID;
import net.minecraft.core.Direction;

public class ZeroGravityState {

    private ZeroGravitySource source = ZeroGravitySource.NONE;
    private UUID ownerUUID = null;
    private int zeroGravityTicks = 0;
    private float radius = 0.0F;
    private int restorationTicks = 0;
    private int swingImpulseCooldown = 0;

    public boolean isZeroGravity() {
        return source != ZeroGravitySource.NONE;
    }

    public int getSwingImpulseCooldown() {
        return swingImpulseCooldown;
    }

    public void setSwingImpulseCooldown(int swingImpulseCooldown) {
        this.swingImpulseCooldown = swingImpulseCooldown;
    }

    public void tickSwingImpulseCooldown() {
        if (swingImpulseCooldown > 0) {
            swingImpulseCooldown--;
        }
    }

    public boolean isSpread() {
        return source == ZeroGravitySource.SPREAD;
    }

    public boolean isSafeUserZeroGravity() {
        return source == ZeroGravitySource.USER_TOGGLE;
    }

    public ZeroGravitySource getSource() {
        return source;
    }

    public void setSource(ZeroGravitySource source) {
        this.source = source;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public int getZeroGravityTicks() {
        return zeroGravityTicks;
    }

    public void incrementZeroGravityTicks() {
        zeroGravityTicks++;
    }

    public void resetZeroGravityTicks() {
        zeroGravityTicks = 0;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getRestorationTicks() {
        return restorationTicks;
    }

    public void setRestorationTicks(int restorationTicks) {
        this.restorationTicks = restorationTicks;
    }

    public boolean isRestoringWeight() {
        return restorationTicks > 0;
    }

    public void tickRestoration() {
        if (restorationTicks > 0) {
            restorationTicks--;
        }
    }

    private boolean suctioned = false;
    private Direction suctionSurface = null;

    public boolean isSuctioned() {
        return suctioned;
    }

    public void setSuctioned(boolean suctioned) {
        this.suctioned = suctioned;
    }

    public Direction getSuctionSurface() {
        return suctionSurface;
    }

    public void setSuctionSurface(Direction suctionSurface) {
        this.suctionSurface = suctionSurface;
    }

    public void clearZeroGravity() {
        source = ZeroGravitySource.NONE;
        ownerUUID = null;
        zeroGravityTicks = 0;
        radius = 0.0F;
        suctioned = false;
        swingImpulseCooldown = 0;
    }

    private int zeroGravityToggleCooldown = 0;

    public int getZeroGravityToggleCooldown() {
        return zeroGravityToggleCooldown;
    }

    public void setZeroGravityToggleCooldown(int ticks) {
        zeroGravityToggleCooldown = ticks;
    }

    public void tickZeroGravityToggleCooldown() {
        if (zeroGravityToggleCooldown > 0) {
            zeroGravityToggleCooldown--;
        }
    }
    private int suctionLaunchSoundCooldown = 0;

    public int getSuctionLaunchSoundCooldown() {
        return suctionLaunchSoundCooldown;
    }

    public void setSuctionLaunchSoundCooldown(int ticks) {
        suctionLaunchSoundCooldown = ticks;
    }

    public void tickSuctionLaunchSoundCooldown() {
        if (suctionLaunchSoundCooldown > 0) {
            suctionLaunchSoundCooldown--;
        }
    }

    private boolean suctionLaunchSoundEnabled = false;

    public boolean isSuctionLaunchSoundEnabled() {
        return suctionLaunchSoundEnabled;
    }

    public void setSuctionLaunchSoundEnabled(boolean enabled) {
        suctionLaunchSoundEnabled = enabled;
    }

    public void toggleSuctionLaunchSound() {
        suctionLaunchSoundEnabled = !suctionLaunchSoundEnabled;
    }

}