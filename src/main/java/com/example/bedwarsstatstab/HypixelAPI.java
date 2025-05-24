package com.example.bedwarsstatstab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class HypixelAPI {
    private static String apiKey = null;

    public static void setApiKey(String key) {
        apiKey = key;
    }

    public static String getFormattedStats(UUID uuid) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "[⭐ ?] (FKDR: ?)";
        }

        try {
            URL url = new URL("https://api.hypixel.net/player?key=" + apiKey + "&uuid=" + uuid.toString().replace("-", ""));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("GET");

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject();

            if (!json.get("success").getAsBoolean()) {
                return "[⭐ ?] (FKDR: ?)";
            }

            JsonObject player = json.getAsJsonObject("player");
            if (player == null || !player.has("stats")) {
                return "[⭐ 0] (FKDR: 0.00)";
            }

            JsonObject bedwars = player
                    .getAsJsonObject("stats")
                    .getAsJsonObject("Bedwars");

            int star = bedwars.has("Experience") ? getStarFromExp(bedwars.get("Experience").getAsDouble()) : 0;
            double fkdr = calculateFKDR(bedwars);

            return String.format("[⭐ %d] (FKDR: %.2f)", star, fkdr);
        } catch (Exception e) {
            return "[⭐ ?] (FKDR: ?)";
        }
    }

    private static int getStarFromExp(double exp) {
        return (int) Math.floor(exp / 5000); // simple linear estimate
    }

    private static double calculateFKDR(JsonObject bw) {
        int fk = bw.has("final_kills_bedwars") ? bw.get("final_kills_bedwars").getAsInt() : 0;
        int fd = bw.has("final_deaths_bedwars") ? bw.get("final_deaths_bedwars").getAsInt() : 1;
        return fd == 0 ? fk : (double) fk / fd;
    }
}
