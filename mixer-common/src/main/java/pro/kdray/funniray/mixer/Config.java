package pro.kdray.funniray.mixer;

import java.util.List;

public class Config {
    public static String clientID;
    public static String shareCode;
    public static int projectID;
    public static String permPrefix = "mixer";

    public static String DBUrl = "unused";
    public static String DBUsername = "unused";
    public static String DBPassword = "unused";

    public static String subscriberCommand;
    public static String resubscriberCommand;
    public static String followCommand;

    public static List<String> bannedWords;

    public static String messagesPrefix = "&6Mixer &8| ";
    public static String messagesChatConnect = "&cChat Connected!";
    public static String messagesConnectInteractive = "&aInteractive connected!";
    public static String messagesDisableInteractive = "&cInteractive has been successfully disabled!";
    public static String messagesEstablishedNotConnected = "&cConnection was established but isn't connected!";
    public static String messagesPause = "Buttons have been paused";
    public static String messagesResume = "Buttons have been resumed";
}
