package com.example.bedwarsstatstab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.UUID;

public class TabOverlayHook {

    private final Minecraft mc = Minecraft.getMinecraft();

    public TabOverlayHook() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!mc.gameSettings.keyBindPlayerList.isKeyDown()) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        // Debug message to confirm event fires
        mc.thePlayer.addChatMessage(new ChatComponentText("§a[BedWarsStatsTab] Updating tab overlay"));

        Collection<NetworkPlayerInfo> players = mc.getNetHandler().getPlayerInfoMap();

        for (NetworkPlayerInfo info : players) {
            UUID uuid = info.getGameProfile().getId();
            BedWarsStats stats = HypixelAPI.getCachedStats(uuid);

            if (stats == null) {
                HypixelAPI.fetchStatsAsync(uuid);
                continue;
            }

            String name = info.getGameProfile().getName();
            String formatted = String.format("§6[⭐ %d] §f%s §7(FKDR: %.2f)", stats.getStars(), name, stats.getFkdr());

            try {
                Field displayNameField = NetworkPlayerInfo.class.getDeclaredField("displayName");
                displayNameField.setAccessible(true);
                displayNameField.set(info, new ChatComponentText(formatted));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
