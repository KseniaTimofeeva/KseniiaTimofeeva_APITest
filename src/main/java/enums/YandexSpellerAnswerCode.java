package enums;

public enum YandexSpellerAnswerCode {
    ERROR_UNKNOWN_WORD("1"),
    ERROR_REPEAT_WORD("2"),
    ERROR_CAPITALIZATION("3"),
    ERROR_TOO_MANY_ERRORS("4");

    private String value;

    YandexSpellerAnswerCode(String value) {
        this.value = value;
    }
}
