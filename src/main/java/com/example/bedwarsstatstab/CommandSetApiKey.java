package com.example.bedwarsstatstab;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

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
            HypixelAPI.setApiKey(args[1]);
            sender.addChatMessage(new ChatComponentText("§a[BedWarsStatsTab] API key set!"));
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: /bwstats setkey <api-key>"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public static void register() {
        ClientCommandHandler.instance.registerCommand(new CommandSetApiKey());
    }
}
