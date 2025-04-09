package committee.nova.mods.mc_lark;

import com.mojang.logging.LogUtils;
import committee.nova.mods.mc_lark.core.LarkApi;
import committee.nova.mods.mc_lark.event.IChatEvent;
import committee.nova.mods.mc_lark.event.IPlayerEvent;
import committee.nova.mods.mc_lark.event.ITickEvent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(McLark.MODID)
public class McLark
{
    public static final String MODID = "mc_lark";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static LarkApi LARK_API = new LarkApi();

    public McLark(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        NeoForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        I18n.init();
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
    }

    @SubscribeEvent
    public void onServerTick(LevelTickEvent.Post event) {
        ITickEvent.register(event.getLevel().getServer());
    }

    @SubscribeEvent
    public void cmdRegister(@NotNull RegisterCommandsEvent event) {
    }

    @SubscribeEvent
    public void onPlayerJoin(@NotNull PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
            IPlayerEvent.loggedIn(event.getEntity().level(), serverPlayer);
    }
    @SubscribeEvent
    public void onPlayerLeave(@NotNull PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
            IPlayerEvent.loggedOut(event.getEntity().level(), serverPlayer);
    }

    @SubscribeEvent
    public void onPlayerDeath(@NotNull LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
            IPlayerEvent.death(event.getSource(), serverPlayer);
    }

    @SubscribeEvent
    public void onPlayerChat(@NotNull ServerChatEvent event) {
        IChatEvent.register(event.getPlayer(), event.getRawText());
    }

    @SubscribeEvent
    public void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
            IPlayerEvent.advancement(serverPlayer, event.getAdvancement().value());
    }

}
