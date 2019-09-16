package pro.kdray.funniray.mixer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.Logger;
import pro.kdray.funniray.mixer.command.*;
import pro.kdray.funniray.mixer.events.Mixer;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static pro.kdray.funniray.mixer.MixerForge.MODID;

@Mod(modid = MODID, name = "Mixer Interactive Plugin", version = "1.0", acceptableRemoteVersions = "*")
public final class MixerForge{

    public static final String MODID = "mixerinteractive";

    private static Main api = null;

    public static boolean chatRunning = false;
    public static boolean interactiveRunning = false;

    private static Configuration configuration;

    private static Logger logger;

    private static String token;
    private static boolean global;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        syncFromFile();
    }

    private static void syncConfig(boolean loadConfigFromFile) {
        if (loadConfigFromFile) {
            configuration.load();
        }

        Property globalProp = configuration.get(Configuration.CATEGORY_GENERAL,"global",false);
        Property tokenProp = configuration.get(Configuration.CATEGORY_GENERAL, "token", "Api key here!");
        Property clientIDProp = configuration.get(Configuration.CATEGORY_GENERAL, "clientID", "d04e85fd1cb06e4eb9891fc118fe75893eca399955189926");
        Property shareCodeProp = configuration.get(Configuration.CATEGORY_GENERAL,"shareCode","dbzktlsk");
        Property projectIDProp = configuration.get(Configuration.CATEGORY_GENERAL,"projectID",191773);

        //Put SQL data here

        Property followCommandProp = configuration.get(Configuration.CATEGORY_GENERAL, "followCommand","command");
        Property subscriberCommandProp = configuration.get(Configuration.CATEGORY_GENERAL, "subscribeCommand","command");
        Property resubscriberCommandProp = configuration.get(Configuration.CATEGORY_GENERAL, "resubscriberCommand","command");

        Property bannedWordsProp = configuration.get(Configuration.CATEGORY_GENERAL,"bannedWords",new String[]{"put multiple","words to remove from chat here"});

        global = globalProp.getBoolean();
        token = tokenProp.getString();
        Config.clientID = clientIDProp.getString();
        Config.shareCode = shareCodeProp.getString();
        Config.projectID = projectIDProp.getInt();

        Config.followCommand = followCommandProp.getString();
        Config.subscribeCommand = subscriberCommandProp.getString();
        Config.resubscribeCommand = resubscriberCommandProp.getString();

        Config.bannedWords = Arrays.asList(bannedWordsProp.getStringList());

        globalProp.set(global);
        tokenProp.set(token);
        clientIDProp.set(Config.clientID);
        shareCodeProp.set(Config.shareCode);
        projectIDProp.set(Config.projectID);

        followCommandProp.set(Config.followCommand);
        subscriberCommandProp.set(Config.subscribeCommand);
        resubscriberCommandProp.set(Config.resubscribeCommand);

        bannedWordsProp.set(bannedWordsProp.getStringList());

        if (configuration.hasChanged()) {
            configuration.save();
            if (chatRunning) {
                api.stopChat();
                try {
                    api.startChat();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (interactiveRunning) {
                api.stopInteractive();
                try {
                    api.startInteractive();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void startMain(){
        new Thread(() -> {
            try {
                api.startAll();
                chatRunning = true;
                interactiveRunning = true;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                new Mixer().sendMessage("&4&l[Mixer] &r&cFailed to start interactive, could be due to invalid API key");
            }
        }).start();
    }

    public static void stopMain() {
        if (api != null) {
            chatRunning = false;
            interactiveRunning = false;
            api.shutdown();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void syncFromFile() {
        syncConfig(true);
    }

    public static void syncFromGUI() {
        syncConfig(false);
    }

    public static Main getApi() {
        return api;
    }

    public static void setApi(Main api) {
        MixerForge.api = api;
    }

    public static boolean isRunning() {
        return chatRunning || interactiveRunning;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigManager.sync(MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);

        for (Permissions permission : Permissions.values()) {
            if (permission.getNode() == null || permission.getDescription() == null)
                continue;
            PermissionAPI.registerNode(permission.getNode(),
                    DefaultPermissionLevel.valueOf(permission.getDefaultMode()),
                    permission.getDescription());
        }
    }

    public static void reload() {
        syncFromFile();
        boolean wasRunning = false;
        if (MixerForge.isRunning()) {
            stopMain();
            wasRunning = true;
        }
        if (wasRunning)
            startMain();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        // Plugin startup logic

        logger.info("[Mixer] Enabled plugin");

        event.registerServerCommand(new Pause());
        event.registerServerCommand(new Stop());
        event.registerServerCommand(new Start());
        event.registerServerCommand(new SwitchScene());
        event.registerServerCommand(new ResetScene());
        event.registerServerCommand(new MainCommand());

        api = new Main(token, new Mixer());
        //startMain();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL) //TODO: Add GUI for Config
    public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (MODID.equals(event.getModID())) {
            syncFromGUI();
        }
    }

    @Mod.EventHandler
    public void onGameStop(FMLServerStoppingEvent event) {
        // Plugin shutdown logic
        if (isRunning())
            stopMain();
    }
}
