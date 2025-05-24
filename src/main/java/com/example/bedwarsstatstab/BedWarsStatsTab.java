package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
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
        mc.ingameGUI.tabList = new CustomTabOverlay(mc, mc.ingameGUI);
        MinecraftForge.EVENT_BUS.register(new TabOverlayHook());
        CommandSetApiKey.register();
    }
}
