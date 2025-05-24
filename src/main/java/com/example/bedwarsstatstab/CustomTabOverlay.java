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
    protected String getPlayerName(NetworkPlayerInfo info) {
        String name = super.getPlayerName(info);
        ScorePlayerTeam team = info.getPlayerTeam();
        String formatted = (team != null ? team.formatString("[DBG] " + name) : "[DBG] " + name);
        return formatted;
    }
}
