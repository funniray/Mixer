package pro.kdray.funniray.mixer.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

public class MainCommand extends BukkitCommand {

    public MainCommand() {
        super(Commands.MAIN.getName(), Commands.MAIN.getDescription(), Commands.MAIN.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.hasPermission(Commands.MAIN.getPermission().getNode())) {
            commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer] " + ChatColor.RESET + ChatColor.RED + "You do not have permission to run this command");
            return true;
        }
        switch (strings.length) {
            case 0:
                return false;
            case 1:
                if (strings[0].equals("reload")) {
                    MixerSpigot.reload();
                    commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer] " + ChatColor.RESET + ChatColor.BLUE + "Reloaded the config");
                    return true;
                } else {
                    return false;
                }
            case 2:
                if (strings[0].equals("token")) {
                    MixerSpigot.setConfig("token", strings[1]);
                    MixerSpigot.reload();
                    commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer] " + ChatColor.RESET + ChatColor.BLUE + "Set token!");
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }
}
