package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.lang.reflect.Field;

@Mod(modid = BedWarsStatsTab.MODID, name = BedWarsStatsTab.NAME, version = BedWarsStatsTab.VERSION)
public class BedWarsStatsTab {
    public static final String MODID = "bedwarsstatstab";
    public static final String NAME = "BedWars Stats Tab";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            GuiIngame ingameGUI = mc.ingameGUI;

            // Loop through fields to find the GuiPlayerTabOverlay
            for (Field field : GuiIngame.class.getDeclaredFields()) {
                if (GuiPlayerTabOverlay.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    field.set(ingameGUI, new CustomTabOverlay(mc, ingameGUI));
                    GuiPlayerTabOverlay test = (GuiPlayerTabOverlay) field.get(ingameGUI);
                    System.out.println("[BedWarsStatsTab] Successfully injected CustomTabOverlay into: " + field.getName());
                    System.out.println("[BedWarsStatsTab] Overlay class now: " + test.getClass().getName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register command
        CommandSetApiKey.register();
    }
}
