package com.example.bedwarsstatstab;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TabOverlayHook {
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!mc.gameSettings.keyBindPlayerList.isKeyDown()) return;

        List<NetworkPlayerInfo> players = mc.getNetHandler().getPlayerInfoMap().stream()
            .sorted(Comparator.comparing(info -> info.getGameProfile().getName()))
            .collect(Collectors.toList());

        GuiIngame guiIngame = mc.ingameGUI;
        GuiPlayerTabOverlay tab = new GuiPlayerTabOverlay(mc, guiIngame);

        int y = 10;
        for (NetworkPlayerInfo info : players) {
            String name = tab.getPlayerName(info);
            BedWarsStats stats = HypixelAPI.getCachedStats(info.getGameProfile().getId());

            String line;
            if (stats != null) {
                line = ChatFormatting.YELLOW + "[⭐ " + stats.star + "] " +
                       ChatFormatting.RESET + name +
                       ChatFormatting.GRAY + " (FKDR: " + stats.fkdr + ")";
            } else {
                line = name;
            }

            mc.fontRendererObj.drawStringWithShadow(line, 10, y, 0xFFFFFF);
            y += 10;
        }
    }
}
