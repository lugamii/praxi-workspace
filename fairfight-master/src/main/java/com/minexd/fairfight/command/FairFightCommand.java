package com.minexd.fairfight.command;

import com.minexd.fairfight.FairFight;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.util.zoot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FairFightCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(CC.translate("&6/fairfight check <filter>"));
                player.sendMessage(CC.translate("&6/fairfight client <player>"));
                player.sendMessage(CC.translate("&6/fairfight alerts"));
            } else if (args[0].equalsIgnoreCase("check") && args.length >= 2) {
                boolean contains = FairFight.getInstance().getDisabledChecks().remove(args[1].toUpperCase());

                if (contains) {
                    player.sendMessage(CC.GREEN + "The filter `" + args[1] + "` has been removed.");
                } else {
                    FairFight.getInstance().getDisabledChecks().add(args[1].toUpperCase());
                    player.sendMessage(CC.GREEN + "The filter `" + args[1] + "` has been added.");
                }
            } else if (args[0].equalsIgnoreCase("client") && args.length >= 2) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target != null) {
                    final PlayerData playerData = FairFight.getInstance().getPlayerDataManager().getPlayerData(target);

                    if (playerData.getClient() != null) {
                        player.sendMessage(CC.PINK + target.getName() + CC.YELLOW + " is on " + CC.PINK + playerData.getClient().getName());
                    } else {
                        player.sendMessage(CC.PINK + target.getName() + CC.YELLOW + " is on " + CC.PINK + "Unknown");
                    }
                } else {
                    player.sendMessage(CC.translate("&cThat player is not online."));
                }
            } else if (args[0].equalsIgnoreCase("alerts")) {
                boolean receiving = FairFight.getInstance().toggleAlerts(player);

                if (receiving) {
                    player.sendMessage(CC.GREEN + "You enabled FairFight alerts.");
                } else {
                    player.sendMessage(CC.RED + "You disabled FairFight alerts.");
                }
            } else {
                player.sendMessage(CC.translate("&6/fairfight check <filter>"));
                player.sendMessage(CC.translate("&6/fairfight client <player>"));
                player.sendMessage(CC.translate("&6/fairfight alerts"));
            }
        } else {
            sender.sendMessage(CC.translate("&cThis command is for players only!"));
        }

        return true;
    }
}
