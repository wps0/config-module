package pl.wieczorekp.configmodule;

public enum Language {
    ENGLISH("en"),
    POLISH("pl");

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

    public static Language fromStringCode(String str) {
        for (Language lang : Language.values() ) {
            if (lang.getId().equalsIgnoreCase(str))
                return lang;
        }
        return null;
    }
}