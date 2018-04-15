package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import pro.kdray.funniray.mixer.MixerSponge;
import pro.kdray.funniray.mixer.commands;

public class stop extends abstractCommand {
    public stop() {
        super(commands.STOP);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        MixerSponge.getApi().shutdown();
        return null;
    }
}
