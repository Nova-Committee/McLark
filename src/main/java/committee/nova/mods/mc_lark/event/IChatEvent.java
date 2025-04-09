package committee.nova.mods.mc_lark.event;


import committee.nova.mods.mc_lark.Config;
import net.minecraft.server.level.ServerPlayer;

import static committee.nova.mods.mc_lark.McLark.LARK_API;

/**
 * @Project: lark
 * @Author: cnlimiter
 * @CreateTime: 2025/4/10 01:30
 * @Description:
 */
public class IChatEvent {
    public static void register(ServerPlayer player, String message) {
        if (
                Config.sendEnable && Config.chatEnable
                        && !message.contains("CICode")
                        && !player.getCommandSenderWorld().isClientSide
        ) {
            String msg = String.format(Config.mcPrefixOn
                            ? "[" + Config.mcPrefix + "]<%s> %s"
                            : "<%s> %s",
                    player.getDisplayName().getString(), message);

            LARK_API.sendMsg(msg);
        }
    }

}
