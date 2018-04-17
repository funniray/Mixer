package pro.kdray.funniray.mixer.command;

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
        MixerSpigot.startMain();
        return true;
    }
}
