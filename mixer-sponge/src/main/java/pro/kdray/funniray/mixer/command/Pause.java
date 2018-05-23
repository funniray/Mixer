package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSponge;

public class Pause extends AbstractCommand {

    public Pause() {
        super(Commands.PAUSE);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        if (MixerSponge.isRunning()) {
            MixerSponge.getApi().getInteractive().pause();
        } else {
            source.sendMessage(Text.builder()
                    .append(Text.builder("[Mixer]")
                            .color(TextColors.BLUE)
                            .format(TextFormat.of(TextStyles.BOLD))
                            .build())
                    .append(Text.builder(" Interactive isn't running")
                            .color(TextColors.RED)
                            .build()).build());
        }
        return CommandResult.success();
    }
}
