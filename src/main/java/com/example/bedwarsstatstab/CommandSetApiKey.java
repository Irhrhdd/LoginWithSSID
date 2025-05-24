
package com.example.bedwarsstatstab;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandSetApiKey extends CommandBase {
    @Override
    public String getCommandName() {
        return "setapikey";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/setapikey <key>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText("Usage: /setapikey <key>"));
            return;
        }
        HypixelAPI.setApiKey(args[0]);
        sender.addChatMessage(new ChatComponentText("API key set."));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
