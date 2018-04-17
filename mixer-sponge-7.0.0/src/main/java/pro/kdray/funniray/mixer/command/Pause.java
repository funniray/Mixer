package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSponge;

public class Pause extends AbstractCommand {

    public Pause() {
        super(Commands.PAUSE);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        MixerSponge.getApi().getInteractive().pause();
        return CommandResult.success();
    }
}
