package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class Stop extends Command {

    public Stop() {
        super(Commands.STOP.getName(), Commands.STOP.getDescription(), Commands.STOP.getUsage());
        this.setPermission(Commands.STOP.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (MixerNukkit.isRunning()) {
            MixerNukkit.stopMain();
        } else {
            commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer]" + TextFormat.RESET + TextFormat.RED + " Interactive isn't running");
        }
        return true;
    }
}
