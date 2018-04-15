package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import pro.kdray.funniray.mixer.MixerNukkit;
import pro.kdray.funniray.mixer.commands;

public class stop extends Command {

    public stop(){
        super(commands.STOP.getName(), commands.STOP.getDescription(), commands.STOP.getUsage());
        this.setPermission(commands.STOP.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerNukkit.getApi().shutdown();
        return true;
    }
}
