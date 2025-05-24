package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;

public class CustomTabOverlay extends GuiPlayerTabOverlay {

    public CustomTabOverlay(Minecraft mc, GuiIngame guiIngame) {
        super(mc, guiIngame);
    }

    @Override
    public String getPlayerName(NetworkPlayerInfo info) {
        BedWarsStats stats = HypixelAPI.getCachedStats(info.getGameProfile().getId());
        String name = info.getGameProfile().getName();

        if (stats != null) {
            return String.format("\u00a76[\u2b50 %d] \u00a7f%s \u00a77(FKDR: %.2f)", stats.getStars(), name, stats.getFkdr());
        } else {
            HypixelAPI.fetchStatsAsync(info.getGameProfile().getId());
            return name;
        }
    }
} 
