package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pro.kdray.funniray.mixer.MixerForge;
import pro.kdray.funniray.mixer.commands;

public class stop extends CommandBase {
    @Override
    public String getName() {
        return commands.STOP.getName();
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return commands.STOP.getUsage();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] strings) throws CommandException {
        if (!sender.canUseCommand(commands.STOP.getPermission().getPermissionLevel(),null))
            return;
        MixerForge.getApi().shutdown();
    }
}
