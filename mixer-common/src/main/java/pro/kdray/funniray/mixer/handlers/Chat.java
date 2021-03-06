package pro.kdray.funniray.mixer.handlers;

import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.api.resource.chat.MixerChat;
import com.mixer.api.resource.chat.events.IncomingMessageEvent;
import com.mixer.api.resource.chat.events.data.MessageComponent;
import com.mixer.api.resource.chat.methods.AuthenticateMessage;
import com.mixer.api.resource.chat.replies.AuthenticationReply;
import com.mixer.api.resource.chat.replies.ReplyHandler;
import com.mixer.api.resource.chat.ws.MixerChatConnectable;
import pro.kdray.funniray.mixer.Config;
import pro.kdray.funniray.mixer.MixerEvents;
import pro.kdray.funniray.mixer.Utils;

public class Chat {

    private MixerChatConnectable chatConnectable;

    public Chat(MixerAPI mixer, MixerUser user, MixerChat chat, MixerEvents eventHandler){
        MixerChatConnectable chatConnectable = chat.connectable(mixer);

        if (chatConnectable.connect()) {
            chatConnectable.send(AuthenticateMessage.from(user.channel, user, chat.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    eventHandler.sendMessage("&9&l[Mixer]&r&9 Chat Connected!");
                }
                public void onFailure(Throwable e) {
                    e.printStackTrace();
                }
            });
        }

        chatConnectable.on(IncomingMessageEvent.class, event -> {
            if (event.data.message.isWhisper())
                return;
            StringBuilder finishedMessage = new StringBuilder("&9&l[Mixer] " + Utils.getColorFromRank(event.data.userRoles) + event.data.userName + "&9&l: &r&7");
            for (MessageComponent.MessageTextComponent message : event.data.message.message){
                finishedMessage.append(message.text.replace("§", "§§").replace("&", "&&7"));
            }
            boolean shouldShow = true;
            for (String word : Config.bannedWords) {
                if (finishedMessage.toString().contains(word)){
                    shouldShow = false;
                    break;
                }
            }
            if (shouldShow)
                eventHandler.sendMessage(finishedMessage.toString());
        });

        this.chatConnectable = chatConnectable;

    }

    public void disconnect(){
        this.chatConnectable.disconnect();
    }
}
