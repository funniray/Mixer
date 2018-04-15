package pro.kdray.funniray.mixer.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import pro.kdray.funniray.mixer.MixerNukkit;
import pro.kdray.funniray.mixer.commands;

public class start extends Command {

    public start(){
        super(commands.START.getName(), commands.START.getDescription(), commands.START.getUsage());
        this.setPermission(commands.START.getPermission().getNode());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        MixerNukkit.startMain();
        return false;
    }
}
