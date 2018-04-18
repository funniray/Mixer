package pro.kdray.funniray.mixer.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

public class Pause extends BukkitCommand {

    public Pause() {
        super(Commands.PAUSE.getName(), Commands.PAUSE.getDescription(), Commands.PAUSE.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (MixerSpigot.isRunning()) {
            MixerSpigot.getApi().getInteractive().pause();
        } else {
            commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer]" + ChatColor.RESET + ChatColor.RED + " Interactive isn't running");
        }
        return true;
    }
}
