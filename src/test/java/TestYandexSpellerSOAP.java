import core.YandexSpellerSoap;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

import static enums.SimpleText.MOSCOW;
import static enums.SimpleText.MOTHER_EN;
import static enums.SimpleText.TEXT_EXAMPLE;

public class TestYandexSpellerSOAP {
    @Test
    public void checkWrongWordIsCorrected() {
        YandexSpellerSoap
                .with()
                .text(MOTHER_EN.getIncorVersion())
                .callSOAP()
                .then()
                .assertThat()
                .body(Matchers.stringContainsInOrder
                        (Arrays.asList(MOTHER_EN.getIncorVersion(), MOTHER_EN.getCorVersion())));
    }

    @Test
    public void checkUnknownWordErrorCode() {
        YandexSpellerSoap
                .with()
                .text(MOTHER_EN.getIncorVersion())
                .callSOAP()
                .then()
                .assertThat()
                .body(Matchers.containsString("code=\"1\""));
    }

    @Test
    public void checkTextsAreCorrected() {
        YandexSpellerSoap
                .with()
                .text(TEXT_EXAMPLE.getIncorVersion())
                .callSOAP()
                .then()
                .assertThat()
                .body(Matchers.stringContainsInOrder
                        (Arrays.asList(TEXT_EXAMPLE.splitIncorrText()[0], TEXT_EXAMPLE.splitCorrText()[0],
                                TEXT_EXAMPLE.splitIncorrText()[1], TEXT_EXAMPLE.splitCorrText()[1],
                                TEXT_EXAMPLE.splitIncorrText()[2], TEXT_EXAMPLE.splitCorrText()[2])));
    }

    @Test
    public void checkWrongCapitalizationIsCorrected() {
        YandexSpellerSoap
                .with()
                .text(MOSCOW.getIncorVersion())
                .callSOAP()
                .then()
                .assertThat()
                .body(Matchers.stringContainsInOrder
                        (Arrays.asList(MOSCOW.getIncorVersion(), MOSCOW.getCorVersion())));
    }

    @Test
    public void checkUpperCaseWord() {
        YandexSpellerSoap
                .with()
                .text(MOTHER_EN.getIncorVersion().toUpperCase())
                .callSOAP()
                .then()
                .assertThat()
                .body(Matchers.stringContainsInOrder
                        (Arrays.asList(MOTHER_EN.getIncorVersion().toUpperCase(), MOTHER_EN.getCorVersion().toUpperCase())));
    }

}