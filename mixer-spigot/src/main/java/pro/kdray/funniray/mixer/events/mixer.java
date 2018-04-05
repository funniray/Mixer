package pro.kdray.funniray.mixer.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerSpigot;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',message));
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
        for(Player player:Bukkit.getOnlinePlayers()){
            runCommand("execute "+player.getName()+" ~ ~ ~ summon "+entity);
        }
    }

    @Override
    public void runCommand(String command) {
        Bukkit.getScheduler().runTask(MixerSpigot.plugin,() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),command));
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
