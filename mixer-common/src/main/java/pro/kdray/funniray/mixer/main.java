package pro.kdray.funniray.mixer;

import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.api.resource.chat.MixerChat;
import com.mixer.api.services.impl.ChatService;
import com.mixer.api.services.impl.UsersService;

import java.util.concurrent.ExecutionException;

public class main {
    public static void initializeAPI(String APIKey, MixerEvents eventHandler) throws ExecutionException, InterruptedException {
        MixerAPI mixer = new MixerAPI(APIKey);

        MixerUser user = mixer.use(UsersService.class).getCurrent().get();
        MixerChat chat = mixer.use(ChatService.class).findOne(user.channel.id).get();

        Constellation.ConstellationHandler(mixer,user,eventHandler);
        Chat.ChatHandler(mixer,user,chat,eventHandler);
    }
}
