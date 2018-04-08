package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import pro.kdray.funniray.mixer.MixerSponge;
import pro.kdray.funniray.mixer.commands;

public class pause extends abstractCommand {

    public pause() {
        super(commands.PAUSE);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        MixerSponge.getApi().getInteractive().pause();
        return CommandResult.success();
    }
}
