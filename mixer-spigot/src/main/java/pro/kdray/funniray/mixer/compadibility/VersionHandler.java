package pro.kdray.funniray.mixer.compadibility;

import javax.annotation.Nullable;

public interface VersionHandler {
    void sendAllActionBar(String message, @Nullable String permission);
    void sendAllTitle(String title, String subtitle, int fadein, int duration, int fadeout, String permission);
    void sendAllTitle(String title, String subtitle, String permission);
}
