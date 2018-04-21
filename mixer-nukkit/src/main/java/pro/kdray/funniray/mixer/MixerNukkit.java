package pro.kdray.funniray.mixer;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.AsyncTask;
import pro.kdray.funniray.mixer.command.Pause;
import pro.kdray.funniray.mixer.command.Start;
import pro.kdray.funniray.mixer.command.Stop;
import pro.kdray.funniray.mixer.command.SwitchScene;
import pro.kdray.funniray.mixer.events.Mixer;

import java.util.concurrent.ExecutionException;

public final class MixerNukkit extends PluginBase {

    public static Plugin plugin;
    private static Main api = null;
    private static boolean isRunning = false;

    public static void startMain(){
        isRunning = true;
        //Run Main class
        String token = MixerNukkit.plugin.getConfig().getString("token");
        Server.getInstance().getScheduler().scheduleAsyncTask(MixerNukkit.plugin, new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    api = new Main(token, new Mixer());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        this.saveDefaultConfig();

        //Getting Config
        Config.shareCode = getConfig().getString("shareCode");
        Config.clientID = getConfig().getString("clientID");
        Config.projectID = getConfig().getInt("projectID");

        Config.followCommand = getConfig().getString("followCommand");
        Config.subscriberCommand = getConfig().getString("subscriberCommand");
        Config.resubscriberCommand = getConfig().getString("resubscriberCommand");

        Config.bannedWords = getConfig().getStringList("bannedWords");

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
    }
}
