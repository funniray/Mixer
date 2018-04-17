package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class Start extends Command {

    public Start() {
        super(Commands.START.getName(), Commands.START.getDescription(), Commands.START.getUsage());
        this.setPermission(Commands.START.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerNukkit.startMain();
        return false;
    }
}
