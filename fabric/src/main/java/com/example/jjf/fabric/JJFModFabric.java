package com.example.jjf.fabric;

import com.example.jjf.JJFMod;
import com.example.jjf.fabric.registry.FabricJJFRegistries;
import com.example.jjf.registry.ModEntities;
import com.example.jjf.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;

public final class JJFModFabric
        implements ModInitializer {

    private static final ResourceKey<CreativeModeTab>
            ROUNDABOUT_WIP_TAB =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    new ResourceLocation(
                            "roundabout",
                            "jojo_wip_features"
                    )
            );

    @Override
    public void onInitialize() {
        JJFMod.setConfigDirectory(
                FabricLoader.getInstance()
                        .getConfigDir()
        );

        FabricJJFRegistries.register();

        FabricDefaultAttributeRegistry.register(
                ModEntities.JUMPIN_JACK_FLASH,
                Mob.createMobAttributes()
        );

        ItemGroupEvents.modifyEntriesEvent(
                ROUNDABOUT_WIP_TAB
        ).register(
                entries -> {
                    entries.accept(
                            ModItems.JJF_STAND_DISC
                    );

                    entries.accept(
                            ModItems.JJF_STAND_DISC_MAX
                    );
                }
        );
    }
}
