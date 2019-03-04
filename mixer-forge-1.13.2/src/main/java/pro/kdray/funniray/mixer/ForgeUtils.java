package pro.kdray.funniray.mixer;

import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.server.permission.PermissionAPI;

public class ForgeUtils {

    public static boolean hasPermission(ICommandSource sender, String node) {
        if (sender instanceof EntityPlayer) {
            return PermissionAPI.hasPermission((EntityPlayer) sender, node);
        } else {
            return true;
        }
    }

}
