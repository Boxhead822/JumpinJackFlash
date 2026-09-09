package com.example.jjf.mixin;

import com.example.jjf.launcher.JJFLauncherAccess;
import com.example.jjf.launcher.JJFLauncherState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin
        implements JJFLauncherAccess {

    @Unique
    private static final EntityDataAccessor<Integer>
            JJF_LOADED_AMMO =
            SynchedEntityData.defineId(
                    Player.class,
                    EntityDataSerializers.INT
            );

    @Unique
    private final JJFLauncherState
            jjf$launcherState =
            new JJFLauncherState();

    @Override
    public JJFLauncherState
    jjf$getLauncherState() {
        return jjf$launcherState;
    }

    @Override
    public int jjf$getLoadedAmmoCount() {
        return ((Player)(Object)this)
                .getEntityData()
                .get(
                        JJF_LOADED_AMMO
                );
    }

    @Override
    public void jjf$setLoadedAmmoCount(
            int count
    ) {
        ((Player)(Object)this)
                .getEntityData()
                .set(
                        JJF_LOADED_AMMO,
                        Math.max(
                                0,
                                count
                        )
                );
    }

    @Inject(
            method = "defineSynchedData",
            at = @At("TAIL")
    )
    private void jjf$defineLauncherData(
            CallbackInfo ci
    ) {
        ((Player)(Object)this)
                .getEntityData()
                .define(
                        JJF_LOADED_AMMO,
                        0
                );
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void jjf$saveLauncherState(
            CompoundTag tag,
            CallbackInfo ci
    ) {
        CompoundTag jjfTag =
                tag.getCompound("jjf");

        jjfTag.put(
                "Launcher",
                jjf$launcherState.save()
        );

        tag.put(
                "jjf",
                jjfTag
        );
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void jjf$loadLauncherState(
            CompoundTag tag,
            CallbackInfo ci
    ) {
        jjf$launcherState
                .clearLoadedItem();

        jjf$setLoadedAmmoCount(0);

        if (!tag.contains(
                "jjf",
                Tag.TAG_COMPOUND
        )) {
            return;
        }

        CompoundTag jjfTag =
                tag.getCompound("jjf");

        if (!jjfTag.contains(
                "Launcher",
                Tag.TAG_COMPOUND
        )) {
            return;
        }

        jjf$launcherState.load(
                jjfTag.getCompound(
                        "Launcher"
                )
        );

        jjf$setLoadedAmmoCount(
                jjf$launcherState
                        .getLoadedCount()
        );
    }
}