package com.example.bedwarsstatstab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HypixelAPI {
    private static String apiKey;
    private static final Map<UUID, BedWarsStats> cache = new HashMap<>();

    public static void setApiKey(String key) {
        apiKey = key;
    }

    public static BedWarsStats getCachedStats(UUID uuid) {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        BedWarsStats stats = fetchStatsFromHypixel(uuid);
        if (stats != null) {
            cache.put(uuid, stats);
        }

        return stats;
    }

    private static BedWarsStats fetchStatsFromHypixel(UUID uuid) {
        if (apiKey == null || apiKey.isEmpty()) {
            return null;
        }

        try {
            URL url = new URL("https://api.hypixel.net/player?uuid=" + uuid + "&key=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            JsonObject json = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
            if (!json.has("player") || json.get("player").isJsonNull()) return null;

            JsonObject player = json.getAsJsonObject("player");
            JsonObject stats = player.getAsJsonObject("stats").getAsJsonObject("Bedwars");

            int star = stats.has("Experience") ? (int) (stats.get("Experience").getAsDouble() / 5000) : 0;
            int finalKills = stats.has("final_kills_bedwars") ? stats.get("final_kills_bedwars").getAsInt() : 0;
            int finalDeaths = stats.has("final_deaths_bedwars") ? stats.get("final_deaths_bedwars").getAsInt() : 1;

            double fkdr = finalDeaths == 0 ? finalKills : (double) finalKills / finalDeaths;
            return new BedWarsStats(star, fkdr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
