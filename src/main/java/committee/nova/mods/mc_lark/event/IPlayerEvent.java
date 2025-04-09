package committee.nova.mods.mc_lark.event;

import committee.nova.mods.mc_lark.Config;
import committee.nova.mods.mc_lark.I18n;
import lombok.val;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static committee.nova.mods.mc_lark.McLark.LARK_API;

/**
 * @Project: lark
 * @Author: cnlimiter
 * @CreateTime: 2025/4/10 01:30
 * @Description:
 */
public class IPlayerEvent {
    public static void loggedIn(Level world, ServerPlayer player) {
        if (
                Config.sendEnable && Config.loginEnable
        ) {
            val msg = player.getDisplayName().getString() + " 加入了服务器";
            LARK_API.sendMsg(msg);
        }
    }

    public static void loggedOut(Level world, ServerPlayer player) {
        if (Config.sendEnable && Config.logoutEnable
        ) {
            val msg = player.getDisplayName().getString() + " 离开了服务器";
            LARK_API.sendMsg(msg);
        }
    }

    public static void death(DamageSource source, ServerPlayer player) {
        if (player != null && Config.sendEnable && Config.deathEnable) {
            LivingEntity livingEntity2 = player.getKillCredit();
            String message = "";

            String string = "lark.death.attack." + source.type().msgId();

            if (source.getEntity() == null && source.getDirectEntity() == null) {
                String string2 = string + ".player";
                message = livingEntity2 != null ? I18n.get(string2, player.getDisplayName().getString(), livingEntity2.getDisplayName().getString()) : I18n.get(string, player.getDisplayName().getString());
            } else {//支持物品造成的死亡信息
                assert source.getDirectEntity() != null;
                Component component = source.getEntity() == null ? source.getDirectEntity().getDisplayName() : source.getEntity().getDisplayName();
                Entity sourceEntity = source.getEntity();
                ItemStack itemStack;
                if (sourceEntity instanceof LivingEntity) {
                    itemStack = ((LivingEntity) sourceEntity).getMainHandItem();
                } else {
                    itemStack = ItemStack.EMPTY;
                }
                message = !itemStack.isEmpty() ? I18n.get(string + ".item", player.getDisplayName().getString(), component.getString(), itemStack.getDisplayName().getString()) : I18n.get(string,player.getDisplayName().getString(), component.getString());
            }
            val msg = String.format(message, player.getDisplayName().getString());
            LARK_API.sendMsg(msg);
        }
    }

    public static void advancement(ServerPlayer player, Advancement advancement) {
        if (advancement.display().isPresent() && Config.sendEnable && Config.advancementEnable) {
            DisplayInfo display = advancement.display().get();
            String message = I18n.get("mcbot.chat.type.advancement." + display.getType().getSerializedName(), player.getDisplayName().getString(), I18n.get(display.getTitle().getString()));
            val msg = String.format(message, player.getDisplayName().getString());
            LARK_API.sendMsg(msg);
        }
    }

}
