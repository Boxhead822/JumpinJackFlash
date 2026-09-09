package com.example.jjf.client;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class JJFHudSettings {

    private static final Path SETTINGS_FILE =
            FMLPaths.CONFIGDIR.get()
                    .resolve("jjf-hud.properties");

    private static boolean showAmmoText = true;
    private static boolean showLauncherText = true;

    static {
        load();
    }

    private JJFHudSettings() {
    }

    public static boolean showAmmoText() {
        return showAmmoText;
    }

    public static boolean showLauncherText() {
        return showLauncherText;
    }

    public static void toggleAmmoText() {
        showAmmoText = !showAmmoText;
        save();
    }

    public static void toggleLauncherText() {
        showLauncherText = !showLauncherText;
        save();
    }

    public static Component ammoMessage() {
        return Component.literal(
                "Ammo Text: "
                        + (showAmmoText ? "ON" : "OFF")
        );
    }

    public static Component launcherMessage() {
        return Component.literal(
                "Launcher Text: "
                        + (showLauncherText ? "ON" : "OFF")
        );
    }

    private static void load() {
        if (!Files.exists(SETTINGS_FILE)) {
            return;
        }

        Properties properties =
                new Properties();

        try (InputStream input =
                     Files.newInputStream(
                             SETTINGS_FILE
                     )) {

            properties.load(input);

            showAmmoText =
                    Boolean.parseBoolean(
                            properties.getProperty(
                                    "showAmmoText",
                                    "true"
                            )
                    );

            showLauncherText =
                    Boolean.parseBoolean(
                            properties.getProperty(
                                    "showLauncherText",
                                    "true"
                            )
                    );

        } catch (IOException ignored) {
        }
    }

    private static void save() {
        Properties properties =
                new Properties();

        properties.setProperty(
                "showAmmoText",
                Boolean.toString(
                        showAmmoText
                )
        );

        properties.setProperty(
                "showLauncherText",
                Boolean.toString(
                        showLauncherText
                )
        );

        try {
            Files.createDirectories(
                    SETTINGS_FILE.getParent()
            );

            try (OutputStream output =
                         Files.newOutputStream(
                                 SETTINGS_FILE
                         )) {

                properties.store(
                        output,
                        "Jumpin Jack Flash HUD"
                );
            }

        } catch (IOException ignored) {
        }
    }
}