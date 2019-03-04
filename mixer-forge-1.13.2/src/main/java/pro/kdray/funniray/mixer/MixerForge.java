package pro.kdray.funniray.mixer;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.kdray.funniray.mixer.command.*;
import pro.kdray.funniray.mixer.events.Mixer;

import java.util.concurrent.ExecutionException;

import static pro.kdray.funniray.mixer.MixerForge.MODID;

@Mod(MODID)
public final class MixerForge{

    public static final String MODID = "mixerinteractive";

    private static Main api = null;

    public static boolean chatRunning = false;
    public static boolean interactiveRunning = false;

    public static CommentedConfig configuration;

    private static Logger logger = LogManager.getLogger();

    private static boolean global;

    public MixerForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.register(MixerConfig.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MixerConfig.CONFIG_SPEC);
    }

    public static void onFileChange() {
        MixerConfig.load();
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

    public static void startMain(){
        new Thread(() -> {
            try {
                api.startAll();
                chatRunning = true;
                interactiveRunning = true;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                api.getInteractive().getEventHandler().sendMessage("&4&l[Mixer] &r&cFailed to start interactive, could be due to invalid API key");
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

    public static Main getApi() {
        return api;
    }

    public static void setApi(Main api) {
        MixerForge.api = api;
    }

    public static boolean isRunning() {
        return chatRunning || interactiveRunning;
    }

    @SubscribeEvent
    public void init(FMLCommonSetupEvent event) {
        for (Permissions permission : Permissions.values()) {
            if (permission.getNode() == null || permission.getDescription() == null)
                continue;
            PermissionAPI.registerNode(permission.getNode(),
                    DefaultPermissionLevel.valueOf(permission.getDefaultMode()),
                    permission.getDescription());
        }
    }

    public static void reload() {
        boolean wasRunning = false;
        if (MixerForge.isRunning()) {
            stopMain();
            wasRunning = true;
        }
        if (wasRunning)
            startMain();
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        // Plugin startup logic

        logger.info("[Mixer] Enabled plugin");

        CommandDispatcher dispatcher = event.getServer().getCommandManager().getDispatcher();

        new Pause(dispatcher);
        new ResetScene(dispatcher);
        new Start(dispatcher);
        new Stop(dispatcher);
        new SwitchScene(dispatcher);

        api = new Main(MixerConfig.token, new Mixer(event.getServer()));
        //startMain();
    }

    @SubscribeEvent
    public void onGameStop(FMLServerStoppingEvent event) {
        // Plugin shutdown logic
        if (isRunning())
            stopMain();
    }
}
