package pro.kdray.funniray.mixer.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

public class Start extends BukkitCommand {

    public Start() {
        super(Commands.START.getName(), Commands.START.getDescription(), Commands.START.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender.hasPermission(Commands.START.getPermission().getNode())) {
            commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer] " + ChatColor.RESET + ChatColor.RED + "You do not have permission to run this command");
            return true;
        }
        if (!MixerSpigot.isRunning()) {
            MixerSpigot.startMain();
        } else {
            commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer]" + ChatColor.RESET + ChatColor.RED + " Interactive is already running");
        }
        return true;
    }
}
