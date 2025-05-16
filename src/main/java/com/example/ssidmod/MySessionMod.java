package com.example.mysessionmod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = MySessionMod.MODID, name = MySessionMod.NAME, version = MySessionMod.VERSION)
public class MySessionMod {
    public static final String MODID = "mysessionmod";
    public static final String NAME = "My Session Mod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Session session = Minecraft.getMinecraft().getSession();

        System.out.println("==========[ Session Info ]==========");
        System.out.println("Username: " + session.getUsername());
        System.out.println("UUID: " + session.getPlayerID());
        System.out.println("Access Token: " + session.getToken());
        System.out.println("====================================");
    }
}
