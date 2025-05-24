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
        // DEBUG: Check if this method is being called
        String originalName = info.getGameProfile().getName();
        String debugPrefix = "§7[DBG] ";

        // Attempt to retrieve stats from the cache
        String stats = StatsCache.get(info.getGameProfile().getId());

        if (stats.isEmpty()) {
            return debugPrefix + originalName; // Show debug tag if no stats found
        }

        String fullName = stats + " " + originalName;
        ScorePlayerTeam team = info.getPlayerTeam();

        // Return formatted name with team styling if applicable
        return team != null ? team.formatString(fullName) : fullName;
    }
}
