package pro.kdray.funniray.mixer.handlers;

import com.google.common.eventbus.Subscribe;
import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.interactive.GameClient;
import com.mixer.interactive.event.connection.ConnectionEstablishedEvent;
import com.mixer.interactive.event.control.input.ControlInputEvent;
import com.mixer.interactive.event.control.input.ControlKeyDownEvent;
import com.mixer.interactive.event.control.input.ControlMouseDownInputEvent;
import com.mixer.interactive.event.participant.ParticipantJoinEvent;
import com.mixer.interactive.event.participant.ParticipantLeaveEvent;
import com.mixer.interactive.resources.control.ButtonControl;
import com.mixer.interactive.resources.control.InteractiveControl;
import com.mixer.interactive.resources.control.InteractiveControlType;
import com.mixer.interactive.resources.group.InteractiveGroup;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import com.mixer.interactive.resources.scene.InteractiveScene;
import com.mixer.interactive.services.SceneServiceProvider;
import pro.kdray.funniray.mixer.Config;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.controls.InteractiveButton;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static com.mixer.interactive.GameClient.GROUP_SERVICE_PROVIDER;

public class Interactive {

    private String token;
    private GameClient client;
    private MixerEvents eventHandler;

    private ConcurrentHashMap<String, InteractiveParticipant> participantHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, InteractiveControl> controlHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, InteractiveGroup> groupHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, InteractiveScene> sceneHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, InteractiveButton> buttonHashMap = new ConcurrentHashMap<>();

    private boolean isPause = false;

    public Interactive(MixerAPI mixer, MixerUser user, String token, MixerEvents events){
        client = new GameClient(Config.projectID, Config.clientID);
        client.connect(token, Config.shareCode);

        client.getEventBus().register(this);

        this.client = client;
        this.eventHandler = events;
        this.token = token;
    }

    @Subscribe
    public void onParticipantJoin(ParticipantJoinEvent event) {
        Set<InteractiveParticipant> participants = event.getParticipants();
        for (InteractiveParticipant participant : participants) {
            eventHandler.debug("&9[Mixer] >>> " + participant.getUsername() + "["+participant.getSessionID()+"] joined with group " + participant.getGroupID());
            participantHashMap.putIfAbsent(participant.getSessionID(), participant);
        }
    }

    @Subscribe
    public void onParticipantLeave(ParticipantLeaveEvent event){
        ConcurrentHashMap<String, InteractiveParticipant> oldParticipants = participantHashMap;
        for(InteractiveParticipant participant:event.getParticipants()){
            oldParticipants.remove(participant.getSessionID());
        }
        for(String id:oldParticipants.keySet()){
            participantHashMap.remove(id);
            eventHandler.debug("&9&l[Mixer]&r&9 >>> "+id+" left");
        }
    }

    @Subscribe
    public void onConnectionEsablished(ConnectionEstablishedEvent event){
        try {
            if (!client.isConnected()) {
                this.eventHandler.sendMessage("&9&l[Mixer]&r&4 Connection was established but isn't connected!");
            }else{
                this.eventHandler.sendMessage("&9&l[Mixer]&r&9 Interactive connected!");
            }

            Set<InteractiveScene> scenes = new SceneServiceProvider(client).getScenes().get();
            for(InteractiveScene scene:scenes){
                Set<InteractiveControl> controls = scene.getControls();
                for(InteractiveControl control:controls){
                    eventHandler.debug("&9&l[Mixer]&r&9 >>> "+control.getControlID()+" is "+control.getKind()+" on "+control.getSceneID());
                    controlHashMap.put(control.getControlID(),control);
                    if (control.getKind() == InteractiveControlType.BUTTON)
                        buttonHashMap.put(control.getControlID(),new InteractiveButton((ButtonControl) control,this));
                }

                sceneHashMap.put(scene.getSceneID(),scene);
                if (!scene.getSceneID().equals("default")) {
                    InteractiveGroup group = new InteractiveGroup(scene.getSceneID());
                    group.setScene(scene);
                    groupHashMap.put(scene.getSceneID(), group);
                }
            }
            client.using(GROUP_SERVICE_PROVIDER).create(groupHashMap.values()).get();
            InteractiveGroup defaultGroup = new InteractiveGroup("default");
            defaultGroup.setScene("default");
            groupHashMap.put("default",defaultGroup);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        client.ready(true);
    }

    @Subscribe
    public void onKeyDown(ControlInputEvent event){
        if (event instanceof ControlKeyDownEvent || event instanceof ControlMouseDownInputEvent) {
            InteractiveParticipant participant = participantHashMap.get(event.getParticipantID());
            if (participant == null)
                return;
            eventHandler.debug("&9&l[Mixer]&r&9 >>> " + participant.getUsername() + " pressed " + event.getControlInput().getControlID());
            eventHandler.debug("&9&l[Mixer]&r&9 >>> Control: " + event.getControlInput().getRawInput());
            if (buttonHashMap.get(event.getControlInput().getControlID()).onClick(participant)) {
                this.getEventHandler().runAsync(() -> {
                    try {
                        event.getTransaction().capture(this.client).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void updateControl(ButtonControl control){
        eventHandler.runAsync(() -> {
            try {
                client.using(GameClient.CONTROL_SERVICE_PROVIDER).update(control).get();
            } catch (InterruptedException | ExecutionException e) {
                //I don't really care what happens here
            }
        });
    }

    public void switchSceneForParticipant(InteractiveParticipant participant, String group){
        participant.changeGroup(getGroup(group));
        eventHandler.runAsync(() -> {
            try {
                client.using(GameClient.PARTICIPANT_SERVICE_PROVIDER).update(participant).get();
            } catch (InterruptedException | ExecutionException e) {
                //I don't really care what happens here
            }
        });
    }

    private InteractiveGroup getGroup(String groupString){
        if(groupHashMap.containsKey(groupString)){
            return groupHashMap.get(groupString);
        }
        return null;
    }

    public void disconnect(){
        this.client.disconnect();
    }

    public MixerEvents getEventHandler() {
        return eventHandler;
    }

    public void switchAllScenes(String scene){
        InteractiveGroup group = this.getGroup("default").setScene(scene);
        for(InteractiveParticipant participant:this.participantHashMap.values()){
            participant.changeGroup("default");
        }

        eventHandler.runAsync(()->{
            client.using(GameClient.PARTICIPANT_SERVICE_PROVIDER).update(this.participantHashMap.values());
            client.using(GameClient.GROUP_SERVICE_PROVIDER).update(group);
        });
    }

    public boolean isPaused(){
        return this.isPause;
    }

    public void pause(){
        if (this.isPause){
            switchAllScenes("default");
            eventHandler.sendMessage("&9&l[Mixer]&r&9 Buttons have been unpaused");
        }else{
            switchAllScenes("pause");
            eventHandler.sendMessage("&9&l[Mixer]&r&9 Buttons have been paused");
        }
        this.isPause = !this.isPause;
    }
}
