package pro.kdray.funniray.mixer.compadibility;

import javax.annotation.Nullable;

public class NullHandler implements VersionHandler {
    @Override
    public void sendAllActionBar(String message, @Nullable String permission) {}

    @Override
    public void sendAllTitle(String title, String subtitle, int fadein, int duration, int fadeout, String permission) {}

    @Override
    public void sendAllTitle(String title, String subtitle, String permission) {}
}
