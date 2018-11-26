package enums;

public enum SimpleText {

    MOTHER_EN("mother", "motherr"),
    HOLIDAYS_RU("каникулы", "коникулы"),
    MOSCOW("Moscow", "moscow"),
    TEXT_EXAMPLE("Mother father brother", "Motherr fatther broother");

    private String corVersion;
    private String incorVersion;

    SimpleText(String corVersion, String incorVersion) {
        this.corVersion = corVersion;
        this.incorVersion = incorVersion;
    }

    public String getCorVersion() {
        return corVersion;
    }

    public String getIncorVersion() {
        return incorVersion;
    }

    public String[] splitCorrText() {
        return this.getCorVersion().split(" ");
    }

    public String[] splitIncorrText() {
        return this.getIncorVersion().split(" ");
    }
}
