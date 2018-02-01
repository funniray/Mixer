package pro.kdray.funniray.mixer.handlers;

import com.google.common.eventbus.Subscribe;
import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.interactive.GameClient;
import com.mixer.interactive.event.InteractiveEvent;
import com.mixer.interactive.event.control.input.ControlKeyDownEvent;
import pro.kdray.funniray.mixer.MixerEvents;

public class Interactive {

    private GameClient client;
    private MixerEvents eventHandler;

    Interactive(MixerAPI mixer, MixerUser user, String token, MixerEvents events){

        client = new GameClient(189031);
        client.connect(token);

        client.getEventBus().register(this);

        this.client = client;
        this.eventHandler = events;
    }

    @Subscribe
    public void onInteractiveEvent(InteractiveEvent event){
        ControlKeyDownEvent event1 = (ControlKeyDownEvent) event;
        event1.getControlInput().getControlID();
    }

    public void disconnect(){
        this.client.disconnect();
    }
}
