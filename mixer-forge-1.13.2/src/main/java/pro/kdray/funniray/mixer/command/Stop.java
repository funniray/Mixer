package pro.kdray.funniray.mixer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextComponentString;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerForge;

public class Stop {

    public Stop(CommandDispatcher dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.literal(Commands.STOP.getName())
                .executes(c -> {
                    CommandSource commandSource = (CommandSource) c.getSource();
                    if (!commandSource.asPlayer().hasPermissionLevel(Commands.START.getPermission().getPermissionLevel()))
                        return 0;
                    if (MixerForge.isRunning()) {
                        MixerForge.stopMain();
                    } else {
                        commandSource.asPlayer().sendMessage(new TextComponentString("&9&l[Mixer]&r&3 Interactive isn't running".replace("&", "§")));
                        return 0;
                    }
                    return 1;
                }));
    }

}
