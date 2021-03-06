package pro.kdray.funniray.mixer.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerForge;
import pro.kdray.funniray.mixer.Permissions;

public class Mixer implements MixerEvents {

    private MinecraftServer server;

    public Mixer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void sendMessage(String message) {
        ITextComponent text = new TextComponentString(message.replace("&", "§"));
        this.debug(text.getFormattedText());
        for(EntityPlayerMP player:server.getPlayerList().getPlayers()) {
            if (!player.hasPermissionLevel(Permissions.RECEIVEMESSAGES.getPermissionLevel()))
                continue;
            player.sendMessage(text);
        }
    }
    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        if (title != null) {
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title),fadein,duration,fadeout);
            for(EntityPlayerMP player:server.getPlayerList().getPlayers()){
                if (!player.hasPermissionLevel(Permissions.RECEIVEMESSAGES.getPermissionLevel()))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
        if (subtitle != null){
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(subtitle),fadein,duration,fadeout);
            for(EntityPlayerMP player:server.getPlayerList().getPlayers()){
                if (!player.hasPermissionLevel(Permissions.RECEIVEMESSAGES.getPermissionLevel()))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        if (title != null) {
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title));
            for(EntityPlayerMP player:server.getPlayerList().getPlayers()){
                if (!player.hasPermissionLevel(Permissions.RECEIVEMESSAGES.getPermissionLevel()))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
        if (subtitle != null){
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(subtitle));
            for(EntityPlayerMP player:server.getPlayerList().getPlayers()){
                if (!player.hasPermissionLevel(Permissions.RECEIVEMESSAGES.getPermissionLevel()))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
    }

    @Override
    public void sendActionBar(String title) {
        SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, new TextComponentString(title));
        for(EntityPlayerMP player:server.getPlayerList().getPlayers()){
            if (!player.hasPermissionLevel(Permissions.RECEIVEMESSAGES.getPermissionLevel()))
                continue;
            player.connection.sendPacket(titleMain);
        }
    }

    @Override
    public void summon(String entity) {
        runCommandAsConsole("execute %streamer% ~ ~ ~ summon "+entity);
    }

    @Override
    public void runCommand(String command) {
        for(EntityPlayerMP player:server.getPlayerList().getPlayers()){
            if (!player.hasPermissionLevel(Permissions.RUNCOMMANDS.getPermissionLevel()))
                continue;
            player.getServerWorld().addScheduledTask(()->
                    server.getCommandManager().handleCommand(player.getCommandSource(), command.replace("%streamer%", player.getName().getFormattedText())));
        }
    }

    @Override
    public void runCommandAsConsole(String command){
        for(EntityPlayerMP player:server.getPlayerList().getPlayers()) {
            if (!player.hasPermissionLevel(Permissions.RUNCOMMANDS.getPermissionLevel()))
                continue;
            player.getServerWorld().addScheduledTask(()-> server.getCommandManager().handleCommand(server.getCommandSource(), command.replace("%streamer%", player.getName().getFormattedText())));
        }
    }

    @Override
    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public void runAsyncAfter(Runnable runnable, int after) {
        new Thread(()->{
            try {
                Thread.sleep(after);
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void debug(String message) {
        MixerForge.getLogger().info(message);
    }
}
