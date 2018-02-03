package pro.kdray.funniray.mixer.handlers;

import com.google.common.eventbus.Subscribe;
import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.interactive.GameClient;
import com.mixer.interactive.event.InteractiveEvent;
import com.mixer.interactive.event.control.input.ControlKeyDownEvent;
import com.mixer.interactive.event.core.HelloEvent;
import com.mixer.interactive.event.participant.ParticipantJoinEvent;
import com.mixer.interactive.resources.group.InteractiveGroup;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import com.mixer.interactive.resources.scene.InteractiveScene;
import pro.kdray.funniray.mixer.MixerEvents;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Interactive {

    private GameClient client;
    private MixerEvents eventHandler;
    private InteractiveGroup defaultGroup;
    private InteractiveScene defaultScene;

    public Interactive(MixerAPI mixer, MixerUser user, String token, MixerEvents events){
        client = new GameClient(191773);
        client.connect(token);

        client.getEventBus().register(this);

        this.client = client;
        this.eventHandler = events;
    }

    @Subscribe
    public void onInteractiveEvent(InteractiveEvent event){
        eventHandler.sendMessage("&9[Mixer] Interactive Event &c>>> "+event.toString());
        if (event instanceof ParticipantJoinEvent){
            ParticipantJoinEvent pJoinEvent = (ParticipantJoinEvent) event;
            Set<InteractiveParticipant> participants = pJoinEvent.getParticipants();
            for (InteractiveParticipant participant : participants) {
                participant.changeGroup(defaultGroup);
            }
            try {
                client.using(GameClient.PARTICIPANT_SERVICE_PROVIDER).update(participants).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }else if (event instanceof HelloEvent){
            InteractiveScene defaultScene = new InteractiveScene("default");
            InteractiveGroup defaultGroup = new InteractiveGroup("default");

            defaultGroup.create(client);
            defaultGroup.setScene(defaultScene);

            try {
                client.using(GameClient.GROUP_SERVICE_PROVIDER).create(defaultGroup).get();
                client.using(GameClient.SCENE_SERVICE_PROVIDER).create(defaultScene).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            this.defaultGroup = defaultGroup;
            this.defaultScene = defaultScene;
        }
    }

    public void disconnect(){
        this.client.disconnect();
    }
}
