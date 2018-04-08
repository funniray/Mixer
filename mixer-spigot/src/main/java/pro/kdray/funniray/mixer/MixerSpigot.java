package pro.kdray.funniray.mixer;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pro.kdray.funniray.mixer.compadibility.VersionHandler;
import pro.kdray.funniray.mixer.compadibility.v1_9.Handler_1_9_R1;
import pro.kdray.funniray.mixer.compadibility.v1_9.Handler_1_9_R2;
import pro.kdray.funniray.mixer.compadibility.v1_10.Handler_1_10_R1;
import pro.kdray.funniray.mixer.compadibility.v1_11.Handler_1_11_R1;
import pro.kdray.funniray.mixer.compadibility.v1_12.Handler_1_12_R1;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

public final class MixerSpigot extends JavaPlugin {

    public static Plugin plugin;

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

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                main.initializeAPI(token,new mixer());//TODO:Make tokens per-player
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
        main.shutdown();
    }
}
