package pro.kdray.funniray.mixer.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.MixerSpigot;
import pro.kdray.funniray.mixer.commands;

import java.util.ArrayList;

public class stop extends BukkitCommand {

    public stop() {
        super(commands.STOP.getName(), commands.STOP.getDescription(), commands.STOP.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerSpigot.getApi().shutdown();
        return true;
    }
}
