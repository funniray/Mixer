package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class Stop extends Command {

    public Stop() {
        super(Commands.STOP.getName(), Commands.STOP.getDescription(), Commands.STOP.getUsage());
        this.setPermission(Commands.STOP.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerNukkit.getApi().shutdown();
        return true;
    }
}
