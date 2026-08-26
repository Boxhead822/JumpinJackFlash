package com.example.jjf.registry;

import com.example.jjf.JJFMod;
import com.example.jjf.entity.stand.JumpinJackFlashEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, JJFMod.MODID);

    public static final RegistryObject<EntityType<JumpinJackFlashEntity>> JUMPIN_JACK_FLASH =
            ENTITY_TYPES.register(
                    "jumpin_jack_flash",
                    () -> EntityType.Builder
                            .of(JumpinJackFlashEntity::new, MobCategory.MISC)
                            .sized(0.75F, 2.05F)
                            .clientTrackingRange(14)
                            .build("jjf:jumpin_jack_flash")
            );
}