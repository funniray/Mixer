package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerForge;

public class Start extends CommandBase {
    @Override
    public String getName() {
        return Commands.START.getName();
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return Commands.START.getUsage();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] strings) {
        if (!sender.canUseCommand(Commands.START.getPermission().getPermissionLevel(), null))
            return;
        MixerForge.startMain();
    }
}
