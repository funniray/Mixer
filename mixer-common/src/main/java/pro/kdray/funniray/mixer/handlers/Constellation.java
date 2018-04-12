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
import pro.kdray.funniray.mixer.config;

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
            SubscribeMethod.params.events.add("channel:"+user.channel.id+":resubscribed");
            //SubscribeMethod.params.events.add("channel:"+user.channel.id+":update");
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
            String actionUser = event.data.payload.get("user").getAsJsonObject().get("username").getAsString();
            switch (channel[2]){
                case "followed":
                    if (!event.data.payload.get("following").getAsBoolean())
                        break;
                    eventHandler.sendActionBar(actionUser+" followed!");
                    eventHandler.sendMessage("&9&l[Mixer] &r&9"+actionUser+" followed!");
                    eventHandler.runCommandAsConsole(config.FollowCommand.replace("%user%",actionUser));
                    break;
                case "subscribed":
                    eventHandler.sendTitle(actionUser+" subscribed!", "");
                    eventHandler.sendMessage("&9&l[Mixer] &r&9"+actionUser+" subscribed!");
                    eventHandler.runCommandAsConsole(config.SubscriberCommand.replace("%user%",actionUser));
                    break;
                case "resubscribed":
                    eventHandler.sendTitle(actionUser+" resubscribed!", "");
                    eventHandler.sendMessage("&9&l[Mixer] &r&9"+actionUser+" resubscribed!");
                    eventHandler.runCommandAsConsole(config.ResubscriberCommand.replace("%user%",actionUser).replace("%totalMonths%",""+event.data.payload.get("totalMonths").getAsInt()));
                    break;
                //case "update":
                //    message = "&9Channel update, new title is "+event.data.payload.get("name").getAsString();
                //    eventHandler.sendTitle(message, "");
                //    eventHandler.sendMessage("&9&l[Mixer] &r"+message);
                //    break;
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
