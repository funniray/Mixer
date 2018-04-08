package pro.kdray.funniray.mixer;

import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.api.resource.chat.MixerChat;
import com.mixer.api.services.impl.ChatService;
import com.mixer.api.services.impl.UsersService;
import pro.kdray.funniray.mixer.handlers.Chat;
import pro.kdray.funniray.mixer.handlers.Constellation;
import pro.kdray.funniray.mixer.handlers.Interactive;

import java.util.concurrent.ExecutionException;

public class main {

    private Interactive interactive;
    private Constellation constellation;
    private Chat chatHandler;

    public main(String APIKey, MixerEvents eventHandler) throws ExecutionException, InterruptedException {
        MixerAPI mixer = new MixerAPI(APIKey);

        MixerUser user = mixer.use(UsersService.class).getCurrent().get();
        MixerChat chat = mixer.use(ChatService.class).findOne(user.channel.id).get();

        constellation = new Constellation(mixer,user,eventHandler);
        interactive = new Interactive(mixer,user,APIKey,eventHandler);
        chatHandler = new Chat(mixer,user,chat,eventHandler);
    }

    public void shutdown(){
        try {
            interactive.disconnect();
            constellation.disconnect();
            chatHandler.disconnect();
        }catch(NullPointerException e){
            //Do nothing because whoops
        }
    }

    public Interactive getInteractive() {
        return interactive;
    }

    public Constellation getConstellation() {
        return constellation;
    }

    public Chat getChatHandler() {
        return chatHandler;
    }
}
