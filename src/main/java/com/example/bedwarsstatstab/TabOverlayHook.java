package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class TabOverlayHook {
    private Minecraft mc = Minecraft.getMinecraft();
    private long lastUpdateTime = 0;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;

        long now = System.currentTimeMillis();
        if (now - lastUpdateTime > 30000) { // 30 seconds cooldown
            lastUpdateTime = now;

            for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
                UUID uuid = info.getGameProfile().getId();
                HypixelAPI.fetchAndCachePlayerStats(uuid);
            }
        }

        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            UUID uuid = info.getGameProfile().getId();
            String name = info.getGameProfile().getName();

            String stats = StatsCache.get(uuid);

            String fullName = stats.isEmpty() ? name : stats + " " + name;

            ScorePlayerTeam team = info.getPlayerTeam();
            ChatComponentText chatName = new ChatComponentText(team != null ? team.formatString(fullName) : fullName);
            info.setDisplayName(chatName);
        }
    }
}
