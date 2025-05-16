package com.example.ssidmod;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Mod(modid = "ssidmod", name = "SSIDAccountSwitcher", version = "1.0")
public class SSIDMod {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiMultiplayer) {
            event.buttonList.add(new GuiButton(9001, event.gui.width - 110, 10, 100, 20, "Switch Account"));
        }
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiMultiplayer && event.button.id == 9001) {
            mc.displayGuiScreen(new LoginScreen());
        }
    }

    private class LoginScreen extends GuiScreen {
        private GuiTextField ssidField;
        private GuiButton loginButton;

        @Override
        public void initGui() {
            this.ssidField = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 - 10, 200, 20);
            this.loginButton = new GuiButton(1, width / 2 - 100, height / 2 + 15, "Login with SSID");
            this.buttonList.add(loginButton);
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 1) {
                String ssid = ssidField.getText();
                new Thread(() -> switchAccount(ssid)).start();
            }
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) {
            this.ssidField.textboxKeyTyped(typedChar, keyCode);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            super.mouseClicked(mouseX, mouseButton, mouseButton);
            this.ssidField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();
            drawCenteredString(fontRendererObj, "Enter SSID", width / 2, height / 2 - 30, 0xFFFFFF);
            this.ssidField.drawTextBox();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        private void switchAccount(String ssid) {
            try {
                URL url = new URL("https://session.minecraft.net/session/minecraft/profile/by-ssid/" + ssid);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                if (response.contains("id") && response.contains("name")) {
                    String uuid = response.split("\"id\":\"")[1].split("\"")[0];
                    String name = response.split("\"name\":\"")[1].split("\"")[0];
                    String token = ssid;

                    Session session = new Session(name, uuid, token, "mojang");

                    Field sessionField = Minecraft.class.getDeclaredField("session");
                    sessionField.setAccessible(true);
                    sessionField.set(mc, session);

                    mc.addScheduledTask(() -> {
                        mc.displayGuiScreen(new GuiMultiplayer(null));
                        IChatComponent msg = new ChatComponentText("§aSwitched to: " + name + " (" + uuid + ")");
                        mc.ingameGUI.getChatGUI().printChatMessage(msg);
                    });
                }
            } catch (Exception e) {
                mc.addScheduledTask(() -> {
                    mc.displayGuiScreen(new GuiMultiplayer(null));
                    IChatComponent msg = new ChatComponentText("§cFailed to switch account: " + e.getMessage());
                    mc.ingameGUI.getChatGUI().printChatMessage(msg);
                });
            }
        }
    }
}