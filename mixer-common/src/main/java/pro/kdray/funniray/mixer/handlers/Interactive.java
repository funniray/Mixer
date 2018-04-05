package pro.kdray.funniray.mixer.handlers;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonElement;
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
import com.mixer.interactive.resources.control.ButtonControl;
import com.mixer.interactive.resources.control.InteractiveControl;
import com.mixer.interactive.resources.group.InteractiveGroup;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import com.mixer.interactive.resources.scene.InteractiveScene;
import com.mixer.interactive.services.SceneServiceProvider;
import pro.kdray.funniray.mixer.MixerEvents;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.mixer.interactive.GameClient.*;

public class Interactive {

    private String token;
    private GameClient client;
    private MixerEvents eventHandler;
    private HashMap<String,InteractiveParticipant> participantHashMap = new HashMap<>();
    private HashMap<String,InteractiveControl> controlHashMap = new HashMap<>();
    private HashMap<String,InteractiveGroup> groupHashMap = new HashMap<>();
    private HashMap<String,InteractiveScene> sceneHashMap = new HashMap<>();

    public Interactive(MixerAPI mixer, MixerUser user, String token, MixerEvents events){
        client = new GameClient(191773,"fa54866255ea641235e596e5659fa726a4aa9f7ecc72758f");
        client.connect(token,"dbzktlsk");

        client.getEventBus().register(this);

        this.client = client;
        this.eventHandler = events;
        this.token = token;
    }

//    @Subscribe
//    public void onInteractiveEvent(InteractiveEvent event) {
//        eventHandler.sendMessage("&9[Mixer] Interactive Event &c>>> " + event.toString());
//    }

    @Subscribe
    public void onParticipantJoin(ParticipantJoinEvent event) {
        Set<InteractiveParticipant> participants = event.getParticipants();
        for (InteractiveParticipant participant : participants) {
            //eventHandler.sendMessage("&9[Mixer] >>> " + participant.getUsername() + "["+participant.getSessionID()+"] joined with group " + participant.getGroupID());
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
            //eventHandler.sendMessage("&9&l[Mixer]&r&9 >>> "+id+" left");
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
                    //eventHandler.sendMessage("&9&l[Mixer]&r&9 >>> "+control.getControlID()+" is "+control.getKind()+" on "+control.getSceneID());
                    controlHashMap.put(control.getControlID(),control);
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
            //eventHandler.sendMessage("&9&l[Mixer]&r&9 >>> " + participant.getUsername() + " pressed " + event.getControlInput().getControlID());
            //eventHandler.sendMessage("&9&l[Mixer]&r&9 >>> Control: "+event.getControlInput().getRawInput());
            InteractiveControl control = controlHashMap.get(event.getControlInput().getControlID());
            JsonObject meta = control.getMeta();
            boolean updateButton = false;
            if (control instanceof ButtonControl) {
                ButtonControl buttonControl = ((ButtonControl) control);
                if (buttonControl.getCooldown() > new Date().getTime()){
                    return;
                }
            }
            if (meta.get("switchWindow") != null){
                switchSceneForParticipant(participant,meta.get("switchWindow").getAsJsonObject().get("value").getAsString());
            }
            String type = meta.get("type").getAsJsonObject().get("value").getAsString();
            if (type == null)
                return;
            JsonElement timeout = meta.get("timeout");
            if (timeout != null){
                if (control instanceof ButtonControl){
                    ButtonControl buttonControl = ((ButtonControl) control);
                    buttonControl.setCooldown(new Date().getTime() + (timeout.getAsJsonObject().get("value").getAsInt()*1000));
                    updateButton = true;
                }
            }
            String action = meta.get("action").getAsJsonObject().get("value").getAsString();
            if (type.equals("switchWindow")){
                switchSceneForParticipant(participant,action);
            }else{
                if (type.equals("summon") && meta.get("NBT") != null)
                    action += " ~ ~ ~ " + meta.get("NBT").getAsJsonObject().get("value").getAsString();
                handleActions(participant,type,action);
            }
            if (updateButton) {
                this.updateControl((ButtonControl) control);
            }
        }
    }

    private void updateControl(ButtonControl control){
        eventHandler.runAsync(() -> {
            try {
                client.using(GameClient.CONTROL_SERVICE_PROVIDER).update(control).get();
            } catch (InterruptedException | ExecutionException e) {
                //I don't really care what happens here
            }
        });
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
                eventHandler.summon(action.replace("%presser%",participant.getUsername()));
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
