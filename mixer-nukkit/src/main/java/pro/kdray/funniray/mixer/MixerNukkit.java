package pro.kdray.funniray.mixer;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

public final class MixerNukkit extends PluginBase {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        String token = getConfig().getString("token");
        config.shareCode = getConfig().getString("shareCode");
        config.clientID = getConfig().getString("clientID");
        config.projectID = getConfig().getInt("projectID");
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
        plugin = this;
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
