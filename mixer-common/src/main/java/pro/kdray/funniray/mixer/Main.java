package pro.kdray.funniray.mixer;

import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.api.resource.chat.MixerChat;
import com.mixer.api.services.impl.ChatService;
import com.mixer.api.services.impl.UsersService;
import pro.kdray.funniray.mixer.database.DataSource;
import pro.kdray.funniray.mixer.database.StorageHandler;
import pro.kdray.funniray.mixer.handlers.Chat;
import pro.kdray.funniray.mixer.handlers.Constellation;
import pro.kdray.funniray.mixer.handlers.Interactive;

import java.util.concurrent.ExecutionException;

public class Main {

    private Interactive interactive;
    private Constellation constellation;
    private Chat chatHandler;

    private static DataSource dataSource;
    private static StorageHandler storageHandler;

    private MixerEvents eventHandler;

    public Main(String APIKey, MixerEvents eventHandler) throws ExecutionException, InterruptedException {

        if (dataSource != null)
            dataSource = new DataSource(Config.DBUrl, Config.DBUsername, Config.DBPassword);
        if (storageHandler != null)
            storageHandler = new StorageHandler(dataSource);

        MixerAPI mixer = new MixerAPI(APIKey);

        MixerUser user = mixer.use(UsersService.class).getCurrent().get();
        MixerChat chat = mixer.use(ChatService.class).findOne(user.channel.id).get();

        constellation = new Constellation(mixer,user,eventHandler);
        interactive = new Interactive(mixer,user,APIKey,eventHandler);
        chatHandler = new Chat(mixer,user,chat,eventHandler);

        this.eventHandler = eventHandler;
    }

    public void shutdown(){
        try {
            interactive.disconnect();
            constellation.disconnect();
            chatHandler.disconnect();
            eventHandler.sendMessage("&9&l[Mixer]&r&9 Successfully disabled interactive.");
        }catch(NullPointerException e){
            //Do nothing because whoops
            eventHandler.sendMessage("&9&l[Mixer]&r&9 Error occurred while disabling interactive.");
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

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static StorageHandler getStorageHandler() {
        return storageHandler;
    }
}