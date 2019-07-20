package pro.kdray.funniray.mixer.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

import static pro.kdray.funniray.mixer.MixerSpigot.plugin;

public class MainCommand extends BukkitCommand {

    public MainCommand() {
        super(Commands.MAIN.getName(), Commands.MAIN.getDescription(), Commands.MAIN.getUsage(), new ArrayList<>());
    }

    FileConfiguration cfg = plugin.getConfig();
    String prefix = cfg.getString("messages.prefix");

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.hasPermission(Commands.MAIN.getPermission().getNode())) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.no-permission")));
            return true;
        }
        switch (strings.length) {
            case 0:
                return false;
            case 1:
                if (strings[0].equals("reload")) {
                    MixerSpigot.reload();
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.reload-config")));
                    return true;
                } else {
                    return false;
                }
            case 2:
                if (strings[0].equals("token")) {
                    MixerSpigot.setConfig("token", strings[1]);
                    MixerSpigot.reload();
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.set-token")));
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }
}
