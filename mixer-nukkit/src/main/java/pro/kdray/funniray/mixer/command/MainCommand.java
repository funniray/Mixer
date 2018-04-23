package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerNukkit;

public class MainCommand extends Command {
    public MainCommand() {
        super(Commands.MAIN.getName(), Commands.MAIN.getDescription(), Commands.MAIN.getUsage());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        switch (strings.length) {
            case 0:
                return false;
            case 1:
                if (strings[0].equals("reload")) {
                    MixerNukkit.reload();
                    commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer] " + TextFormat.RESET + TextFormat.BLUE + "Reloaded the config");
                    return true;
                } else {
                    return false;
                }
            case 2:
                if (strings[0].equals("token")) {
                    MixerNukkit.setConfig("token", strings[1]);
                    MixerNukkit.reload();
                    commandSender.sendMessage(TextFormat.BLUE + "" + TextFormat.BOLD + "[Mixer] " + TextFormat.RESET + TextFormat.BLUE + "Set the token");
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }
}
