package pro.kdray.funniray.mixer;

import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;

public class Utils {
    public static LiteralText formatText(String message){
        return Text.of(message.replace("&", "§"));
    }
}
