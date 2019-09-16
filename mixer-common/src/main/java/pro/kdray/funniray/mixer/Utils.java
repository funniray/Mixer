package pro.kdray.funniray.mixer;

import com.mixer.api.resource.MixerUser;

import java.util.List;

public class Utils {
    public static String getColorFromRank(List<MixerUser.Role> roles){
        String color = "&4";

        if (roles.contains(MixerUser.Role.OWNER)) {
            color = "&c";
        }else if (roles.contains(MixerUser.Role.FOUNDER) || roles.contains(MixerUser.Role.STAFF) || roles.contains(MixerUser.Role.GLOBAL_MOD)){
            color = "&3";
        }else if (roles.contains(MixerUser.Role.MOD)) {
            color = "&2";
        }else if (roles.contains(MixerUser.Role.SUBSCRIBER)){
            color = "&6";
        }else if (roles.contains(MixerUser.Role.PRO)){
            color = "&d";
        }else if (roles.contains(MixerUser.Role.USER)){
            color = "&9";
        }

        return color;
    }
}
