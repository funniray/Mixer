package pro.kdray.funniray.mixer.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.ForgeUtils;
import pro.kdray.funniray.mixer.MixerForge;

import java.util.concurrent.ExecutionException;

public class MainCommand extends CommandBase {

    @Override
    public String getName() {
        return Commands.MAIN.getName();
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return Commands.MAIN.getUsage();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (!ForgeUtils.hasPermission(iCommandSender, Commands.MAIN.getPermission().getNode())) {
            iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&c You don't have permission to run this command".replace("&", "§")));
            return;
        }
        switch (strings.length) {
            case 0:
                iCommandSender.sendMessage(new TextComponentString(this.getUsage(iCommandSender)));
                return;
            case 1:
                switch (strings[0]) {
                    case "reload":
                        MixerForge.reload();
                        iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&9 Reloaded the config".replace("&", "§")));
                        return;
                    default:
                        iCommandSender.sendMessage(new TextComponentString(this.getUsage(iCommandSender)));
                        return;
                }
            case 2:
                switch (strings[0]) {
                    case "start":
                        switch (strings[1]) {
                            case "chat":
                                try {
                                    MixerForge.getApi().startChat();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                    iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&9 Failed to start chat".replace("&", "§")));
                                }
                                return;
                            case "interactive":
                                try {
                                    MixerForge.getApi().startInteractive();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                    iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&9 Failed to start interactive".replace("&", "§")));
                                }
                                return;
                            case "all":
                                if (!MixerForge.isRunning()) {
                                    MixerForge.startMain();
                                } else {
                                    iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&9 Interactive is already running".replace("&", "§")));
                                }
                            default:
                                iCommandSender.sendMessage(new TextComponentString(this.getUsage(iCommandSender)));
                                return;
                        }
                    case "stop":
                        switch (strings[1]) {
                            case "chat":
                                MixerForge.getApi().stopChat();
                                iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&9 Stopped chat".replace("&", "§")));
                                return;
                            case "interactive":
                                MixerForge.getApi().stopInteractive();
                                iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&9 Stopped interactive".replace("&", "§")));
                                return;
                            case "all":
                                if (!MixerForge.isRunning()) {
                                    MixerForge.stopMain();
                                    iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&9 Stopped everything".replace("&", "§")));
                                } else {
                                    iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&3 Interactive isn't running".replace("&", "§")));
                                }
                            default:
                                iCommandSender.sendMessage(new TextComponentString(this.getUsage(iCommandSender)));
                                return;
                        }
                    case "token":
                        iCommandSender.sendMessage(new TextComponentString("&9&l[Mixer]&r&c Not there yet".replace("&", "§"))); //TODO
                        return;
                    default:
                        iCommandSender.sendMessage(new TextComponentString(this.getUsage(iCommandSender)));
                        return;
                }
                //    if (strings[0].equals("token")) {
                //        MixerSpigot.setConfig("token", strings[1]);
                //        MixerSpigot.reload();
                //        commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Mixer] " + ChatColor.RESET + ChatColor.BLUE + "Set token!");
                //        return;
                //    } else {
                //        return false;
                //    }
            default:
                iCommandSender.sendMessage(new TextComponentString(this.getUsage(iCommandSender)));
                return;
        }
    }
}
