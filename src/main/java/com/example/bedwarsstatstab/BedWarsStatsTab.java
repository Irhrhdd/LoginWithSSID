package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BedWarsStatsTab.MODID, name = BedWarsStatsTab.NAME, version = BedWarsStatsTab.VERSION)
public class BedWarsStatsTab {
    public static final String MODID = "bedwarsstatstab";
    public static final String NAME = "BedWars Stats Tab";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiIngame guiIngame = mc.ingameGUI;

        try {
            // Use reflection to set the private tabList field to your custom overlay
            java.lang.reflect.Field tabListField = GuiIngame.class.getDeclaredField("tabList");
            tabListField.setAccessible(true);
            tabListField.set(guiIngame, new CustomTabOverlay(mc, guiIngame));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register command for setting the API key
        CommandSetApiKey.register();

        // Initialize tab overlay hook for stats fetching
        new TabOverlayHook();
    }
}
