package pro.kdray.funniray.mixer.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

public class SwitchScene extends BukkitCommand {
    public SwitchScene() {
        super(Commands.SWITCHSCENE.getName(), Commands.SWITCHSCENE.getDescription(), Commands.SWITCHSCENE.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.hasPermission(Commands.SWITCHSCENE.getPermission().getNode())) {
            commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer] " + ChatColor.RESET + ChatColor.RED + "You do not have permission to run this command");
            return true;
        }
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer]" + ChatColor.RESET + ChatColor.RED + " You need to provide a valid scene");
        }
        if (MixerSpigot.isRunning()) {
            MixerSpigot.getApi().getInteractive().switchAllScenes(strings[0]);
        } else {
            commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer]" + ChatColor.RESET + ChatColor.RED + " Interactive isn't running");
        }
        return true;
    }
}
