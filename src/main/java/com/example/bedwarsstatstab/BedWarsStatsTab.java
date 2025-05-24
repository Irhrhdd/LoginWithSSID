package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraftforge.common.MinecraftForge;
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
        replaceTabOverlay(Minecraft.getMinecraft());
        MinecraftForge.EVENT_BUS.register(new TabOverlayHook());
        CommandSetApiKey.register();
    }

    private void replaceTabOverlay(Minecraft mc) {
        try {
            Field tabField = GuiIngame.class.getDeclaredField("field_175196_v"); // tab list field (obfuscated)
            tabField.setAccessible(true);
            tabField.set(mc.ingameGUI, new CustomTabOverlay(mc, mc.ingameGUI));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
