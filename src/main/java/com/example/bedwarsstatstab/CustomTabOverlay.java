package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.UUID;

public class CustomTabOverlay extends GuiPlayerTabOverlay {

    private final Minecraft mc;

    public CustomTabOverlay(Minecraft mcIn, GuiIngame guiIngame) {
        super(mcIn, guiIngame);
        this.mc = mcIn;
    }

    @Override
    public String getPlayerName(NetworkPlayerInfo info) {
        UUID uuid = info.getGameProfile().getId();
        BedWarsStats stats = HypixelAPI.getCachedStats(uuid);

        String name = info.getGameProfile().getName();

        if (stats != null) {
            return String.format("§6[⭐ %d] §f%s §7(FKDR: %.2f)", stats.getStars(), name, stats.getFkdr());
        } else {
            return name;
        }
    }
}
