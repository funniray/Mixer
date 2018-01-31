package pro.kdray.funniray.mixer;

public interface MixerEvents {
    void sendMessage(String message);
    void sendTitle(String title, String subtitle, int fadein, int duration, int fadeout);
    void sendTitle(String title, String subtitle);
}
