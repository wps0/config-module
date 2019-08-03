package pl.wieczorekp.configmodule;

import org.jetbrains.annotations.Nullable;

public enum Language {
    ENGLISH("en"),
    POLISH("pl"),
    ANY("en");

    private String path;
    Language(String path) {
        this.path = path;
    }

    public String getId() {
        return path;
    }
    public String getPath() {
        return path + ".";
    }

    @Nullable
    public static Language fromStringCode(String str) {
        for (Language lang : Language.values() ) {
            if (lang.getId().equalsIgnoreCase(str))
                return lang;
        }
        return null;
    }
}
