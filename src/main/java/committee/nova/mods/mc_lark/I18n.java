package committee.nova.mods.mc_lark;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.locale.Language;
import net.neoforged.fml.loading.FMLLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Project: lark
 * @Author: cnlimiter
 * @CreateTime: 2025/4/10 01:30
 * @Description:
 */
public class I18n {
    private static Map<String, String> translations;


    public static Optional<Path> getResourcePath(String name) {
        return Optional.ofNullable(FMLLoader.getLoadingModList().getModFileById("mc_lark").getFile().findResource(name.split("/")));
    }

    public static void init() {
        translations = new HashMap<>();


        Optional<Path> optional =  getResourcePath(("lang/" + Config.language + ".json"));

        if (optional.isEmpty()) {
            McLark.LOGGER.warn("-----------------------------------------");
            McLark.LOGGER.warn("McBot cannot find translations for \"" + Config.language + "\" and uses \"en_us\" by default!");
            McLark.LOGGER.warn("");
            McLark.LOGGER.warn("You are welcome to contribute translations!");
            McLark.LOGGER.warn("Contributing: https://github.com/Nova-Committee/McBot#Contributing");
            McLark.LOGGER.warn("-----------------------------------------");

            optional = getResourcePath("lang/en_us.json");
        }

        if (optional.isPresent()) {
            try {
                String content = IOUtils.toString(Files.newInputStream(optional.get()), StandardCharsets.UTF_8);
                translations = new Gson().fromJson(content, new TypeToken<Map<String, String>>() {
                }.getType());
            } catch (Exception e) {
                McLark.LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    public static void reload() {
        translations = null;
        init();
    }

    public static String get(String key, Object... args) {
        try {
            String translation1 = translations.get(key);
            if (translation1 != null) {
                return String.format(translation1, args);
            } else {
                String key2 = key.replaceAll("mcbot.", "");
                String translation2 = Language.getInstance().getOrDefault(key2);
                if (!translation2.equals(key2)) {
                    return String.format(translation2, args);
                } else {
                    return "TranslateError{\"key\":\"" + key2 + "\",\"args\":" + Arrays.toString(args) + "}";
                }
            }
        } catch (Exception e) {
            return "TranslateError{\"key\":\"" + key + "\",\"args\":" + Arrays.toString(args) + "}";
        }
    }

    public static String get(String key) {
        String translation = translations.get(key);
        if (translation != null) {
            return translation;
        } else {
            return key;
        }
    }
}
