package pro.kdray.funniray.mixer.handlers;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;
import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.interactive.GameClient;
import com.mixer.interactive.event.connection.ConnectionEstablishedEvent;
import com.mixer.interactive.event.control.input.ControlInputEvent;
import com.mixer.interactive.event.control.input.ControlKeyDownEvent;
import com.mixer.interactive.event.control.input.ControlMouseDownInputEvent;
import com.mixer.interactive.event.participant.ParticipantJoinEvent;
import com.mixer.interactive.event.participant.ParticipantLeaveEvent;
import com.mixer.interactive.resources.control.InteractiveControl;
import com.mixer.interactive.resources.group.InteractiveGroup;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import com.mixer.interactive.resources.scene.InteractiveScene;
import pro.kdray.funniray.mixer.MixerEvents;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Interactive {

    private GameClient client;
    private MixerEvents eventHandler;
    private HashMap<String,InteractiveParticipant> participantHashMap = new HashMap<>();
    private HashMap<String,InteractiveControl> controlHashMap = new HashMap<>();
    private HashMap<String,InteractiveGroup> groupHashMap = new HashMap<>();
    private HashMap<String,InteractiveScene> sceneHashMap = new HashMap<>();

    public Interactive(MixerAPI mixer, MixerUser user, String token, MixerEvents events){
        client = new GameClient(191773,"fa54866255ea641235e596e5659fa726a4aa9f7ecc72758f");
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
                controlHashMap.put(control.getControlID(),control);
            }
            Set<InteractiveScene> scenes = client.using(GameClient.SCENE_SERVICE_PROVIDER).getScenes().get();
            for(InteractiveScene scene:scenes){
                sceneHashMap.put(scene.getSceneID(),scene);
                if (!scene.getSceneID().equals("default")) {
                    InteractiveGroup group = new InteractiveGroup(scene.getSceneID());
                    group.setScene(scene);
                    groupHashMap.put(scene.getSceneID(), group);
                }
            }
            client.using(GameClient.GROUP_SERVICE_PROVIDER).create(groupHashMap.values()).get();
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
            eventHandler.sendMessage("&9[Mixer] >>> " + participant.getUsername() + " pressed " + event.getControlInput().getControlID());
            InteractiveControl control = controlHashMap.get(event.getControlInput().getControlID());
            JsonObject meta = control.getMeta();
            if (meta.get("switchWindow") != null){
                switchSceneForParticipant(participant,meta.get("switchWindow").getAsJsonObject().get("value").getAsString());
            }
            String type = meta.get("type").getAsJsonObject().get("value").getAsString();
            if (type == null)
                return;
            String action = meta.get("action").getAsJsonObject().get("value").getAsString();
            if (type.equals("switchWindow")){
                switchSceneForParticipant(participant,action);
            }else{
                handleActions(participant,type,action);
            }
        }
    }

    private void switchSceneForParticipant(InteractiveParticipant participant, String group){
        participant.changeGroup(getGroup(group));
        eventHandler.runAsync(() -> {
            try {
                client.using(GameClient.PARTICIPANT_SERVICE_PROVIDER).update(participant).get();
            } catch (InterruptedException | ExecutionException e) {
                //I don't really care what happens here
            }
        });
    }

    private void handleActions(InteractiveParticipant participant,String type, String action){
        switch(type){
            case "summon":
                eventHandler.summon(action);
                break;
            case "runCommand":
                eventHandler.runCommand(action);
                break;
            default:
                eventHandler.sendMessage("&9[Mixer] Unknown type: \""+type+"\"");
        }
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
}
