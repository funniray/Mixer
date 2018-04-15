package pro.kdray.funniray.mixer;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.AsyncTask;
import pro.kdray.funniray.mixer.command.pause;
import pro.kdray.funniray.mixer.command.start;
import pro.kdray.funniray.mixer.command.stop;
import pro.kdray.funniray.mixer.events.mixer;

import java.util.concurrent.ExecutionException;

public final class MixerNukkit extends PluginBase {

    public static Plugin plugin;
    private static main api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        this.saveDefaultConfig();

        //Getting config
        config.shareCode = getConfig().getString("shareCode");
        config.clientID = getConfig().getString("clientID");
        config.projectID = getConfig().getInt("projectID");

        config.FollowCommand = getConfig().getString("followCommand");
        config.SubscriberCommand = getConfig().getString("subscriberCommand");
        config.ResubscriberCommand = getConfig().getString("resubscriberCommand");

        config.bannedWords = (String[]) getConfig().getStringList("bannedWords").toArray();

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

        this.getServer().getCommandMap().register(commands.PAUSE.getName(), new pause());
        this.getServer().getCommandMap().register(commands.STOP.getName(), new stop());
        this.getServer().getCommandMap().register(commands.START.getName(), new start());
    }

    public static void startMain(){
        //Run main class
        String token = MixerNukkit.plugin.getConfig().getString("token");
        Server.getInstance().getScheduler().scheduleAsyncTask(MixerNukkit.plugin, new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    api = new main(token,new mixer());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        api.shutdown();
    }

    public Plugin getPlugin(){
        return this;
    }

    public static main getApi() {
        return api;
    }

    public static void setApi(main api) {
        MixerNukkit.api = api;
    }
}
