package com.example.bedwarsstatstab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class HypixelAPI {
    private static final String BASE_URL = "https://api.hypixel.net/player?key=";
    private static String apiKey = null;
    private static final ConcurrentHashMap<UUID, BedWarsStats> cache = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void setApiKey(String key) {
        apiKey = key;
    }

    public static BedWarsStats getCachedStats(UUID uuid) {
        return cache.get(uuid);
    }

    public static void fetchStatsAsync(UUID uuid) {
        if (apiKey == null || apiKey.isEmpty()) return;

        executor.submit(() -> {
            try {
                URL url = new URL(BASE_URL + apiKey + "&uuid=" + uuid.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                if (conn.getResponseCode() != 200) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§c[BedWarsStatsTab] Failed to fetch stats (HTTP " + conn.getResponseCode() + ")"));
                    return;
                }

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject();

                boolean success = json.get("success").getAsBoolean();
                if (!success) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§c[BedWarsStatsTab] API request failed"));
                    return;
                }

                JsonObject player = json.getAsJsonObject("player");
                if (player == null) return;

                JsonObject statsObj = player.getAsJsonObject("stats");
                if (statsObj == null) return;

                JsonObject bedwars = statsObj.getAsJsonObject("Bedwars");
                if (bedwars == null) return;

                int stars = bedwars.has("bedwars_level") ? bedwars.get("bedwars_level").getAsInt() : 0;

                int kills = bedwars.has("kills_bedwars") ? bedwars.get("kills_bedwars").getAsInt() : 0;
                int deaths = bedwars.has("deaths_bedwars") ? bedwars.get("deaths_bedwars").getAsInt() : 0;
                double fkdr = deaths == 0 ? kills : ((double) kills / deaths);

                BedWarsStats stats = new BedWarsStats(stars, fkdr);
                cache.put(uuid, stats);

            } catch (Exception e) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§c[BedWarsStatsTab] Exception while fetching stats"));
                e.printStackTrace();
            }
        });
    }
}
