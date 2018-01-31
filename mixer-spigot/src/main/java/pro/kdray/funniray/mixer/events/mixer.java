package pro.kdray.funniray.mixer.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pro.kdray.funniray.mixer.MixerEvents;

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
}
