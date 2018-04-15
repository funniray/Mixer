package pro.kdray.funniray.mixer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import pro.kdray.funniray.mixer.command.pause;
import pro.kdray.funniray.mixer.command.start;
import pro.kdray.funniray.mixer.command.stop;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static pro.kdray.funniray.mixer.MixerForge.MODID;

@Mod(modid = MODID, name = "Mixer Interactive Plugin", version = "1.0", acceptableRemoteVersions="*") //TODO:Make it so it's optional on clients
public final class MixerForge{

    public static final String MODID = "mixerinteractive";

    private static main api;

    private static boolean running = false;

    private static Configuration configuration;

    private static Logger logger;

    private static String token;
    private static boolean global;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        syncFromFile();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        // Plugin startup logic

        logger.info("[Mixer] Enabled plugin");

        event.registerServerCommand(new pause());
        event.registerServerCommand(new stop());
        event.registerServerCommand(new start());

        //startMain();
    }

    @Mod.EventHandler
    public void onGameStop(FMLServerStoppingEvent event) {
        // Plugin shutdown logic
        api.shutdown();
        running = false;
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
        Property subscriberCommandProp = configuration.get(Configuration.CATEGORY_GENERAL, "subscriberCommand","command");
        Property resubscriberCommandProp = configuration.get(Configuration.CATEGORY_GENERAL, "resubscriberCommand","command");

        Property bannedWordsProp = configuration.get(Configuration.CATEGORY_GENERAL,"bannedWords",new String[]{"put multiple","words to remove from chat here"});

        global = globalProp.getBoolean();
        token = tokenProp.getString();
        config.clientID = clientIDProp.getString();
        config.shareCode = shareCodeProp.getString();
        config.projectID = projectIDProp.getInt();

        config.FollowCommand = followCommandProp.getString();
        config.SubscriberCommand = subscriberCommandProp.getString();
        config.ResubscriberCommand = resubscriberCommandProp.getString();

        config.bannedWords = bannedWordsProp.getStringList();

        globalProp.set(global);
        tokenProp.set(token);
        clientIDProp.set(config.clientID);
        shareCodeProp.set(config.shareCode);
        projectIDProp.set(config.projectID);

        followCommandProp.set(config.FollowCommand);
        subscriberCommandProp.set(config.SubscriberCommand);
        resubscriberCommandProp.set(config.ResubscriberCommand);

        bannedWordsProp.set(config.bannedWords);

        if (configuration.hasChanged()) {
            configuration.save();
            if (running){
                api.shutdown();
                startMain();
            }
        }
    }

    public static void startMain(){
        new Thread(() -> {
            try {
                api = new main(token,new mixer());//TODO: Make tokens per-player
                running = true;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL) //TODO: Add GUI for config
    public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (MODID.equals(event.getModID())) {
            syncFromGUI();
        }
    }

    public static main getApi() {
        return api;
    }

    public static void setApi(main api) {
        MixerForge.api = api;
    }
}
