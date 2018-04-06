package pro.kdray.funniray.mixer.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerSpigot;
import pro.kdray.funniray.mixer.Permissions;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        String formatted = ChatColor.translateAlternateColorCodes('&',message);
        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            if (!player.hasPermission(Permissions.RECIEVEMESSAGES.getNode()))
                continue;
            player.sendMessage(formatted);
        }
        this.debug(formatted);
    }
    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            player.sendTitle(title,subtitle,fadein,duration,fadeout);
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            player.sendTitle(title,subtitle);
        }
    }

    @Override
    public void summon(String entity) {
        runCommandAsConsole("execute %streamer% ~ ~ ~ summon "+entity);
    }

    @Override
    public void runCommand(String command) {
        Bukkit.getScheduler().runTask(MixerSpigot.plugin,() -> {
            for(Player player:Bukkit.getOnlinePlayers()){
                if (!player.hasPermission(Permissions.RUNCOMMANDS.getNode()))
                    continue;
                Bukkit.getServer().dispatchCommand(player,command.replace("%streamer",player.getName()));
            }
        });
    }

    @Override
    public void runCommandAsConsole(String command){
        Bukkit.getScheduler().runTask(MixerSpigot.plugin,() ->{
            for(Player player:Bukkit.getOnlinePlayers()){
                if (!player.hasPermission(Permissions.RUNCOMMANDS.getNode()))
                    continue;
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),command.replace("%streamer",player.getName()));
            }
        });
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(MixerSpigot.plugin,runnable);
    }

    @Override
    public void debug(String message) {
        Bukkit.getLogger().info(message);
    }
}
