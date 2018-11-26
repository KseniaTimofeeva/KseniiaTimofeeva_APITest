package core;

import enums.SimpleText;

public class YandexSpellerConstants {

    public static final String YANDEX_SPELLER_BASE_URI = "https://speller.yandex.net/services/spellservice";
    public static final String YANDEX_SPELLER_API_URI = "https://speller.yandex.net/services/spellservice.json/checkTexts";
    public static final String YANDEX_SPELLER_HOST = "speller.yandex.net";

    public static final String PARAM_TEXT = "text";
    public static final String PARAM_LANG = "lang";
    public static final String PARAM_OPTIONS = "options";

    public static final String QUOTES = "\"";

    public static final String WORD_WITH_LEADING_DIGITS = "25" + SimpleText.MOTHER_EN.getCorVersion();

}
