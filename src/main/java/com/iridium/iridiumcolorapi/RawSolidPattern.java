package com.iridium.iridiumcolorapi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawSolidPattern implements com.iridium.iridiumcolorapi.Pattern {

    Pattern pattern = Pattern.compile("#([a-fA-F0-9]{6})|&?#([A-Fa-f0-9]{6})");

    public String process(String string) {
        Matcher matcher = this.pattern.matcher(string);
        while (matcher.find()) {
            String color = matcher.group(1);
            if (color == null)
                color = matcher.group(2);
            string = string.replace(matcher.group(), "" + IridiumColorAPI.getColor(color));
        }
        return string;
    }
}