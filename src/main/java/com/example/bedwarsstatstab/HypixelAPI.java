package com.example.bedwarsstatstab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HypixelAPI {

    private static String apiKey = "";
    private static final Map<UUID, BedWarsStats> cache = new HashMap<>();

    public static void setApiKey(String key) {
        apiKey = key;
    }

    public static BedWarsStats getCachedStats(UUID uuid) {
        return cache.get(uuid);
    }

    public static void fetchStatsAsync(UUID uuid) {
        // If already cached, skip
        if (cache.containsKey(uuid)) return;

        new Thread(() -> {
            try {
                URL url = new URL("https://api.hypixel.net/player?key=" + apiKey + "&uuid=" + uuid.toString().replace("-", ""));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonParser parser = new JsonParser(); // Compatible with GSON 2.2.4
                JsonObject json = parser.parse(reader).getAsJsonObject();
                reader.close();

                JsonObject player = json.getAsJsonObject("player");
                if (player != null && player.has("stats")) {
                    JsonObject stats = player.getAsJsonObject("stats").getAsJsonObject("Bedwars");

                    int star = player.has("achievements") ? player.getAsJsonObject("achievements").get("bedwars_level").getAsInt() : 0;
                    int fk = stats.has("final_kills_bedwars") ? stats.get("final_kills_bedwars").getAsInt() : 0;
                    int fd = stats.has("final_deaths_bedwars") ? stats.get("final_deaths_bedwars").getAsInt() : 1;
                    double fkdr = fd == 0 ? fk : (double) fk / fd;

                    BedWarsStats bedWarsStats = new BedWarsStats(star, fkdr);
                    cache.put(uuid, bedWarsStats);
                }
            } catch (Exception e) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new net.minecraft.util.ChatComponentText("§c[BedWarsStatsTab] Failed to fetch stats for UUID: " + uuid));
            }
        }).start();
    }
}
