package pro.kdray.funniray.mixer;

import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.interactive.GameClient;

public class Interactive {

    private static GameClient client;

    static void InteractiveHandler(MixerAPI mixer, MixerUser user, String token, MixerEvents events){

        client = new GameClient(189031);

        client.connect(token);

    }
}
