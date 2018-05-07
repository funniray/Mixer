package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.ForgeUtils;
import pro.kdray.funniray.mixer.MixerForge;

public class ResetScene extends CommandBase {
    @Override
    public String getName() {
        return Commands.RESETSCENE.getName();
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return Commands.RESETSCENE.getUsage();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] strings) {
        if (!ForgeUtils.hasPermission(sender, Commands.RESETSCENE.getPermission().getNode()))
            return;
        if (strings.length <= 0) {
            sender.sendMessage(new TextComponentString("&9&l[Mixer]&r&3 You must put in a valid scene".replace('&', '§')));
            return;
        }

        if (MixerForge.isRunning()) {
            MixerForge.getApi().getInteractive().resetScene(strings[0]);
        } else {
            sender.sendMessage(new TextComponentString("&9&l[Mixer]&r&3 Interactive isn't running".replace('&', '§')));
        }
    }
}
