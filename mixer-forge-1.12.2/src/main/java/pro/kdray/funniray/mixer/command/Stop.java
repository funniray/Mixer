package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.ForgeUtils;
import pro.kdray.funniray.mixer.MixerForge;

public class Stop extends CommandBase {
    @Override
    public String getName() {
        return Commands.STOP.getName();
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return Commands.STOP.getUsage();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] strings) {
        if (!ForgeUtils.hasPermission(sender, Commands.STOP.getPermission().getNode()))
            return;
        if (MixerForge.isRunning()) {
            MixerForge.stopMain();
        } else {
            sender.sendMessage(new TextComponentString("&9&l[Mixer]&r&3 Interactive isn't running".replace('&', '§')));
        }
    }
}
