package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSponge;

public class Start extends AbstractCommand {
    public Start() {
        super(Commands.START);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        MixerSponge.startMain();
        return null;
    }
}
