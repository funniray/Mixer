package pro.kdray.funniray.mixer.events;


import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pro.kdray.funniray.mixer.MixerEvents;

public class mixer implements MixerEvents {
    @Override
    public void sendMessage(String message) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(message.replace("&","§")));
    }
    @Override
    public void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout) {
        if (title != null) {
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title),fadein,duration,fadeout);
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                player.connection.sendPacket(titleMain);
            }
        }
        if (subtitle != null){
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(subtitle),fadein,duration,fadeout);
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                player.connection.sendPacket(titleMain);
            }
        }
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        if (title != null) {
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title));
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                player.connection.sendPacket(titleMain);
            }
        }
        if (subtitle != null){
            SPacketTitle titleMain = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(subtitle));
            for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                player.connection.sendPacket(titleMain);
            }
        }
    }

    @Override
    public void summon(String entity) {
        for(EntityPlayerMP player:FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
            FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(player,"summon "+entity);
        }
    }

    @Override
    public void runCommand(String command) {
        if (FMLCommonHandler.instance().getMinecraftServerInstance().getCommandSenderEntity() == null)
            return;
        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance().getCommandSenderEntity(),command);
    }

    @Override
    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }
}
