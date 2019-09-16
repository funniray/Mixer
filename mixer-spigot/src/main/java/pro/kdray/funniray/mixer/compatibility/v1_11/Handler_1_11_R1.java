package pro.kdray.funniray.mixer.compatibility.v1_11;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pro.kdray.funniray.mixer.compatibility.unknown.VersionHandler;

import javax.annotation.Nullable;

public class Handler_1_11_R1 implements VersionHandler {
    @Override
    public void sendAllActionBar(String message, @Nullable String permission) {
        //Credit to Bear53 ( https://www.spigotmc.org/members/bear53.32839/ )
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
                ChatColor.translateAlternateColorCodes('&', message) + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            if (permission != null)
                if (!player.hasPermission(permission))
                    continue;
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(bar);
        }
    }

    @Override
    public void sendAllTitle(String title, String subtitle, int fadein, int duration, int fadeout, String permission) {
        if (title != null) {
            IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
                    ChatColor.translateAlternateColorCodes('&', title) + "\"}");
            PacketPlayOutTitle titleP = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, icbc, fadein, duration, fadeout);
            for (Player player : Bukkit.getServer().getOnlinePlayers()){
                if (permission != null)
                    if (!player.hasPermission(permission))
                        continue;
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titleP);
            }
        }
        if (title != null) {
            IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
                    ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");
            PacketPlayOutTitle titleP = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, icbc, fadein, duration, fadeout);
            for (Player player : Bukkit.getServer().getOnlinePlayers()){
                if (permission != null)
                    if (!player.hasPermission(permission))
                        continue;
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titleP);
            }
        }
    }

    @Override
    public void sendAllTitle(String title, String subtitle, String permission) {
        if (title != null) {
            IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
                    ChatColor.translateAlternateColorCodes('&', title) + "\"}");
            PacketPlayOutTitle titleP = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, icbc);
            for (Player player : Bukkit.getServer().getOnlinePlayers()){
                if (permission != null)
                    if (!player.hasPermission(permission))
                        continue;
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titleP);
            }
        }
        if (title != null) {
            IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
                    ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");
            PacketPlayOutTitle titleP = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, icbc);
            for (Player player : Bukkit.getServer().getOnlinePlayers()){
                if (permission != null)
                    if (!player.hasPermission(permission))
                        continue;
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titleP);
            }
        }
    }
}
