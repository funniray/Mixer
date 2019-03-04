package pro.kdray.funniray.mixer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextComponentString;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerForge;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;

public class ResetScene {

    public ResetScene(CommandDispatcher dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.literal(Commands.SWITCHSCENE.getName())
            .then(RequiredArgumentBuilder.argument("scene", string())
                .executes(c -> {
                    CommandSource commandSource = (CommandSource) c.getSource();
                    if (!commandSource.asPlayer().hasPermissionLevel(Commands.START.getPermission().getPermissionLevel()))
                        return 0;

                    if (MixerForge.isRunning()) {
                        MixerForge.getApi().getInteractive().resetScene(getString(c, Commands.SWITCHSCENE.getName()));
                    } else {
                        commandSource.asPlayer().sendMessage(new TextComponentString("&9&l[Mixer]&r&3 Interactive isn't running".replace("&", "§")));
                        return 0;
                    }
                    return 1;
                })));
    }
}
