
package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TabOverlayHook {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<UUID, String> playerStats = new HashMap<>();

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!mc.isSingleplayer() && mc.thePlayer != null && mc.getCurrentServerData() != null &&
            mc.getCurrentServerData().serverIP.contains("hypixel.net")) {
            // This will eventually hook the tab overlay and insert stats
            List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.sortedPlayerList
                (mc.thePlayer.sendQueue.getPlayerInfoMap());

            for (NetworkPlayerInfo info : players) {
                UUID uuid = info.getGameProfile().getId();
                String name = info.getGameProfile().getName();

                // Placeholder: Eventually insert real stats from API
                String prefix = "[⭐ ?] ";
                String suffix = " (FKDR: ?)";

                if (playerStats.containsKey(uuid)) {
                    prefix = playerStats.get(uuid);
                    suffix = "";
                }

                info.displayName = new net.minecraft.util.IChatComponent() {
                    @Override
                    public String getUnformattedText() {
                        return prefix + name + suffix;
                    }
                    // Stub methods to satisfy interface
                    public String getUnformattedTextForChat() { return getUnformattedText(); }
                    public net.minecraft.util.IChatComponent createCopy() { return this; }
                    public net.minecraft.util.IChatComponent setChatStyle(net.minecraft.util.ChatStyle style) { return this; }
                    public net.minecraft.util.ChatStyle getChatStyle() { return null; }
                    public net.minecraft.util.IChatComponent appendText(String text) { return this; }
                    public net.minecraft.util.IChatComponent appendSibling(net.minecraft.util.IChatComponent component) { return this; }
                    public List<net.minecraft.util.IChatComponent> getSiblings() { return null; }
                };
            }
        }
    }
}
