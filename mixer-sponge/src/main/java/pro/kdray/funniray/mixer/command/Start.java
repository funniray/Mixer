package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.MixerSponge;

public class Start extends AbstractCommand {
    public Start() {
        super(Commands.START);
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        if (!MixerSponge.isRunning()) {
            MixerSponge.startMain();
        } else {
            source.sendMessage(Text.builder()
                    .append(Text.builder("[Mixer]")
                            .color(TextColors.BLUE)
                            .format(TextFormat.of(TextStyles.BOLD))
                            .build())
                    .append(Text.builder(" Interactive is already running")
                            .color(TextColors.RED)
                            .build()).build());
        }
        return null;
    }
}
