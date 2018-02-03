package pro.kdray.funniray.mixer.handlers;

import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.api.resource.constellation.AbstractConstellationDatagram;
import com.mixer.api.resource.constellation.AbstractConstellationReply;
import com.mixer.api.resource.constellation.MixerConstellation;
import com.mixer.api.resource.constellation.events.LiveEvent;
import com.mixer.api.resource.constellation.methods.LiveSubscribeMethod;
import com.mixer.api.resource.constellation.methods.data.LiveRequestData;
import com.mixer.api.resource.constellation.ws.MixerConstellationConnectable;
import pro.kdray.funniray.mixer.MixerEvents;

import java.util.ArrayList;
import java.util.Random;

public class Constellation {

    private MixerConstellationConnectable constellationConnectable;

    public Constellation(MixerAPI mixer, MixerUser user, MixerEvents eventHandler){

        MixerConstellationConnectable constellation = new MixerConstellation().connectable(mixer);

        if (constellation.connect()){
            LiveSubscribeMethod SubscribeMethod = new LiveSubscribeMethod();
            Random ID_RANDOM = new Random();
            SubscribeMethod.id =  Math.abs(ID_RANDOM.nextInt());
            SubscribeMethod.method = "livesubscribe";
            SubscribeMethod.type = AbstractConstellationDatagram.Type.EVENT;
            SubscribeMethod.params = new LiveRequestData();
            SubscribeMethod.params.events = new ArrayList<>();
            SubscribeMethod.params.events.add("channel:"+user.channel.id+":followed");
            SubscribeMethod.params.events.add("channel:"+user.channel.id+":subscribed");
            SubscribeMethod.params.events.add("channel:"+user.channel.id+":update");
            constellation.send(SubscribeMethod, new com.mixer.api.resource.constellation.replies.ReplyHandler<AbstractConstellationReply>() {
                public void onSuccess(AbstractConstellationReply result) {
                    eventHandler.sendMessage("&9&l[Mixer]&r&9 Constellation Connected!");
                }
                public void onFailure(Throwable e) {
                    e.printStackTrace();
                }
            });
        }

        constellation.on(LiveEvent.class, event -> {
            String[] channel = event.data.channel.split(":");
            String message;
            switch (channel[2]){
                case "followed":
                    if (!event.data.payload.get("following").getAsBoolean())
                        break;
                    message = "&9"+event.data.payload.get("user").getAsJsonObject().get("username").getAsString()+" followed!";
                    eventHandler.sendTitle("",message);
                    eventHandler.sendMessage("&9&l[Mixer] &r"+message);
                    break;
                case "subscribed":
                    message = "&9"+event.data.payload.get("user").getAsJsonObject().get("username").getAsString()+" Subscribed!";
                    eventHandler.sendTitle(message, "");
                    eventHandler.sendMessage("&9&l[Mixer] &r"+message);
                    break;
                case "update":
                    message = "&9Channel update, new title is "+event.data.payload.get("name").getAsString();
                    eventHandler.sendTitle(message, "");
                    eventHandler.sendMessage("&9&l[Mixer] &r"+message);
                    break;
                default:
                    eventHandler.sendMessage("&9&l[Mixer]&r&c >>> Unhandled Contellation Event: "+event.toString());
            }
        });

        this.constellationConnectable = constellation;
    }

    public void disconnect(){
        this.constellationConnectable.disconnect();
    }
}
