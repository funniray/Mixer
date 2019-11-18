package pro.kdray.funniray.mixer;

import com.mixer.api.MixerAPI;
import com.mixer.api.http.HttpBadResponseException;
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
    private String APIKey;
    private String clientID;

    public Main(String APIKey, MixerEvents eventHandler) {

        if (dataSource != null)
            dataSource = new DataSource(Config.DBUrl, Config.DBUsername, Config.DBPassword);
        if (storageHandler != null)
            storageHandler = new StorageHandler(dataSource);

        this.eventHandler = eventHandler;
        this.APIKey = APIKey;
        this.clientID = Config.clientID;
    }

    public void setAPIKey(String key) {
        this.APIKey = key;
    }

    public void startChat() throws ExecutionException, InterruptedException {
        MixerAPI mixer = new MixerAPI(APIKey, clientID);

        MixerUser user = mixer.use(UsersService.class).getCurrent().get();
        MixerChat chat = mixer.use(ChatService.class).findOne(user.channel.id).get();

        constellation = new Constellation(mixer, user, eventHandler);
        chatHandler = new Chat(mixer, user, chat, eventHandler);
    }

    public void startInteractive() throws ExecutionException, InterruptedException {
        MixerAPI mixer = new MixerAPI(APIKey, clientID);

        MixerUser user = mixer.use(UsersService.class).getCurrent().get();

        interactive = new Interactive(mixer, user, APIKey, eventHandler);
    }

    public void startAll() throws ExecutionException, InterruptedException {
        MixerAPI mixer = new MixerAPI(APIKey, clientID);

        MixerUser user = mixer.use(UsersService.class).getCurrent().get();
        MixerChat chat = mixer.use(ChatService.class).findOne(user.channel.id).get();

        constellation = new Constellation(mixer, user, eventHandler);
        chatHandler = new Chat(mixer, user, chat, eventHandler);
        interactive = new Interactive(mixer, user, APIKey, eventHandler);
    }

    public void stopChat() {
        if (this.chatHandler != null) {
            this.chatHandler.disconnect();
        }
        if (this.constellation != null) {
            this.constellation.disconnect();
        }
    }

    public void stopInteractive() {
        if (this.interactive != null) {
            this.interactive.disconnect();
        }
    }

    public void shutdown(){
        this.stopChat();
        this.stopInteractive();
        eventHandler.sendMessage("&9&l[Mixer]&r&9 Successfully disabled interactive.");
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
