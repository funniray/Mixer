package pro.kdray.funniray.mixer.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSpigot;

import java.util.ArrayList;

public class Stop extends BukkitCommand {

    public Stop() {
        super(Commands.STOP.getName(), Commands.STOP.getDescription(), Commands.STOP.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerSpigot.getApi().shutdown();
        return true;
    }
}
