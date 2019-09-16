package pro.kdray.funniray.mixer.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

import static pro.kdray.funniray.mixer.MixerSpigot.plugin;

public class ResetScene extends BukkitCommand {

    public ResetScene() {
        super(Commands.RESETSCENE.getName(), Commands.RESETSCENE.getDescription(), Commands.RESETSCENE.getUsage(), new ArrayList<>());
    }

    FileConfiguration cfg = plugin.getConfig();
    String prefix = cfg.getString("messages.prefix");

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.hasPermission(Commands.RESETSCENE.getPermission().getNode())) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.no-permission")));
            return true;
        }
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.invalid-scene")));
        }
        if (MixerSpigot.isRunning()) {
            MixerSpigot.getApi().getInteractive().resetScene(strings[0]);
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("messages.not-running")));
        }
        return true;
    }
}
