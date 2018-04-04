package pro.kdray.funniray.mixer.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerSponge;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        Sponge.getServer().getBroadcastChannel().send(Text.of(message)); //TODO: Format colors
        MixerSponge.getLogger().info(message);
    }
    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        Title titleO = Title.builder().title(Text.of(title)).subtitle(Text.of(subtitle)).fadeIn(fadein).stay(duration).fadeOut(fadeout).build();
        for (Player player : Sponge.getServer().getOnlinePlayers()){
            player.sendTitle(titleO);
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        Title titleO = Title.builder().title(Text.of(title)).subtitle(Text.of(subtitle)).build();
        for (Player player : Sponge.getServer().getOnlinePlayers()){
            player.sendTitle(titleO);
        }
    }

    @Override
    public void summon(String entity) {
        for(Player player:Sponge.getServer().getOnlinePlayers()){
            runCommand("execute "+player.getName()+" ~ ~ ~ summon "+entity);
        }
    }

    @Override
    public void runCommand(String command) {
        Sponge.getGame().getScheduler().createSyncExecutor(MixerSponge.class).execute(() -> Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),command));
    }

    @Override
    public void runAsync(Runnable runnable) {
        Sponge.getGame().getScheduler().createAsyncExecutor(MixerSponge.class).execute(runnable);
    }
}
