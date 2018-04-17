package pro.kdray.funniray.mixer;

public enum Permissions {

    RUNCOMMANDS(Config.permPrefix + ".runcommands", "Players with this permission will run interactive Commands when buttons are pressed.", "OP", 3),
    RECIEVEMESSAGES(Config.permPrefix + ".revieveMessages", "Lets the player recieve messages from the Mixer plugin", "OP", 3),
    PAUSE(Config.permPrefix + ".Commands.pause", "Lets the player run /pause", "OP", 3),
    STOP(Config.permPrefix + ".Commands.stop", "Lets the player run /stop", "OP", 3),
    START(Config.permPrefix + ".Commands.start", "Lets the player run /start", "OP", 3);

    private String node;
    private String description;
    private String defaultMode;
    private int permissionLevel;

    Permissions(String node, String description, String defaultMode, int permissionLevel){
        this.node = node;
        this.defaultMode = defaultMode;
        this.permissionLevel = permissionLevel;
    }

    public String getNode() {
        return node;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultMode() {
        return defaultMode;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }
}
