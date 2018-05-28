package pro.kdray.funniray.mixer;

public enum Commands {

    PAUSE("ipause", "Pauses interactive buttons", "/ipause", Permissions.PAUSE),
    STOP("istop", "Stops interactive", "/istop", Permissions.STOP),
    START("istart", "Starts interactive", "/istart", Permissions.START),
    SWITCHSCENE("iswitchscene", "Sets the default scene to the specified scene", "/iswitchscene <scene>", Permissions.SWITCHSCENE),
    RESETSCENE("iresetscene", "Resets the specified scene", "/iresetscene <scene>", Permissions.RESETSCENE),
    MAIN("mixer", "Sets various settings related to the plugin", "/mixer (reload) | (token <token>)", Permissions.MAINCOMMAND);

    private String name;
    private String description;
    private String usage;
    private Permissions permission;

    Commands(String name, String description, String usage, Permissions permission) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public Permissions getPermission() {
        return permission;
    }
}
