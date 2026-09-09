package com.example.jjf.registry;

import com.example.jjf.JJFMod;
import com.example.jjf.entity.stand.JumpinJackFlashEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.example.jjf.entity.projectile.JJFProjectileEntity;
import com.example.jjf.entity.JJFGunpowderCloudEntity;
import com.example.jjf.entity.projectile.JJFSpitEntity;

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
    public static final RegistryObject<EntityType<JJFProjectileEntity>> JJF_PROJECTILE =
            ENTITY_TYPES.register(
                    "jjf_projectile",
                    () -> EntityType.Builder
                            .<JJFProjectileEntity>of(
                                    JJFProjectileEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(8)
                            .build("jjf:jjf_projectile")
            );
    public static final RegistryObject<EntityType<JJFGunpowderCloudEntity>>
            JJF_GUNPOWDER_CLOUD =
            ENTITY_TYPES.register(
                    "jjf_gunpowder_cloud",
                    () -> EntityType.Builder
                            .<JJFGunpowderCloudEntity>of(
                                    JJFGunpowderCloudEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(
                                    3.5F,
                                    2.0F
                            )
                            .clientTrackingRange(8)
                            .updateInterval(2)
                            .build(
                                    "jjf:jjf_gunpowder_cloud"
                            )
            );
    public static final RegistryObject<EntityType<JJFSpitEntity>> JJF_SPIT =
            ENTITY_TYPES.register(
                    "jjf_spit",
                    () -> EntityType.Builder
                            .<JJFSpitEntity>of(
                                    JJFSpitEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(
                                    0.18F,
                                    0.18F
                            )
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build(
                                    "jjf:jjf_spit"
                            )
            );

}