package pro.kdray.funniray.mixer;

import com.mixer.api.http.HttpBadResponseException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pro.kdray.funniray.mixer.command.*;
import pro.kdray.funniray.mixer.compadibility.NullHandler;
import pro.kdray.funniray.mixer.compadibility.VersionHandler;
import pro.kdray.funniray.mixer.compadibility.v1_10.Handler_1_10_R1;
import pro.kdray.funniray.mixer.compadibility.v1_11.Handler_1_11_R1;
import pro.kdray.funniray.mixer.compadibility.v1_12.Handler_1_12_R1;
import pro.kdray.funniray.mixer.compadibility.v1_13.Handler_1_13_R1;
import pro.kdray.funniray.mixer.compadibility.v1_13.Handler_1_13_R2;
import pro.kdray.funniray.mixer.compadibility.v1_14.Handler_1_14_R1;
import pro.kdray.funniray.mixer.compadibility.v1_9.Handler_1_9_R1;
import pro.kdray.funniray.mixer.compadibility.v1_9.Handler_1_9_R2;
import pro.kdray.funniray.mixer.events.Mixer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

public final class MixerSpigot extends JavaPlugin {

    public static Plugin plugin;
    public static Main api = null;
    private static boolean isRunning = false;

    public static VersionHandler versionHandler;

    public static void startMain(){
        //Run Main class
        MixerSpigot.plugin.reloadConfig();
        Bukkit.getScheduler().runTaskAsynchronously(MixerSpigot.plugin, () -> {
            try {
                api.startAll();
                isRunning = true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                if (e.getCause() instanceof HttpBadResponseException) {
                    HttpBadResponseException er = (HttpBadResponseException) e.getCause();
                    new Mixer().sendMessage("&4&l[Mixer] &r&cFailed to start interactive, Response: "+er.response.body());
                } else {
                    new Mixer().sendMessage("&4&l[Mixer] &r&cFailed to start interactive, maybe an invalid API key.");
                }
            }
        });
    }

    public static void stopMain() {
        if (api != null) {
            isRunning = false;
            api.shutdown();
        }
    }

    public static Main getApi() {
        return api;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    private void getVersion(){
        String version = Bukkit.getServer().getClass().getPackage().getName();

        version = version.substring(version.lastIndexOf('.') + 1);

        switch(version){
            case "v1_14_R1":
                versionHandler = new Handler_1_14_R1();
                break;
            case "v1_13_R2":
                versionHandler = new Handler_1_13_R2();
                break;
            case "v1_13_R1":
                versionHandler = new Handler_1_13_R1();
                break;
            case "v1_12_R1":
                versionHandler = new Handler_1_12_R1();
                break;
            case "v1_11_R1":
                versionHandler = new Handler_1_11_R1();
                break;
            case "v1_10_R1":
                versionHandler = new Handler_1_10_R1();
                break;
            case "v1_9_R2":
                versionHandler = new Handler_1_9_R2();
                break;
            case "v1_9_R1":
                versionHandler = new Handler_1_9_R1();
                break;
            default:
                this.getLogger().warning("This version is unsupported, Titles and ActionBars won't work. Current Version\":"+version);
                versionHandler = new NullHandler();
        }
    }

    public static VersionHandler getVersionHandler() {
        return versionHandler;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (isRunning)
            stopMain();
    }

    public static void setApi(Main api) {
        MixerSpigot.api = api;
    }

    public static void reload() {
        boolean wasRunning = false;
        if (MixerSpigot.isRunning) {
            stopMain();
            wasRunning = true;
        }
        MixerSpigot.loadConfig();
        if (wasRunning)
            startMain();
    }

    public static void setConfig(String path, String value) {
        MixerSpigot.plugin.getConfig().set(path, value);
        MixerSpigot.plugin.saveConfig();
    }

    public static void loadConfig() {
        MixerSpigot.plugin.reloadConfig();

        FileConfiguration configuration = MixerSpigot.plugin.getConfig();

        Config.shareCode = configuration.getString("shareCode");
        Config.clientID = configuration.getString("clientID");
        Config.projectID = configuration.getInt("projectID");

        Config.followCommand = configuration.getString("followCommand");
        Config.subscriberCommand = configuration.getString("subscriberCommand");
        Config.resubscriberCommand = configuration.getString("resubscriberCommand");

        Config.bannedWords = configuration.getStringList("bannedWords");

        if (!isRunning) {
            String token = configuration.getString("token");
            api = new Main(token, new Mixer());
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        this.getVersion();

        this.saveDefaultConfig();
        this.reloadConfig();
        loadConfig();


        //registering permissions
        PluginManager pm = this.getServer().getPluginManager();
        Permission basePerm = new Permission(Config.permPrefix + ".*");
        pm.addPermission(basePerm);
        for (Permissions permission : Permissions.values()) {
            Permission perm = new Permission(permission.getNode(), permission.getDescription());
            perm.setDefault(PermissionDefault.valueOf(permission.getDefaultMode()));
            perm.addParent(basePerm, true);
            pm.addPermission(perm);
        }

        //Credit to mine-care ( https://bukkit.org/members/mine-care.90737861/ )
        try {
            Method commandMap = plugin.getServer().getClass().getMethod("getCommandMap", null);
            Object cmdmap = commandMap.invoke(plugin.getServer(), null);
            Method register = cmdmap.getClass().getMethod("register", String.class, Command.class);
            register.invoke(cmdmap, Commands.PAUSE.getName(), new Pause());
            register.invoke(cmdmap, Commands.STOP.getName(), new Stop());
            register.invoke(cmdmap, Commands.START.getName(), new Start());
            register.invoke(cmdmap, Commands.SWITCHSCENE.getName(), new SwitchScene());
            register.invoke(cmdmap, Commands.RESETSCENE.getName(), new ResetScene());
            register.invoke(cmdmap, Commands.MAIN.getName(), new MainCommand());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        String token = this.getConfig().getString("token");
        api = new Main(token, new Mixer());
    }
}
