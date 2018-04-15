package pro.kdray.funniray.mixer.events;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerNukkit;
import pro.kdray.funniray.mixer.Permissions;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        String formatted = TextFormat.colorize('&',message);
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            if (!player.hasPermission(Permissions.RECIEVEMESSAGES.getNode()))
                continue;
            player.sendMessage(formatted);
        }
        this.debug(formatted);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            if (!player.hasPermission(Permissions.RECIEVEMESSAGES.getNode()))
                continue;
            player.sendTitle(title,subtitle,fadein,duration,fadeout);
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            if (!player.hasPermission(Permissions.RECIEVEMESSAGES.getNode()))
                continue;
            player.sendTitle(title,subtitle);
        }
    }

    @Override
    public void sendActionBar(String title) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            if (!player.hasPermission(Permissions.RECIEVEMESSAGES.getNode()))
                continue;
            player.sendActionBar(title);
        }
    }

    @Override
    public void summon(String entity) {
        runCommandAsConsole("execute %streamer% ~ ~ ~ summon "+entity); //TODO: Make it run the right command
    }

    @Override
    public void runCommand(String command) {
        Server.getInstance().getScheduler().scheduleTask(MixerNukkit.plugin, () -> {
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                if (!player.hasPermission(Permissions.RUNCOMMANDS.getNode()))
                    continue;
                Server.getInstance().dispatchCommand(player, command.replace("%streamer%", player.getName()));
            }
        });
    }

    @Override
    public void runCommandAsConsole(String command) {
        Server.getInstance().getScheduler().scheduleTask(MixerNukkit.plugin, () -> {
            for(Player player:Server.getInstance().getOnlinePlayers().values()) {
                if (!player.hasPermission(Permissions.RUNCOMMANDS.getNode()))
                    continue;
                Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command.replace("%streamer%",player.getName()));
            }
        });
    }

    @Override
    public void runAsync(Runnable runnable) {
        Server.getInstance().getScheduler().scheduleAsyncTask(MixerNukkit.plugin, new AsyncTask() {
            @Override
            public void onRun() {
                runnable.run();
            }
        });
    }

    @Override
    public void runAsyncAfter(Runnable runnable, int after) {
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int i) {
                runnable.run();
            }
        },after/50,true);
    }

    @Override
    public void debug(String message) {
        Server.getInstance().getLogger().info(message);
    }

}
