package pro.kdray.funniray.mixer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
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
import org.apache.logging.log4j.Logger;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

import static pro.kdray.funniray.mixer.MixerForge.MODID;

@Mod(modid = MODID, name = "Mixer Interactive Plugin", version = "1.0", acceptableRemoteVersions="*") //TODO:Make it so it's optional on clients
public final class MixerForge{

    public static final String MODID = "mixerinteractive";

    private static boolean running = false;

    private static Configuration configuration;

    private static Logger logger;

    private static String token;

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

        startMain();
    }

    @Mod.EventHandler
    public void onGameStop(FMLServerStoppingEvent event) {
        // Plugin shutdown logic
        main.shutdown();
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

        Property tokenProp = configuration.get(Configuration.CATEGORY_GENERAL, "token", "Api key here!");
        Property clientIDProp = configuration.get(Configuration.CATEGORY_GENERAL, "clientID", "fa54866255ea641235e596e5659fa726a4aa9f7ecc72758f");
        Property shareCodeProp = configuration.get(Configuration.CATEGORY_GENERAL,"shareCode","dbzktlsk");
        Property projectIDProp = configuration.get(Configuration.CATEGORY_GENERAL,"projectID",191773);

        token = tokenProp.getString();
        config.clientID = clientIDProp.getString();
        config.shareCode = shareCodeProp.getString();
        config.projectID = projectIDProp.getInt();

        tokenProp.set(token);
        clientIDProp.set(config.clientID);
        shareCodeProp.set(config.shareCode);
        projectIDProp.set(config.projectID);

        if (configuration.hasChanged()) {
            configuration.save();
            if (running){
                main.shutdown();
                startMain();
            }
        }
    }

    public static void startMain(){
        new Thread(() -> {
            try {
                main.initializeAPI(token,new mixer());//TODO: Make tokens per-player
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
}
