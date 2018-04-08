package pro.kdray.funniray.mixer;

public interface MixerEvents {
    void sendMessage(String message);
    void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout);
    void sendTitle(String title, String subtitle);
    void sendActionBar(String title);
    void summon(String entity);
    void runCommand(String command);
    void runAsync(Runnable runnable);
    void debug(String message);
    void runCommandAsConsole(String command);
}
