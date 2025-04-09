package committee.nova.mods.mc_lark;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;


@EventBusSubscriber(modid = McLark.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> APP_ID = BUILDER
            .define("app_id", "XXXXXXXXXXXXX");
    public static final ModConfigSpec.ConfigValue<String> APP_SECRET = BUILDER
            .define("app_secret", "XXXXXXXXXXXXX");
    public static final ModConfigSpec.ConfigValue<String> CHAT_ID = BUILDER
            .comment("以群 ID 来标识群聊")
            .define("chat_id", "XXXXXXXXXXXXX");
    public static final ModConfigSpec.ConfigValue<String> TEMPLATE_ID = BUILDER
            .comment("消息卡片的TemplateId")
            .define("template_id", "XXXXXXXXXXXXX");
    public static final ModConfigSpec.ConfigValue<String> VERIFICATION_TOKEN = BUILDER
            .comment("字符串Token校验")
            .define("verification_token", "XXXXXXXXXXXXX");

    public static final ModConfigSpec.BooleanValue SEND_ENABLE = BUILDER
            .comment("开启所有")
            .define("send_enable", true);
    public static final ModConfigSpec.BooleanValue LOGIN_ENABLE = BUILDER
            .comment("开启登陆消息")
            .define("login_enable", true);
    public static final ModConfigSpec.BooleanValue LOGOUT_ENABLE = BUILDER
            .comment("开启离开消息")
            .define("logout_enable", true);
    public static final ModConfigSpec.BooleanValue DEATH_ENABLE = BUILDER
            .comment("开启死亡消息")
            .define("death_enable", true);
    public static final ModConfigSpec.BooleanValue ADVANCEMENT_ENABLE = BUILDER
            .comment("开启成就消息")
            .define("advancement_enable", true);
    public static final ModConfigSpec.BooleanValue CHAT_ENABLE = BUILDER
            .comment("开启聊天消息")
            .define("chat_enable", true);

    public static final ModConfigSpec.ConfigValue<String> MC_PREFIX = BUILDER
            .comment("字符串Token校验")
            .define("mc_prefix", "MC");
    public static final ModConfigSpec.BooleanValue MC_PREFIX_ON = BUILDER
            .comment("开启聊天消息")
            .define("mc_prefix_on", true);


    public static final ModConfigSpec.ConfigValue<String> LANGUAGE = BUILDER
            .comment("语言文件")
            .define("language", "zh_cn");

    static final ModConfigSpec SPEC = BUILDER.build();


    public static String appId;
    public static String appSecret;
    public static String chatId;
    public static String templateId;
    public static String verificationToken;

    public static boolean mcPrefixOn;
    public static String mcPrefix;
    public static String language;

    public static boolean sendEnable;
    public static boolean loginEnable;
    public static boolean logoutEnable;
    public static boolean deathEnable;
    public static boolean advancementEnable;
    public static boolean chatEnable;



    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        appId = APP_ID.get();
        appSecret = APP_SECRET.get();
        chatId = CHAT_ID.get();
        templateId = TEMPLATE_ID.get();
        verificationToken = VERIFICATION_TOKEN.get();
        sendEnable = SEND_ENABLE.get();
        loginEnable = LOGIN_ENABLE.get();
        logoutEnable = LOGOUT_ENABLE.get();
        deathEnable = DEATH_ENABLE.get();
        advancementEnable = ADVANCEMENT_ENABLE.get();
        chatEnable = CHAT_ENABLE.get();

        mcPrefixOn = MC_PREFIX_ON.get();
        mcPrefix = MC_PREFIX.get();

        language = LANGUAGE.get();
    }
}
