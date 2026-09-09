package com.example.jjf.registry;

import com.example.jjf.JJFMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, JJFMod.MODID);

    public static final RegistryObject<SoundEvent> JUMPIN_JACK_FLASH_SUMMON =
            SOUND_EVENTS.register(
                    "jumpin_jack_flash_summon",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    "jjf",
                                    "jumpin_jack_flash_summon"
                            )

                    )
            );
    public static final RegistryObject<SoundEvent> SUCTION_LAUNCH =
            SOUND_EVENTS.register("suction_launch",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("jjf", "suction_launch")
                    ));
    public static final RegistryObject<SoundEvent> LAUNCHER_SHOOT =
            SOUND_EVENTS.register(
                    "launcher_shoot",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    "jjf",
                                    "launcher_shoot"
                            )
                    )
            );

    public static final RegistryObject<SoundEvent> LAUNCHER_BURST =
            SOUND_EVENTS.register(
                    "launcher_burst",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    "jjf",
                                    "launcher_burst"
                            )
                    )
            );
}