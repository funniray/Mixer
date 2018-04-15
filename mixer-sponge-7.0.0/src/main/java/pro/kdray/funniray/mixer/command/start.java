package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import pro.kdray.funniray.mixer.MixerSponge;
import pro.kdray.funniray.mixer.commands;

public class start extends abstractCommand {
    public start() {
        super(commands.START);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        MixerSponge.startMain();
        return null;
    }
}
