package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderPlayerListEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class TabOverlayHook {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<UUID, String> statsCache = new HashMap<>();

    @SubscribeEvent
    public void onRenderTab(RenderPlayerListEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        List<NetworkPlayerInfo> players = new ArrayList<>(event.players);
        for (NetworkPlayerInfo info : players) {
            UUID uuid = info.getGameProfile().getId();
            String name = info.getGameProfile().getName();
            String stats = statsCache.computeIfAbsent(uuid, HypixelAPI::getFormattedStats);

            String full = stats + " " + name;
            ScorePlayerTeam team = info.getPlayerTeam();
            info.displayName = new ChatComponentText((team != null ? team.formatString(full) : full));
        }
    }
}
