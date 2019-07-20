package pro.kdray.funniray.mixer.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

import static pro.kdray.funniray.mixer.MixerSpigot.plugin;

public class Start extends BukkitCommand {

    public Start() {
        super(Commands.START.getName(), Commands.START.getDescription(), Commands.START.getUsage(), new ArrayList<>());
    }

    FileConfiguration cfg = plugin.getConfig();
    String prefix = cfg.getString("messages.prefix");

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.hasPermission(Commands.START.getPermission().getNode())) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.no-permission")));
            return true;
        }
        if (!MixerSpigot.isRunning()) {
            MixerSpigot.startMain();
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.already-running")));
        }
        return true;
    }
}
