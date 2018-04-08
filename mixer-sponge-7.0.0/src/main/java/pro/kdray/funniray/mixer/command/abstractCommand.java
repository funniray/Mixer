package pro.kdray.funniray.mixer.command;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import pro.kdray.funniray.mixer.commands;
import pro.kdray.funniray.mixer.utils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class abstractCommand implements CommandCallable {

    private commands command;

    public abstractCommand(commands command){
        this.command = command;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission(command.getPermission().getNode());
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(utils.formatText(command.getDescription()));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(utils.formatText(command.getDescription()));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return utils.formatText(command.getUsage());
    }
}

