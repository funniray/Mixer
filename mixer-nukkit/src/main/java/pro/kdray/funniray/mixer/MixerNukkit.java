package pro.kdray.funniray.mixer;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.AsyncTask;
import pro.kdray.funniray.mixer.command.*;
import pro.kdray.funniray.mixer.events.Mixer;

import java.util.concurrent.ExecutionException;

public final class MixerNukkit extends PluginBase {

    public static Plugin plugin;
    private static Main api = null;
    private static boolean isRunning = false;

    public static void startMain(){
        //Run Main class
        MixerNukkit.plugin.reloadConfig();
        Server.getInstance().getScheduler().scheduleAsyncTask(MixerNukkit.plugin, new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    api.startAll();
                    isRunning = true;
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    new Mixer().sendMessage("&4&l[Mixer] &r&cFailed to start interactive, could be due to invalid API key");
                }
            }
        });
    }

    public static void stopMain() {
        if (api != null) {
            api.shutdown();
            isRunning = false;
        }
    }

    public static Main getApi() {
        return api;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setApi(Main api) {
        MixerNukkit.api = api;
    }

    public Plugin getPlugin() {
        return this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (isRunning) {
            stopMain();
        }
    }

    public static void reload() {
        boolean wasRunning = false;
        if (MixerNukkit.isRunning) {
            stopMain();
            wasRunning = true;
        }
        MixerNukkit.loadConfig();
        if (wasRunning)
            startMain();
    }

    public static void setConfig(String path, String value) {
        MixerNukkit.plugin.getConfig().set(path, value);
        MixerNukkit.plugin.saveConfig();
    }

    public static void loadConfig() {
        MixerNukkit.plugin.reloadConfig();

        cn.nukkit.utils.Config configuration = MixerNukkit.plugin.getConfig();

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

        this.saveDefaultConfig();
        this.reloadConfig();
        loadConfig();

        //Registering permissions
        PluginManager pm = this.getServer().getPluginManager();
        Permission basePerm = new Permission(Config.permPrefix + ".*");
        pm.addPermission(basePerm);
        for (Permissions permission : Permissions.values()) {
            Permission perm = new Permission(permission.getNode(), permission.getDescription());
            perm.setDefault(permission.getDefaultMode());
            perm.addParent(basePerm, true);
            pm.addPermission(perm);
        }

        this.getServer().getCommandMap().register(Commands.PAUSE.getName(), new Pause());
        this.getServer().getCommandMap().register(Commands.STOP.getName(), new Stop());
        this.getServer().getCommandMap().register(Commands.START.getName(), new Start());
        this.getServer().getCommandMap().register(Commands.SWITCHSCENE.getName(), new SwitchScene());
        this.getServer().getCommandMap().register(Commands.RESETSCENE.getName(), new ResetScene());
        this.getServer().getCommandMap().register(Commands.MAIN.getName(), new MainCommand());

        String token = MixerNukkit.plugin.getConfig().getString("token");
        api = new Main(token, new Mixer());
    }
}
