package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerForge;

public class Pause extends CommandBase {
    @Override
    public String getName() {
        return Commands.PAUSE.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return Commands.PAUSE.getUsage();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!sender.canUseCommand(Commands.PAUSE.getPermission().getPermissionLevel(), null))
            return;
        MixerForge.getApi().getInteractive().pause(); //TODO: Check if running
    }
}
