package pro.kdray.funniray.mixer.events;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import pro.kdray.funniray.mixer.MixerEvents;

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

}
