package com.example.jjf.fabric;

import com.example.jjf.client.models.stand.JumpinJackFlashModel;
import com.example.jjf.client.models.stand.renderers.JJFSpitRenderer;
import com.example.jjf.client.models.stand.renderers.JumpinJackFlashRenderer;
import com.example.jjf.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public final class JJFModFabricClient
        implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(
                JumpinJackFlashModel.LAYER_LOCATION,
                JumpinJackFlashModel::createBodyLayer
        );

        EntityRendererRegistry.register(
                ModEntities.JUMPIN_JACK_FLASH,
                JumpinJackFlashRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities.JJF_PROJECTILE,
                ThrownItemRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities.JJF_GUNPOWDER_CLOUD,
                NoopRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities.JJF_SPIT,
                JJFSpitRenderer::new
        );
    }
}
