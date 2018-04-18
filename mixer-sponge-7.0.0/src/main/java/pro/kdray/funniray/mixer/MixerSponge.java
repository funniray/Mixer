package pro.kdray.funniray.mixer;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;
import pro.kdray.funniray.mixer.command.Pause;
import pro.kdray.funniray.mixer.events.Mixer;

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
    private static Game game;

    private static Logger logger;

    @Inject
    private void setLogger(Logger logger1) {
        logger = logger1;
    }

    private static Main api = null;
    private static String realToken;
    private static boolean isRunning = false;

    public static boolean isRunning() {
        return isRunning;
    }

    public static Main getApi() {
        return api;
    }

    public static void setApi(Main api) {
        MixerSponge.api = api;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void startMain() {
        isRunning = true;
        MixerSponge.game.getScheduler().createAsyncExecutor(MixerSponge.class).execute(() -> {
            try {
                api = new Main(realToken, new Mixer());//TODO:Make tokens per-player
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void stopMain() {
        if (api != null) {
            api.shutdown();
            isRunning = false;
        }
    }

    @Listener
    public void onGameStop(GameStoppingServerEvent event) {
        // Plugin shutdown logic
        if (isRunning)
            stopMain();
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
            logger.info("Config doesn't exist after loading default Config");
        }else{
            logger.info("Path is: "+defaultConfig.toFile().getAbsolutePath());
        }

        loader = HoconConfigurationLoader.builder().setPath(defaultConfig).build();

        ConfigurationNode localConfig;
        String token = null;

        if (loader != null){
            try {
                localConfig = loader.load();
                token = localConfig.getNode("token").getString();
                Config.projectID = localConfig.getNode("projectID").getInt();
                Config.clientID = localConfig.getNode("shareCode").getString();
                Config.shareCode = localConfig.getNode("clientID").getString();

                Config.followCommand = localConfig.getNode("followCommand").getString();
                Config.subscriberCommand = localConfig.getNode("subscriberCommand").getString();
                Config.resubscriberCommand = localConfig.getNode("resubscriberCommand").getString();

                try {
                    Config.bannedWords = localConfig.getNode("bannedWords").getList(TypeToken.of(String.class));
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (token == null){
            logger.info("Config won't load :/");
            token = "NoToken";
        }

        realToken = token;

        //registering permissions
        PermissionService ps = game.getServiceManager().getRegistration(PermissionService.class).get().getProvider();
        PermissionDescription.Builder builder = ps.newDescriptionBuilder(this);
        for (Permissions permission:Permissions.values()){
            builder.id(permission.getNode())
                .description(Text.of(permission.getDescription()))
                .assign(PermissionDescription.ROLE_STAFF,!permission.getDefaultMode().equals("false"))
                .assign(PermissionDescription.ROLE_USER,(permission.getDefaultMode().equals("true")))
                .register();
        }

        game.getCommandManager().register(this, new Pause());
    }
}
