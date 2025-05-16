package com.example.mysessionmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SessionChanger {

    public static void setSession(Session session) {
        // This sets the private "session" field in Minecraft using ReflectionHelper.
        ReflectionHelper.setPrivateValue(
            Minecraft.class,
            Minecraft.getMinecraft(),
            session,
            "session",          // MCP (Searge) name
            "field_71449_j"     // Obfuscated name (for reobf compatibility)
        );
    }
}
