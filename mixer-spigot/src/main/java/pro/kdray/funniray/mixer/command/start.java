package pro.kdray.funniray.mixer.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.MixerSpigot;
import pro.kdray.funniray.mixer.commands;

import java.util.ArrayList;

public class start extends BukkitCommand {

    public start() {
        super(commands.START.getName(), commands.START.getDescription(), commands.START.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerSpigot.startMain();
        return true;
    }
}
