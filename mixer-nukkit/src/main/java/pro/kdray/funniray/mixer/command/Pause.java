package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class Pause extends Command {
    public Pause() {
        super(Commands.PAUSE.getName(), Commands.PAUSE.getDescription(), Commands.PAUSE.getUsage());
        this.setPermission(Commands.PAUSE.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (MixerNukkit.isRunning()) {
            MixerNukkit.getApi().getInteractive().pause();
        } else {
            commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer]" + TextFormat.RESET + TextFormat.RED + " Interactive isn't running");
        }
        return true;
    }
}
