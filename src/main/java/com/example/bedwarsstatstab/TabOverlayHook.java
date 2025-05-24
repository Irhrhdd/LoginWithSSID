package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.UUID;

public class TabOverlayHook {
    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;

        Collection<NetworkPlayerInfo> players = mc.ingameGUI.getTabList();

        for (NetworkPlayerInfo info : players) {
            UUID uuid = info.getGameProfile().getId();
            String name = info.getGameProfile().getName();

            String stats = HypixelAPI.getFormattedStats(uuid);
            String fullName = stats + " " + name;

            ScorePlayerTeam team = info.getPlayerTeam();
            ChatComponentText chatName = new ChatComponentText(team != null ? team.formatString(fullName) : fullName);
            info.setDisplayName(chatName);
        }
    }
}
