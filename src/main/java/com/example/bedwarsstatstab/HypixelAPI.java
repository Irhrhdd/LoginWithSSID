package com.example.bedwarsstatstab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class HypixelAPI {
    private static String apiKey = "";

    public static void setApiKey(String key) {
        apiKey = key;
    }

    public static String getFormattedStats(UUID uuid) {
        try {
            URL url = new URL("https://api.hypixel.net/player?key=" + apiKey + "&uuid=" + uuid.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject();

            if (!json.get("success").getAsBoolean()) return "[\u2B50 ?]";

            JsonObject player = json.getAsJsonObject("player");
            JsonObject stats = player.getAsJsonObject("stats").getAsJsonObject("Bedwars");
            int star = stats.has("Experience") ? stats.get("Experience").getAsInt() / 500 : 0;
            int fk = stats.has("final_kills_bedwars") ? stats.get("final_kills_bedwars").getAsInt() : 0;
            int fd = stats.has("final_deaths_bedwars") ? stats.get("final_deaths_bedwars").getAsInt() : 1;
            double fkdr = fd == 0 ? fk : (double) fk / fd;
            return "[\u2B50 " + star + "] (FKDR: " + String.format("%.2f", fkdr) + ")";
        } catch (Exception e) {
            return "[\u2B50 ?]";
        }
    }
}
