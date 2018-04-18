package pro.kdray.funniray.mixer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pro.kdray.funniray.mixer.command.Pause;
import pro.kdray.funniray.mixer.command.Start;
import pro.kdray.funniray.mixer.command.Stop;
import pro.kdray.funniray.mixer.compadibility.VersionHandler;
import pro.kdray.funniray.mixer.compadibility.v1_10.Handler_1_10_R1;
import pro.kdray.funniray.mixer.compadibility.v1_11.Handler_1_11_R1;
import pro.kdray.funniray.mixer.compadibility.v1_12.Handler_1_12_R1;
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
        isRunning = true;
        //Run Main class
        String token = MixerSpigot.plugin.getConfig().getString("token");
        Bukkit.getScheduler().runTaskAsynchronously(MixerSpigot.plugin, () -> {
            try {
                api = new Main(token, new Mixer());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void stopMain() {
        isRunning = false;
        api.shutdown();
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
                this.getLogger().warning("This version is unsupported, disabling. We currently don't support \":"+version);
                Bukkit.getPluginManager().disablePlugin(this);
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        this.saveDefaultConfig();

        String token = getConfig().getString("token");
        Config.shareCode = getConfig().getString("shareCode");
        Config.clientID = getConfig().getString("clientID");
        Config.projectID = getConfig().getInt("projectID");

        Config.followCommand = getConfig().getString("followCommand");
        Config.subscriberCommand = getConfig().getString("subscriberCommand");
        Config.resubscriberCommand = getConfig().getString("resubscriberCommand");

        Config.bannedWords = getConfig().getStringList("bannedWords");

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
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                api = new Main(token, new Mixer());//TODO:Make tokens per-player
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
