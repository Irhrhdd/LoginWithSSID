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
        Minecraft mc = Minecraft.getMinecraft();
        GuiIngame ingameGUI = mc.ingameGUI;

        try {
            Field tabListField = GuiIngame.class.getDeclaredField("field_175196_v"); // obfuscated name for tabList
            tabListField.setAccessible(true);
            tabListField.set(ingameGUI, new CustomTabOverlay(mc, ingameGUI));
            System.out.println("[BedWarsStatsTab] Successfully injected custom tab overlay.");
        } catch (Exception e) {
            System.err.println("[BedWarsStatsTab] Failed to inject custom tab overlay.");
            e.printStackTrace();
        }

        // Register the /bwstats command
        CommandSetApiKey.register();
    }
}
