package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class Start extends Command {

    public Start() {
        super(Commands.START.getName(), Commands.START.getDescription(), Commands.START.getUsage());
        this.setPermission(Commands.START.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!MixerNukkit.isRunning()) {
            MixerNukkit.startMain();
        } else {
            commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer]" + TextFormat.RESET + TextFormat.RED + " Interactive is already running");
        }
        return false;
    }
}
