package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class CustomTabOverlay extends GuiPlayerTabOverlay {

    public CustomTabOverlay(Minecraft mc, GuiIngame guiIngame) {
        super(mc, guiIngame);
    }

    @Override
    public String getPlayerName(NetworkPlayerInfo info) {
        String name = info.getGameProfile().getName();
        String stats = StatsCache.get(info.getGameProfile().getId());
        String full = (stats.isEmpty() ? name : stats + " " + name);

        ScorePlayerTeam team = info.getPlayerTeam();
        return team != null ? team.formatString(full) : full;
    }
}
