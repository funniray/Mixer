package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import pro.kdray.funniray.mixer.MixerNukkit;
import pro.kdray.funniray.mixer.commands;

public class pause extends Command {
    public pause() {
        super(commands.PAUSE.getName(), commands.PAUSE.getDescription(), commands.PAUSE.getUsage());
        this.setPermission(commands.PAUSE.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerNukkit.getApi().getInteractive().pause();
        return true;
    }
}
