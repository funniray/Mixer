package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSponge;

public class Stop extends AbstractCommand {
    public Stop() {
        super(Commands.STOP);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        MixerSponge.getApi().shutdown();
        return null;
    }
}
