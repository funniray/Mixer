package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pro.kdray.funniray.mixer.MixerForge;
import pro.kdray.funniray.mixer.commands;

public class pause extends CommandBase {
    @Override
    public String getName() {
        return commands.PAUSE.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return commands.PAUSE.getUsage();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!sender.canUseCommand(commands.PAUSE.getPermission().getPermissionLevel(),null))
            return;
        MixerForge.getApi().getInteractive().pause(); //TODO: Check if running
    }
}
