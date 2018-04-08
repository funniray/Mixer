package pro.kdray.funniray.mixer.Controls;

import com.google.gson.JsonObject;
import com.mixer.interactive.resources.control.ButtonControl;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import pro.kdray.funniray.mixer.handlers.Interactive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InteractiveButton {

    private Interactive handler;

    private ButtonControl control;

    private String summon;
    private String NBT;
    private String switchWindow;
    private String runCommand;
    //TODO: private String move;
    private String message;
    private String title;
    private String subtitle;
    private String actionTitle;
    private int timeout = 0;
    /**TODO:
     * private int time;
     * private ButtonControl enableButton;
     * private ButtonControl diableButton;
     */
    private int requiredClicks = 1;
    private List<InteractiveParticipant> clickedBy = new ArrayList<>();

    public InteractiveButton(ButtonControl control, Interactive handler) {

        this.control = control;

        JsonObject meta = control.getMeta();

        this.handler = handler;

        if (meta == null)
            return;

        if (meta.get("type") != null){
            switch (meta.get("type").getAsJsonObject().get("value").getAsString()){
                case "summon":
                    this.summon = meta.get("action").getAsJsonObject().get("value").getAsString();
                    if (meta.get("NBT") != null){
                        this.NBT = meta.get("NBT").getAsJsonObject().get("value").getAsString();
                    }
                    break;
                case "runCommand":
                    this.runCommand = meta.get("action").getAsJsonObject().get("value").getAsString();
                    break;
                case "switchWindow":
                    this.switchWindow = meta.get("action").getAsJsonObject().get("value").getAsString();
                    break;
            }
        }

        if (meta.get("switchWindow") != null)
            this.switchWindow = meta.get("switchWindow").getAsJsonObject().get("value").getAsString();

        if (meta.get("timeout") != null)
            this.timeout = meta.get("timeout").getAsJsonObject().get("value").getAsInt();

        if (meta.get("sendMessage") != null)
            this.message = meta.get("sendMessage").getAsJsonObject().get("value").getAsString();

        if (meta.get("sendTitle") != null)
            this.title = meta.get("sendTitle").getAsJsonObject().get("value").getAsString();

        if (meta.get("sendSubtitle") != null)
            this.subtitle = meta.get("sendSubtitle").getAsJsonObject().get("value").getAsString();

        if (meta.get("sendActionTitle") != null)
            this.actionTitle = meta.get("sendActionTitle").getAsJsonObject().get("value").getAsString();

        if (meta.get("requiredClicks") != null)
            this.requiredClicks = meta.get("requiredClicks").getAsJsonObject().get("value").getAsInt();
    }

    public void onClick(InteractiveParticipant participant){

        boolean updateButton = false;

        if (control.getCooldown() != null)
            if (control.getCooldown() > new Date().getTime())
                return;

        StringBuilder SSLBuilder = new StringBuilder(); //Space Separated List
        StringBuilder CSLBuilder = new StringBuilder(); //Comma Separated List
        StringBuilder HRLBuilder = new StringBuilder(); //Human Readable List
        if (this.requiredClicks > 1){
            this.clickedBy.add(participant);

            if (this.requiredClicks > this.clickedBy.size()){
                control.setProgress((float) this.clickedBy.size()/this.requiredClicks);
                this.handler.updateControl(control);
                return;
            }else{
                control.setProgress(0F);
                updateButton = true;

                for (int i = 0; i < this.clickedBy.size()-1; i++){
                    SSLBuilder.append(this.clickedBy.get(i).getUsername()).append(" ");
                    HRLBuilder.append(this.clickedBy.get(i).getUsername()).append(" ");
                    SSLBuilder.append(this.clickedBy.get(i).getUsername()).append(",");
                }
                SSLBuilder.append(this.clickedBy.get(this.clickedBy.size()-1).getUsername());
                HRLBuilder.append("and ").append(this.clickedBy.get(this.clickedBy.size()-1).getUsername());
                CSLBuilder.append(this.clickedBy.get(this.clickedBy.size()-1).getUsername());
                this.clickedBy = new ArrayList<>();
            }
        }
        String SSL = SSLBuilder.toString();
        String CSL = CSLBuilder.toString();
        String HRL = HRLBuilder.toString();

        if (this.switchWindow != null)
            handler.switchSceneForParticipant(participant,this.switchWindow);


        if (this.timeout > 0){
            control.setCooldown(new Date().getTime() + (timeout*1000));
            updateButton = true;
        }

        if (this.runCommand != null)
            this.handler.getEventHandler().runCommand(this.runCommand
                    .replace("%CSL%",CSL)
                    .replace("%SSL%",SSL)
                    .replace("%HRL%",HRL)
                    .replace("%presser%",participant.getUsername()));

        if (this.summon != null){
            if (this.NBT != null) {
                this.handler.getEventHandler().summon((this.summon + " ~ ~ ~ " + this.NBT)
                        .replace("%CSL%",CSL)
                        .replace("%SSL%",SSL)
                        .replace("%HRL%",HRL)
                        .replace("%presser%",participant.getUsername()));
            }else{
                this.handler.getEventHandler().summon(this.summon
                        .replace("%CSL%",CSL)
                        .replace("%SSL%",SSL)
                        .replace("%HRL%",HRL)
                        .replace("%presser%",participant.getUsername()));
            }
        }

        if (this.message != null)
            this.handler.getEventHandler().sendMessage(this.message
                    .replace("%CSL%",CSL)
                    .replace("%SSL%",SSL)
                    .replace("%HRL%",HRL)
                    .replace("%presser%",participant.getUsername()));

        if (this.title != null || this.subtitle != null)
            this.handler.getEventHandler().sendTitle(this.title
                    .replace("%CSL%",CSL)
                    .replace("%SSL%",SSL)
                    .replace("%HRL%",HRL)
                    .replace("%presser%",participant.getUsername())
                    ,this.subtitle
                    .replace("%CSL%",CSL)
                    .replace("%SSL%",SSL)
                    .replace("%HRL%",HRL)
                    .replace("%presser%",participant.getUsername()));

        if (this.actionTitle != null)
            this.handler.getEventHandler().sendActionBar(this.actionTitle
                    .replace("%CSL%",CSL)
                    .replace("%SSL%",SSL)
                    .replace("%HRL%",HRL)
                    .replace("%presser%",participant.getUsername()));

        if (updateButton) {
            this.handler.updateControl(control);
        }
    }
}
