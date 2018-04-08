package pro.kdray.funniray.mixer.events;


import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.MixerForge;
import pro.kdray.funniray.mixer.Permissions;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        ITextComponent text = new TextComponentString(message.replace("&", "§"));
        this.debug(text.getUnformattedText());
        for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (!player.canUseCommand(Permissions.RECIEVEMESSAGES.getPermissionLevel(),null))
                continue;
            player.sendMessage(text);
        }
    }
    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        if (title != null) {
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title),fadein,duration,fadeout);
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                if (!player.canUseCommand(Permissions.RECIEVEMESSAGES.getPermissionLevel(),null))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
        if (subtitle != null){
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(subtitle),fadein,duration,fadeout);
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                if (!player.canUseCommand(Permissions.RECIEVEMESSAGES.getPermissionLevel(),null))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        if (title != null) {
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title));
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                if (!player.canUseCommand(Permissions.RECIEVEMESSAGES.getPermissionLevel(),null))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
        if (subtitle != null){
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(subtitle));
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                if (!player.canUseCommand(Permissions.RECIEVEMESSAGES.getPermissionLevel(),null))
                    continue;
                player.connection.sendPacket(titleMain);
            }
        }
    }

    @Override
    public void sendActionBar(String title) {
        SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, new TextComponentString(title));
        for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
            if (!player.canUseCommand(Permissions.RECIEVEMESSAGES.getPermissionLevel(),null))
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
        for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
            if (!player.canUseCommand(Permissions.RUNCOMMANDS.getPermissionLevel(),null))
                continue;
            player.getServerWorld().addScheduledTask(()-> FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(player, command.replace("%streamer%", player.getName())));
        }
    }

    @Override
    public void runCommandAsConsole(String command){
        for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (!player.canUseCommand(Permissions.RUNCOMMANDS.getPermissionLevel(),null))
                continue;
            player.getServerWorld().addScheduledTask(()-> FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(), command.replace("%streamer%", player.getName())));
        }
    }

    @Override
    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public void debug(String message) {
        MixerForge.getLogger().info(message);
    }
}
