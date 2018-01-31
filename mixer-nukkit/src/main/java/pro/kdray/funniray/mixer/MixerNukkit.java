package pro.kdray.funniray.mixer;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

public final class MixerNukkit extends PluginBase {

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            main.initializeAPI("AKI Key",new mixer());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Plugin getPlugin(){
        return this;
    }
}
