package com.example.bedwarsstatstab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HypixelAPI {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static String apiKey = "";

    public static void setApiKey(String key) {
        apiKey = key;
    }

    public static void fetchAndCachePlayerStats(UUID uuid) {
        if (apiKey == null || apiKey.isEmpty()) return;

        executor.submit(() -> {
            try {
                String uuidStr = uuid.toString().replace("-", "");
                // Build URL for Hypixel API player stats
                URL url = new URL("https://api.hypixel.net/player?key=" + apiKey + "&uuid=" + uuidStr);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
                reader.close();

                if (json.has("success") && json.get("success").getAsBoolean()) {
                    JsonObject player = json.getAsJsonObject("player");
                    if (player != null) {
                        JsonObject stats = player.getAsJsonObject("stats");
                        if (stats != null) {
                            JsonObject bedwars = stats.getAsJsonObject("Bedwars");
                            if (bedwars != null) {
                                int stars = bedwars.has("stars") ? bedwars.get("stars").getAsInt() : 0;
                                double fkdr = 0.0;
                                int finalKills = bedwars.has("final_kills_bedwars") ? bedwars.get("final_kills_bedwars").getAsInt() : 0;
                                int finalDeaths = bedwars.has("final_deaths_bedwars") ? bedwars.get("final_deaths_bedwars").getAsInt() : 0;
                                if (finalDeaths != 0) {
                                    fkdr = (double) finalKills / finalDeaths;
                                }

                                String formattedStats = "[⭐ " + stars + "] (FKDR: " + String.format("%.2f", fkdr) + ")";
                                StatsCache.put(uuid, formattedStats);
                                return;
                            }
                        }
                    }
                }
                // If anything missing, clear cache for that uuid
                StatsCache.put(uuid, "");
            } catch (Exception e) {
                // On error clear cache for that player
                StatsCache.put(uuid, "");
                e.printStackTrace();
            }
        });
    }
}
