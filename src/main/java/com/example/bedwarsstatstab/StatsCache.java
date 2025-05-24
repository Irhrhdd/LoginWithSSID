package com.example.bedwarsstatstab;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StatsCache {
    private static final Map<UUID, String> statsMap = new ConcurrentHashMap<>();

    public static void put(UUID uuid, String stats) {
        statsMap.put(uuid, stats);
    }

    public static String get(UUID uuid) {
        return statsMap.getOrDefault(uuid, "");
    }
}
