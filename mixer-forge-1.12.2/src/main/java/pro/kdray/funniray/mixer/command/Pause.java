package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.ForgeUtils;
import pro.kdray.funniray.mixer.MixerForge;

import java.util.Collections;
import java.util.List;

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
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!ForgeUtils.hasPermission(sender, Commands.PAUSE.getPermission().getNode()))
            return;
        if (MixerForge.isRunning()) {
            MixerForge.getApi().getInteractive().pause();
        } else {
            sender.sendMessage(new TextComponentString("&9&l[Mixer]&r&c Interactive isn't running".replace("&", "§")));
        }
    }
}
