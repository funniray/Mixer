package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
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
        if (!MixerForge.isRunning()) {
            MixerForge.startMain();
        } else {
            sender.sendMessage(new TextComponentString("&9&l[Mixer]&r&3 Interactive is already running".replace('&', '§')));
        }
    }
}
