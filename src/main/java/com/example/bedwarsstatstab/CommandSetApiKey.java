package com.example.bedwarsstatstab;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandSetApiKey extends CommandBase {

    @Override
    public String getCommandName() {
        return "bwstats";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bwstats setkey <api-key>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("setkey")) {
            String key = args[1];
            HypixelAPI.setApiKey(key);
            sender.addChatMessage(new ChatComponentText("§a[BedWarsStatsTab] API key set!"));
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: /bwstats setkey <api-key>"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
