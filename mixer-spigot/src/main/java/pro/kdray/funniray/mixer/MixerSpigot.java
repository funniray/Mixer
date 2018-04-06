package pro.kdray.funniray.mixer;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

public final class MixerSpigot extends JavaPlugin {

    public static Plugin plugin;

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

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main.shutdown();
    }
}
