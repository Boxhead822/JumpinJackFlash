package com.example.jjf.forge;

import com.example.jjf.JJFMod;
import com.example.jjf.client.models.stand.JumpinJackFlashModel;
import com.example.jjf.client.models.stand.renderers.JJFSpitRenderer;
import com.example.jjf.client.models.stand.renderers.JumpinJackFlashRenderer;
import com.example.jjf.forge.registry.ForgeJJFRegistries;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(JJFMod.MODID)
public final class JJFModForge {

    private static final ResourceKey<CreativeModeTab>
            ROUNDABOUT_WIP_TAB =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    ResourceLocation.fromNamespaceAndPath(
                            "roundabout",
                            "jojo_wip_features"
                    )
            );

    public JJFModForge(
            FMLJavaModLoadingContext context
    ) {
        JJFMod.setConfigDirectory(
                FMLPaths.CONFIGDIR.get()
        );

        IEventBus modBus =
                context.getModEventBus();

        ForgeJJFRegistries.register(
                modBus
        );

        modBus.addListener(
                this::registerAttributes
        );

        modBus.addListener(
                this::addCreativeItems
        );
    }

    private void registerAttributes(
            EntityAttributeCreationEvent event
    ) {
        event.put(
                ForgeJJFRegistries
                        .JUMPIN_JACK_FLASH
                        .get(),
                Mob.createMobAttributes()
                        .build()
        );
    }

    private void addCreativeItems(
            BuildCreativeModeTabContentsEvent event
    ) {
        if (!event.getTabKey()
                .equals(
                        ROUNDABOUT_WIP_TAB
                )) {
            return;
        }

        event.accept(
                ForgeJJFRegistries
                        .JJF_STAND_DISC
        );

        event.accept(
                ForgeJJFRegistries
                        .JJF_STAND_DISC_MAX
        );
    }

    @Mod.EventBusSubscriber(
            modid = JJFMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT
    )
    public static final class ClientEvents {

        private ClientEvents() {
        }

        @SubscribeEvent
        public static void registerRenderers(
                EntityRenderersEvent.RegisterRenderers event
        ) {
            event.registerEntityRenderer(
                    ForgeJJFRegistries
                            .JUMPIN_JACK_FLASH
                            .get(),
                    JumpinJackFlashRenderer::new
            );

            event.registerEntityRenderer(
                    ForgeJJFRegistries
                            .JJF_PROJECTILE
                            .get(),
                    ThrownItemRenderer::new
            );

            event.registerEntityRenderer(
                    ForgeJJFRegistries
                            .JJF_GUNPOWDER_CLOUD
                            .get(),
                    NoopRenderer::new
            );

            event.registerEntityRenderer(
                    ForgeJJFRegistries
                            .JJF_SPIT
                            .get(),
                    JJFSpitRenderer::new
            );
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(
                EntityRenderersEvent.RegisterLayerDefinitions event
        ) {
            event.registerLayerDefinition(
                    JumpinJackFlashModel
                            .LAYER_LOCATION,
                    JumpinJackFlashModel
                            ::createBodyLayer
            );
        }
    }
}
