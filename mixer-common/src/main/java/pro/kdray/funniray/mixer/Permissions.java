package pro.kdray.funniray.mixer;

public enum Permissions {

    RUNCOMMANDS(config.permPrefix+".runcommands","Players with this permission will run interactive commands when buttons are pressed.","OP",3),
    RECIEVEMESSAGES(config.permPrefix+".revieveMessages","Lets the player recieve messages from the Mixer plugin","OP",3);

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
