package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class ResetScene extends Command {

    public ResetScene() {
        super(Commands.RESETSCENE.getName(), Commands.RESETSCENE.getDescription(), Commands.RESETSCENE.getUsage());
        this.setPermission(Commands.RESETSCENE.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer]" + TextFormat.RESET + TextFormat.RED + " You need to provide a valid scene");
            return true;
        }
        if (MixerNukkit.isRunning()) {
            MixerNukkit.getApi().getInteractive().resetScene(strings[0]);
        } else {
            commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer]" + TextFormat.RESET + TextFormat.RED + " Interactive isn't running");
        }
        return true;
    }
}
