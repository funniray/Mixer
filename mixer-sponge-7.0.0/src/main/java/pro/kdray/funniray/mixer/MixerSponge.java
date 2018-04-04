package pro.kdray.funniray.mixer;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import pro.kdray.funniray.mixer.events.mixer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

@Plugin(id = "mixerinteractive", description = "A Mixer Interactive Plugin", name = "Mixer Interactive Plugin", version = "1.0")
public final class MixerSponge{

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Inject
    private Game game;

    private static Logger logger;

    @Inject
    private void setLogger(Logger logger1) {
        logger = logger1;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Plugin startup logic

        logger.info("[Mixer] Enabled plugin");

        ConfigurationLoader<CommentedConfigurationNode> loader = null;

        if (Files.notExists(defaultConfig)) {
            try {
                Sponge.getAssetManager().getAsset(this,"defaultConfig.conf").get().copyToFile(defaultConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Files.notExists(defaultConfig)){
            logger.info("Config doesn't exist after loading default config");
        }else{
            logger.info("Path is: "+defaultConfig.toFile().getAbsolutePath());
        }

        loader = HoconConfigurationLoader.builder().setPath(defaultConfig).build();

        ConfigurationNode config;
        String token = null;

        if (loader != null){
            try {
                config = loader.load();
                token = config.getNode("token").getString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (token == null){
            logger.info("Config won't load :/");
            token = "NoToken";
        }

        String realToken = token;

        game.getScheduler().createAsyncExecutor(this).execute(() -> {
            try {
                main.initializeAPI(realToken,new mixer());//TODO:Make tokens per-player
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Listener
    public void onGameStop(GameStoppingServerEvent event) {
        // Plugin shutdown logic
        main.shutdown();
    }

    public static Logger getLogger() {
        return logger;
    }
}
