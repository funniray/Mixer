package pro.kdray.funniray.mixer;

public enum commands {

    PAUSE("pause","Pauses interactive buttons","/pause",Permissions.PAUSE),
    STOP("stop","Stops interactive","/stop",Permissions.STOP),
    START("start","Starts interactive","/start",Permissions.START);

    private String name;
    private String description;
    private String usage;
    private Permissions permission;

    commands(String name, String description, String usage, Permissions permission){
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
