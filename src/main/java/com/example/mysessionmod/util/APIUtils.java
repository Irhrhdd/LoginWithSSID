package com.example.mysessionmod.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class APIUtils {

    public static String[] getProfileInfo(String token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
        request.setHeader("Authorization", "Bearer " + token);

        CloseableHttpResponse response = client.execute(request);
        String jsonString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();

        String ign = jsonObject.get("name").getAsString();
        String uuid = jsonObject.get("id").getAsString();
        return new String[]{ign, uuid};
    }

    public static boolean validateSession(String token) {
        try {
            String[] profileInfo = getProfileInfo(token);
            String ign = profileInfo[0];
            String uuid = profileInfo[1];

            Minecraft mc = Minecraft.getMinecraft();
            return ign.equals(mc.getSession().getUsername()) &&
                   uuid.equals(mc.getSession().getPlayerID());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkOnline(String uuid) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("https://api.slothpixel.me/api/players/" + uuid);
            CloseableHttpResponse response = client.execute(request);
            String jsonString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
            return jsonObject.get("online").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int changeName(String newName, String token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut request = new HttpPut("https://api.minecraftservices.com/minecraft/profile/name/" + newName);
        request.setHeader("Authorization", "Bearer " + token);

        CloseableHttpResponse response = client.execute(request);
        return response.getStatusLine().getStatusCode();
    }

    public static int changeSkin(String url, String token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://api.minecraftservices.com/minecraft/profile/skins");
        request.setHeader("Authorization", "Bearer " + token);
        request.setHeader("Content-Type", "application/json");

        String jsonString = String.format("{ \"variant\": \"classic\", \"url\": \"%s\"}", url);
        request.setEntity(new StringEntity(jsonString));

        CloseableHttpResponse response = client.execute(request);
        return response.getStatusLine().getStatusCode();
    }
}
