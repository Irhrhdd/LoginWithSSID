
package com.example.ssidmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.lang.reflect.Field;

@Mod(modid = "ssidmod", name = "SSID Account Switcher", version = "1.0")
public class SSIDMod {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiMainMenu) {
            event.buttonList.add(new GuiButton(9999, event.gui.width / 2 + 104, event.gui.height / 4 + 48, 80, 20, "Switch Account"));
        }
    }

    @SubscribeEvent
    public void onAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiMainMenu && event.button.id == 9999) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSSIDInput());
        }
    }

    public static class GuiSSIDInput extends GuiScreen {
        private GuiTextField ssidField;
        private GuiButton switchButton;

        @Override
        public void initGui() {
            this.buttonList.clear();
            ssidField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, this.height / 2 - 10, 200, 20);
            ssidField.setMaxStringLength(1024);
            switchButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 20, "Login with SSID");
            this.buttonList.add(switchButton);
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 1 && ssidField.getText() != null && !ssidField.getText().isEmpty()) {
                String ssid = ssidField.getText();
                Session session = new Session("Player", "00000000-0000-0000-0000-000000000000", ssid, "mojang");
                try {
                    Field sessionField = Minecraft.class.getDeclaredField("session");
                    sessionField.setAccessible(true);
                    sessionField.set(Minecraft.getMinecraft(), session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            ssidField.textboxKeyTyped(typedChar, keyCode);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            ssidField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();
            drawCenteredString(this.fontRendererObj, "Enter SSID", this.width / 2, this.height / 2 - 30, 0xFFFFFF);
            ssidField.drawTextBox();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        public void updateScreen() {
            ssidField.updateCursorCounter();
        }
    }
}
