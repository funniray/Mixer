package pro.kdray.funniray.mixer;

public enum Commands {

    PAUSE("pause", "Pauses interactive buttons", "/ipause", Permissions.PAUSE),
    STOP("stop", "Stops interactive", "/istop", Permissions.STOP),
    START("start", "Starts interactive", "/istart", Permissions.START);

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
