package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
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
        GuiIngame guiIngame = mc.ingameGUI;

        try {
            Field tabListField = GuiIngame.class.getDeclaredField("tabList");
            tabListField.setAccessible(true);
            tabListField.set(guiIngame, new CustomTabOverlay(mc, guiIngame));
            System.out.println("[BedWarsStatsTab] CustomTabOverlay installed!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[BedWarsStatsTab] Failed to install CustomTabOverlay.");
        }
    }
}
