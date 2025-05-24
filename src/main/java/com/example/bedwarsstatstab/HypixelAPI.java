package com.example.bedwarsstatstab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HypixelAPI {
    private static String apiKey = "";
    private static final Map<UUID, BedWarsStats> cache = new ConcurrentHashMap<>();

    public static void setApiKey(String key) {
        apiKey = key;
    }

    public static BedWarsStats getCachedStats(UUID uuid) {
        return cache.get(uuid);
    }

    public static void fetchStatsAsync(UUID uuid) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.hypixel.net/player?key=" + apiKey + "&uuid=" + uuid.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                JsonObject json = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();

                if (json.get("success").getAsBoolean()) {
                    JsonObject player = json.getAsJsonObject("player");
                    if (player != null && player.has("stats")) {
                        JsonObject stats = player.getAsJsonObject("stats").getAsJsonObject("Bedwars");

                        int stars = stats.has("Experience") ? stats.get("Experience").getAsInt() / 500 : 0;
                        int finalKills = stats.has("final_kills_bedwars") ? stats.get("final_kills_bedwars").getAsInt() : 0;
                        int finalDeaths = stats.has("final_deaths_bedwars") ? stats.get("final_deaths_bedwars").getAsInt() : 1;
                        double fkdr = finalKills / (double) finalDeaths;

                        cache.put(uuid, new BedWarsStats(stars, fkdr));
                    }
                }
            } catch (Exception e) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§c[BedWarsStatsTab] Failed to fetch stats"));
            }
        }).start();
    }
}
