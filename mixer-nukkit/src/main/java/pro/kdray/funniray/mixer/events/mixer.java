package pro.kdray.funniray.mixer.events;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerNukkit;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        Server.getInstance().broadcastMessage(TextFormat.colorize('&',message));
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            player.sendTitle(title,subtitle,fadein,duration,fadeout);
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()){
            player.sendTitle(title,subtitle);
        }
    }

    @Override
    public void summon(String entity) {
        for(Player player:Server.getInstance().getOnlinePlayers().values()){
            runCommand("execute "+player.getName()+" ~ ~ ~ summon "+entity);
        }
    }

    @Override
    public void runCommand(String command) {
        Server.getInstance().getScheduler().scheduleTask(MixerNukkit.plugin, () -> Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(),command));
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

}
