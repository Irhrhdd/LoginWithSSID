package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class TabOverlayHook {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<UUID, String> statsCache = new HashMap<>();

    @SubscribeEvent
    public void onRenderTab(RenderGameOverlayEvent.Text event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        List<NetworkPlayerInfo> players = new ArrayList<>(mc.thePlayer.sendQueue.getPlayerInfoMap());
        players.sort(Comparator.comparing(p -> p.getGameProfile().getName()));

        for (NetworkPlayerInfo info : players) {
            UUID uuid = info.getGameProfile().getId();
            String name = info.getGameProfile().getName();

            String stats = statsCache.computeIfAbsent(uuid, HypixelAPI::getFormattedStats);
            event.left.add(stats + " " + name);
        }
    }
}
