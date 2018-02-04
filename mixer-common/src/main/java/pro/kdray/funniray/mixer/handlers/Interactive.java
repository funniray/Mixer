package pro.kdray.funniray.mixer.handlers;

import com.google.common.eventbus.Subscribe;
import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.interactive.GameClient;
import com.mixer.interactive.event.InteractiveEvent;
import com.mixer.interactive.event.connection.ConnectionEstablishedEvent;
import com.mixer.interactive.event.control.input.ControlInputEvent;
import com.mixer.interactive.event.control.input.ControlKeyDownEvent;
import com.mixer.interactive.event.control.input.ControlMouseDownInputEvent;
import com.mixer.interactive.event.core.HelloEvent;
import com.mixer.interactive.event.participant.ParticipantJoinEvent;
import com.mixer.interactive.event.participant.ParticipantLeaveEvent;
import com.mixer.interactive.resources.InteractiveResource;
import com.mixer.interactive.resources.control.ButtonControl;
import com.mixer.interactive.resources.control.InteractiveControl;
import com.mixer.interactive.resources.group.InteractiveGroup;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import com.mixer.interactive.resources.scene.InteractiveScene;
import pro.kdray.funniray.mixer.MixerEvents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Interactive {

    private GameClient client;
    private MixerEvents eventHandler;
    private HashMap<String,InteractiveParticipant> participantHashMap = new HashMap<>();

    public Interactive(MixerAPI mixer, MixerUser user, String token, MixerEvents events){
        client = new GameClient(191773);
        client.connect(token);

        client.getEventBus().register(this);

        this.client = client;
        this.eventHandler = events;
    }

//    @Subscribe
//    public void onInteractiveEvent(InteractiveEvent event) {
//        eventHandler.sendMessage("&9[Mixer] Interactive Event &c>>> " + event.toString());
//    }

    @Subscribe
    public void onParticipantJoin(ParticipantJoinEvent event) {
        Set<InteractiveParticipant> participants = event.getParticipants();
        for (InteractiveParticipant participant : participants) {
            eventHandler.sendMessage("&9[Mixer] >>> " + participant.getUsername() + "["+participant.getSessionID()+"] joined with group " + participant.getGroupID());
            participantHashMap.putIfAbsent(participant.getSessionID(), participant);
        }
    }

    @Subscribe
    public void onParticipantLeave(ParticipantLeaveEvent event){
        HashMap<String,InteractiveParticipant> oldParticipants = participantHashMap;
        for(InteractiveParticipant participant:event.getParticipants()){
            oldParticipants.remove(participant.getSessionID());
        }
        for(String id:oldParticipants.keySet()){
            participantHashMap.remove(id);
            eventHandler.sendMessage("&9[Mixer] >>> "+id+" left");
        }
    }

    @Subscribe
    public void onConnectionEsablished(ConnectionEstablishedEvent event){
        try {
            Set<InteractiveControl> controls = client.using(GameClient.CONTROL_SERVICE_PROVIDER).getControls().get();
            for(InteractiveControl control:controls){
                eventHandler.sendMessage("&9[Mixer] >>> "+control.getControlID()+" is "+control.getKind()+" on "+control.getSceneID());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        client.ready(true);
    }

    @Subscribe
    public void onKeyDown(ControlInputEvent event){
        if (event instanceof ControlKeyDownEvent || event instanceof ControlMouseDownInputEvent)
            eventHandler.sendMessage("&9[Mixer] >>> "+participantHashMap.get(event.getParticipantID()).getUsername()+" pressed "+event.getControlInput().getControlID());
    }

    public void disconnect(){
        this.client.disconnect();
    }
}
