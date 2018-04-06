package pro.kdray.funniray.mixer.events;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerNukkit;
import pro.kdray.funniray.mixer.Permissions;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        Server.getInstance().broadcastMessage(TextFormat.colorize('&',message));
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            player.sendTitle(title);
            player.sendActionBar(subtitle);
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            player.sendTitle(title);
            player.sendActionBar(subtitle);
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
    public void debug(String message) {
        Server.getInstance().getLogger().info(message);
    }

}
