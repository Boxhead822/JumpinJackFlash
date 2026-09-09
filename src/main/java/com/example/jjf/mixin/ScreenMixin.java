package com.example.jjf.mixin;

import com.example.jjf.client.JJFHudSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow
    protected abstract <
            T extends GuiEventListener
                    & Renderable
                    & NarratableEntry
            > T addRenderableWidget(T widget);

    @Inject(
            method = "init(Lnet/minecraft/client/Minecraft;II)V",
            at = @At("TAIL")
    )
    private void jjf$addSettingsButtons(
            Minecraft minecraft,
            int width,
            int height,
            CallbackInfo ci
    ) {
        Screen screen =
                (Screen) (Object) this;

        if (!screen.getClass()
                .getName()
                .equals(
                        "net.hydra.jojomod.client.gui.PowerInventoryScreen"
                )) {
            return;
        }

        int buttonWidth = 105;
        int buttonHeight = 18;

        int x =
                width / 2 - 20;

        int y =
                height / 2 ;

        addRenderableWidget(
                Button.builder(
                                JJFHudSettings.ammoMessage(),
                                button -> {
                                    JJFHudSettings.toggleAmmoText();

                                    button.setMessage(
                                            JJFHudSettings.ammoMessage()
                                    );
                                }
                        )
                        .bounds(
                                x,
                                y,
                                buttonWidth,
                                buttonHeight
                        )
                        .build()
        );

        addRenderableWidget(
                Button.builder(
                                JJFHudSettings.launcherMessage(),
                                button -> {
                                    JJFHudSettings.toggleLauncherText();

                                    button.setMessage(
                                            JJFHudSettings.launcherMessage()
                                    );
                                }
                        )
                        .bounds(
                                x,
                                y + 22,
                                buttonWidth,
                                buttonHeight
                        )
                        .build()
        );
    }
}