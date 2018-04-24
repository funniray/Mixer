package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import pro.kdray.funniray.mixer.Commands;
import pro.kdray.funniray.mixer.SpongeUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCommand implements CommandCallable {

    private Commands command;

    public AbstractCommand(Commands command) {
        this.command = command;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission(command.getPermission().getNode());
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(SpongeUtils.formatText(command.getDescription()));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(SpongeUtils.formatText(command.getDescription()));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return SpongeUtils.formatText(command.getUsage());
    }
}

