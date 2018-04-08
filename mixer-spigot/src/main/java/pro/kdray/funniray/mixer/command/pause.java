package pro.kdray.funniray.mixer.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pro.kdray.funniray.mixer.MixerSpigot;
import pro.kdray.funniray.mixer.commands;

import java.util.ArrayList;

public class pause extends BukkitCommand {

    public pause() {
        super(commands.PAUSE.getName(), commands.PAUSE.getDescription(), commands.PAUSE.getUsage(), new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerSpigot.getApi().getInteractive().pause();
        return true;
    }
}
