package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class SwitchScene extends Command {

    public SwitchScene() {
        super(Commands.SWITCHSCENE.getName(), Commands.SWITCHSCENE.getDescription(), Commands.SWITCHSCENE.getUsage());
        this.setPermission(Commands.SWITCHSCENE.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer]" + TextFormat.RESET + TextFormat.RED + " You need to provide a valid scene");
            return true;
        }
        if (MixerNukkit.isRunning()) {
            MixerNukkit.getApi().getInteractive().switchAllScenes(strings[0]);
        } else {
            commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer]" + TextFormat.RESET + TextFormat.RED + " Interactive isn't running");
        }
        return true;
    }
}
