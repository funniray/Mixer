package pro.kdray.funniray.mixer;

import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.AsyncTask;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

public final class MixerNukkit extends PluginBase {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        this.saveDefaultConfig();

        //Getting config
        String token = getConfig().getString("token");
        config.shareCode = getConfig().getString("shareCode");
        config.clientID = getConfig().getString("clientID");
        config.projectID = getConfig().getInt("projectID");

        //Registering permissions
        PluginManager pm = this.getServer().getPluginManager();
        Permission basePerm = new Permission(config.permPrefix+".*");
        pm.addPermission(basePerm);
        for (Permissions permission:Permissions.values()){
            Permission perm = new Permission(permission.getNode(),permission.getDescription());
            perm.setDefault(permission.getDefaultMode());
            perm.addParent(basePerm,true);
            pm.addPermission(perm);
        }

        //Run main class
        getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    main.initializeAPI(token,new mixer());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main.shutdown();
    }

    public Plugin getPlugin(){
        return this;
    }
}
