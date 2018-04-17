package pro.kdray.funniray.mixer;

import com.mixer.api.resource.MixerUser;

import java.util.List;

public class Utils {
    public static String getColorFromRank(List<MixerUser.Role> roles){
        String color = "&4";

        if (roles.contains(MixerUser.Role.OWNER)) {
            color = "&f";
        }else if (roles.contains(MixerUser.Role.FOUNDER) || roles.contains(MixerUser.Role.STAFF) || roles.contains(MixerUser.Role.GLOBAL_MOD)){
            color = "&4";
        }else if (roles.contains(MixerUser.Role.MOD)) {
            color = "&a";
        }else if (roles.contains(MixerUser.Role.SUBSCRIBER)){
            color = "&6";
        }else if (roles.contains(MixerUser.Role.PRO)){
            color = "&5";
        }else if (roles.contains(MixerUser.Role.USER)){
            color = "&9";
        }

        return color;
    }
}
