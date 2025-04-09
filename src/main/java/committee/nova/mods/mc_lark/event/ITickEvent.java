package committee.nova.mods.mc_lark.event;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

import java.util.LinkedList;
import java.util.Queue;


/**
 * @Project: lark
 * @Author: cnlimiter
 * @CreateTime: 2025/4/10 01:30
 * @Description:
 */
public class ITickEvent {
    private static final Queue<MutableComponent> SEND_QUEUE = new LinkedList<>();

    public static Queue<MutableComponent> sendQueue() {
        return SEND_QUEUE;
    }

    public static void register(MinecraftServer server) {
        MutableComponent toSend = SEND_QUEUE.poll();
        if (server != null
                && server.isDedicatedServer()
                && toSend != null
        ) {
            server.getPlayerList().broadcastSystemMessage(toSend, false);
        }
    }
}
