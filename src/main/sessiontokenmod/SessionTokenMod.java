package com.example.sessiontokenmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = SessionTokenMod.MODID, name = SessionTokenMod.NAME, version = SessionTokenMod.VERSION)
public class SessionTokenMod
{
    public static final String MODID = "sessiontokenmod";
    public static final String NAME = "Session Token Mod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Initialization code here
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.getServer().getPlayerList().addPlayerLoginHandler(new PlayerLoginHandler());
    }
}
