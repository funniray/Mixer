package pro.kdray.funniray.mixer.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.title.Title;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerSponge;
import pro.kdray.funniray.mixer.Permissions;
import pro.kdray.funniray.mixer.SpongeUtils;

import java.util.concurrent.TimeUnit;

public class Mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        LiteralText formatted = SpongeUtils.formatText(message);
        for (Player player : Sponge.getServer().getOnlinePlayers()){
            if (!player.hasPermission(Permissions.RECEIVEMESSAGES.getNode()))
                continue;
            player.sendMessage(formatted);
        }
        this.debug(formatted.getContent());
    }
    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        Title titleO = Title.builder().title(SpongeUtils.formatText(title)).subtitle(SpongeUtils.formatText(subtitle)).fadeIn(fadein).stay(duration).fadeOut(fadeout).build();
        for (Player player : Sponge.getServer().getOnlinePlayers()){
            if (!player.hasPermission(Permissions.RECEIVEMESSAGES.getNode()))
                continue;
            player.sendTitle(titleO);
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        Title titleO = Title.builder().title(SpongeUtils.formatText(title)).subtitle(SpongeUtils.formatText(subtitle)).build();
        for (Player player : Sponge.getServer().getOnlinePlayers()){
            if (!player.hasPermission(Permissions.RECEIVEMESSAGES.getNode()))
                continue;
            player.sendTitle(titleO);
        }
    }

    @Override
    public void sendActionBar(String title) {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            if (!player.hasPermission(Permissions.RECEIVEMESSAGES.getNode()))
                continue;
            player.sendMessage(ChatTypes.ACTION_BAR, SpongeUtils.formatText(title));
        }
    }

    @Override
    public void summon(String entity) {
        runCommandAsConsole("execute %streamer% ~ ~ ~ summon "+entity);
    }

    @Override
    public void runCommand(String command) {
        Sponge.getGame().getScheduler().createSyncExecutor(MixerSponge.class).execute(() -> {
            for(Player player:Sponge.getServer().getOnlinePlayers()) {
                if (!player.hasPermission(Permissions.RUNCOMMANDS.getNode()))
                    continue;
                Sponge.getCommandManager().process(player, command.replace("%streamer%",player.getName()));
            }
        });
    }

    @Override
    public void runCommandAsConsole(String command){
        Sponge.getGame().getScheduler().createSyncExecutor(MixerSponge.class).execute(() -> {
            for(Player player:Sponge.getServer().getOnlinePlayers()) {
                if (!player.hasPermission(Permissions.RUNCOMMANDS.getNode()))
                    continue;
                Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(), command.replace("%streamer%",player.getName()));
            }
        });
    }

    @Override
    public void runAsync(Runnable runnable) {
        Sponge.getGame().getScheduler().createAsyncExecutor(MixerSponge.class).execute(runnable);
    }

    @Override
    public void runAsyncAfter(Runnable runnable, int after) {
        Sponge.getGame().getScheduler().createAsyncExecutor(MixerSponge.class).schedule(runnable,after,TimeUnit.MILLISECONDS).run();
    }

    @Override
    public void debug(String message) {
        MixerSponge.getLogger().info(message);
    }
}
