package com.example.mysessionmod;

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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Mod(modid = "mysessionmod", name = "MySessionMod", version = "1.0")
public class MySessionMod {

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
            this.ssidField.setFocused(true);
            this.loginButton = new GuiButton(1, width / 2 - 100, height / 2 + 15, "Login with SSID");
            this.buttonList.add(loginButton);
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 1) {
                String ssid = ssidField.getText().trim();
                if (!ssid.isEmpty()) {
                    switchAccount(ssid);
                } else {
                    mc.thePlayer.addChatMessage(new ChatComponentText("§cSSID cannot be empty."));
                }
            }
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) {
            this.ssidField.textboxKeyTyped(typedChar, keyCode);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
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
            new Thread(() -> {
                try {
                    URL url = new URL("https://session.minecraft.net/session/minecraft/profile/by-ssid/" + ssid);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    InputStream is = conn.getInputStream();
                    Scanner scanner = new Scanner(is).useDelimiter("\\A");
                    String response = scanner.hasNext() ? scanner.next() : "";
                    scanner.close();
                    is.close();

                    if (response.contains("\"id\"") && response.contains("\"name\"")) {
                        String uuid = response.split("\"id\":\"")[1].split("\"")[0];
                        String name = response.split("\"name\":\"")[1].split("\"")[0];

                        Session session = new Session(name, uuid, ssid, "mojang");

                        Field sessionField = Minecraft.class.getDeclaredField("session");
                        sessionField.setAccessible(true);
                        sessionField.set(mc, session);

                        mc.addScheduledTask(() -> {
                            mc.displayGuiScreen(new GuiMultiplayer(null));
                            IChatComponent msg = new ChatComponentText("§aSwitched to: " + name + " (" + uuid + ")");
                            mc.ingameGUI.getChatGUI().printChatMessage(msg);
                        });
                    } else {
                        mc.addScheduledTask(() -> {
                            mc.displayGuiScreen(new GuiMultiplayer(null));
                            IChatComponent msg = new ChatComponentText("§cInvalid SSID: Unable to retrieve account information.");
                            mc.ingameGUI.getChatGUI().printChatMessage(msg);
                        });
                    }

                } catch (Exception e) {
                    mc.addScheduledTask(() -> {
                        mc.displayGuiScreen(new GuiMultiplayer(null));
                        IChatComponent msg = new ChatComponentText("§cFailed to switch account: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                        mc.ingameGUI.getChatGUI().printChatMessage(msg);
                    });
                }
            }).start();
        }
    }
}
