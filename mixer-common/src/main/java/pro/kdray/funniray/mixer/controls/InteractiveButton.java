package pro.kdray.funniray.mixer.controls;

import com.google.gson.JsonObject;
import com.mixer.interactive.resources.control.ButtonControl;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import com.mixer.interactive.resources.scene.InteractiveScene;
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
    private boolean runAsServer = false;
    private boolean resetScene = false;
    //TODO: private String move;
    private String message;
    private String title;
    private String subtitle;
    private String actionTitle;
    private String tempAllScene;
    private int tempAllTime;
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
            handler.getEventHandler().debug("&9&l[Mixer] &r&9You're still using type/action. This is depreciated and might be removed.");
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
                case "runCommandAsServer":
                    this.runCommand = meta.get("action").getAsJsonObject().get("value").getAsString();
                    this.runAsServer = true;
                    break;
                case "switchWindow":
                    this.switchWindow = meta.get("action").getAsJsonObject().get("value").getAsString();
                    break;
            }
        }

        if (meta.get("runCommand") != null)
            this.runCommand = meta.get("runCommand").getAsJsonObject().get("value").getAsString();

        if (meta.get("runCommandAsServer") != null) {
            this.runCommand = meta.get("runCommandAsServer").getAsJsonObject().get("value").getAsString();
            this.runAsServer = true;
        }

        if (meta.get("summon") != null){
            this.summon = meta.get("summon").getAsJsonObject().get("value").getAsString();
            if (meta.get("NBT") != null){
                this.NBT = meta.get("NBT").getAsJsonObject().get("value").getAsString();
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

        if (meta.get("setTempSceneAll") != null && meta.get("setTempSceneAllTime") != null){
            this.tempAllScene = meta.get("setTempSceneAll").getAsJsonObject().get("value").getAsString();
            this.tempAllTime = meta.get("setTempSceneAllTime").getAsJsonObject().get("value").getAsInt();
        }

        if (meta.get("resetScene") != null && meta.get("resetScene").getAsBoolean()){
            this.resetScene = true;
        }
    }

    public boolean onClick(InteractiveParticipant participant){

        boolean didAction = false;

        boolean updateButton = false;

        if (control.getCooldown() != null)
            if (control.getCooldown() > new Date().getTime())
                return false;

        if (this.handler.isPaused()){
            return false;
        }

        StringBuilder SSLBuilder = new StringBuilder(); //Space Separated List
        StringBuilder CSLBuilder = new StringBuilder(); //Comma Separated List
        StringBuilder HRLBuilder = new StringBuilder(); //Human Readable List
        if (this.requiredClicks > 1){
            this.clickedBy.add(participant);

            if (this.requiredClicks > this.clickedBy.size()){
                control.setProgress((float) this.clickedBy.size()/this.requiredClicks);
                this.handler.updateControl(control);
                return true;
            }else {
                control.setProgress(0F);
                updateButton = true;

                List<InteractiveParticipant> noDups = new ArrayList<>();

                for (InteractiveParticipant participant1 : this.clickedBy) {
                    if (noDups.contains(participant))
                        continue;
                    noDups.add(participant1);
                }

                for (int i = 0; i < this.clickedBy.size() - 1; i++) {
                    SSLBuilder.append(this.clickedBy.get(i).getUsername()).append(" ");
                    CSLBuilder.append(this.clickedBy.get(i).getUsername()).append(",");
                }
                if (noDups.size() > 1){
                    for (int i = 0; i < noDups.size() - 1; i++) {
                        HRLBuilder.append(noDups.get(i).getUsername()).append(" ");
                    }
                    HRLBuilder.append("and ").append(noDups.get(noDups.size() - 1).getUsername());
                }else{
                    HRLBuilder.append(participant.getUsername());
                }
                SSLBuilder.append(this.clickedBy.get(this.clickedBy.size()-1).getUsername());
                CSLBuilder.append(this.clickedBy.get(this.clickedBy.size()-1).getUsername());
                this.clickedBy = new ArrayList<>();
            }
        }
        String SSL = SSLBuilder.toString();
        String CSL = CSLBuilder.toString();
        String HRL = HRLBuilder.toString();

        if (this.switchWindow != null) {
            handler.switchSceneForParticipant(participant, this.switchWindow);
            didAction = true;
        }


        if (this.timeout > 0){
            control.setCooldown(new Date().getTime() + (timeout*1000));
            updateButton = true;
        }

        if (this.runCommand != null) {
            String command = this.runCommand
                    .replace("%CSL%", CSL)
                    .replace("%SSL%", SSL)
                    .replace("%HRL%", HRL)
                    .replace("%presser%", participant.getUsername());
            if (this.runAsServer) {
                this.handler.getEventHandler().runCommandAsConsole(command);
                didAction = true;
            }else{
                this.handler.getEventHandler().runCommand(command);
                didAction = true;
            }
        }

        if (this.summon != null){
            if (this.NBT != null) {
                this.handler.getEventHandler().summon((this.summon + " ~ ~ ~ " + this.NBT)
                        .replace("%CSL%",CSL)
                        .replace("%SSL%",SSL)
                        .replace("%HRL%",HRL)
                        .replace("%presser%",participant.getUsername()));
                didAction = true;
            }else{
                this.handler.getEventHandler().summon(this.summon
                        .replace("%CSL%",CSL)
                        .replace("%SSL%",SSL)
                        .replace("%HRL%",HRL)
                        .replace("%presser%",participant.getUsername()));
                didAction = true;
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

        if (this.tempAllScene != null){
            this.handler.switchAllScenes(this.tempAllScene);
            this.handler.getEventHandler().runAsyncAfter(()-> this.handler.switchAllScenes("default"),this.tempAllTime);
            didAction = true;
        }

        if (updateButton)
            this.handler.updateControl(control);

        if (this.resetScene) {
            this.handler.resetScene(this.getButton().getSceneID());
        }

        return didAction;
    }

    public void resetButton(boolean update){
        this.clickedBy = new ArrayList<>();
        this.control.setProgress(0F);
        this.control.setCooldown(0);
        if (update)
            this.handler.updateControl(control);
    }

    public void resetButton(){
        this.resetButton(true);
    }

    public ButtonControl getButton(){
        return this.control;
    }

    public String getSceneID(){
        return this.control.getSceneID();
    }
}
