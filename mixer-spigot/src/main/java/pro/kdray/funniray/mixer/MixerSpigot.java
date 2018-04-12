package pro.kdray.funniray.mixer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pro.kdray.funniray.mixer.command.pause;
import pro.kdray.funniray.mixer.compadibility.VersionHandler;
import pro.kdray.funniray.mixer.compadibility.v1_9.Handler_1_9_R1;
import pro.kdray.funniray.mixer.compadibility.v1_9.Handler_1_9_R2;
import pro.kdray.funniray.mixer.compadibility.v1_10.Handler_1_10_R1;
import pro.kdray.funniray.mixer.compadibility.v1_11.Handler_1_11_R1;
import pro.kdray.funniray.mixer.compadibility.v1_12.Handler_1_12_R1;
import pro.kdray.funniray.mixer.events.mixer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

public final class MixerSpigot extends JavaPlugin {

    public static Plugin plugin;
    public static main api;

    public static VersionHandler versionHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        this.saveDefaultConfig();

        String token = getConfig().getString("token");
        config.shareCode = getConfig().getString("shareCode");
        config.clientID = getConfig().getString("clientID");
        config.projectID = getConfig().getInt("projectID");

        config.FollowCommand = getConfig().getString("followCommand");
        config.SubscriberCommand = getConfig().getString("subscriberCommand");
        config.ResubscriberCommand = getConfig().getString("resubscriberCommand");

        //registering permissions
        PluginManager pm = this.getServer().getPluginManager();
        Permission basePerm = new Permission(config.permPrefix+".*");
        pm.addPermission(basePerm);
        for (Permissions permission:Permissions.values()){
            Permission perm = new Permission(permission.getNode(),permission.getDescription());
            perm.setDefault(PermissionDefault.valueOf(permission.getDefaultMode()));
            perm.addParent(basePerm,true);
            pm.addPermission(perm);
        }

        //Credit to mine-care ( https://bukkit.org/members/mine-care.90737861/ )
        try {
            Method commandMap = plugin.getServer().getClass().getMethod("getCommandMap", null);
            Object cmdmap = commandMap.invoke(plugin.getServer(), null);
            Method register = cmdmap.getClass().getMethod("register", String.class, Command.class);
            register.invoke(cmdmap, commands.PAUSE.getName(), new pause());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                api = new main(token,new mixer());//TODO:Make tokens per-player
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
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
        api.shutdown();
    }

    public static main getApi() {
        return api;
    }

    public static void setApi(main api) {
        MixerSpigot.api = api;
    }
}
