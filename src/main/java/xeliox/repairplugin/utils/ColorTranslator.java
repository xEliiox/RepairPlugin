package xeliox.repairplugin.utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.jetbrains.annotations.NotNull;

public class ColorTranslator {

    public static @NotNull String translate(String message) {
        return IridiumColorAPI.process(message);
    }
}
