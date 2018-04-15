package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pro.kdray.funniray.mixer.MixerForge;
import pro.kdray.funniray.mixer.commands;

public class start extends CommandBase {
    @Override
    public String getName() {
        return commands.START.getName();
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return commands.START.getUsage();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] strings) throws CommandException {
        if (!sender.canUseCommand(commands.START.getPermission().getPermissionLevel(),null))
            return;
        MixerForge.startMain();
    }
}
