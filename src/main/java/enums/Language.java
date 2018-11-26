package enums;

public enum Language {
    RU("ru"),
    UK("uk"),
    EN("en");

    private String languageCode;

    private Language(String lang) {
        this.languageCode = lang;
    }

    public String langCode() {
        return languageCode;
    }
}
