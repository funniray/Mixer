package pro.kdray.funniray.mixer;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

public final class MixerSpigot extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        String token = getConfig().getString("token");
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    main.initializeAPI(token,new mixer());//TODO:Make tokens per-player
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
}
