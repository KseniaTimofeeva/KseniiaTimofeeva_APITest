import core.YandexSpellerApi;
import entity.YandexSpellerAnswer;
import enums.Language;
import enums.Options;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerConstants.PARAM_LANG;
import static core.YandexSpellerConstants.PARAM_TEXT;
import static core.YandexSpellerConstants.WORD_WITH_LEADING_DIGITS;
import static core.YandexSpellerConstants.YANDEX_SPELLER_API_URI;
import static enums.SimpleText.HOLIDAYS_RU;
import static enums.SimpleText.MOSCOW;
import static enums.SimpleText.MOTHER_EN;
import static enums.YandexSpellerAnswerCode.ERROR_CAPITALIZATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class TestYandexSpellerJSON {

    @Test
    public void checkRussianWordIsCorrected() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with()
                                .language(Language.RU)
                                .text(HOLIDAYS_RU.getIncorVersion(), HOLIDAYS_RU.getIncorVersion())
                                .callApi());
        assertThat(answers.get(0).get(0).word, equalTo(HOLIDAYS_RU.getIncorVersion()));
        assertThat(answers.get(0).get(0).s.get(0), equalTo(HOLIDAYS_RU.getCorVersion()));
        assertThat(answers.get(1).get(0).word, equalTo(HOLIDAYS_RU.getIncorVersion()));
        assertThat(answers.get(1).get(0).s.get(0), equalTo(HOLIDAYS_RU.getCorVersion()));
    }

    @Test
    public void checkCapitalizationIsCorrected() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with()
                                .language(Language.EN)
                                .text(MOSCOW.getIncorVersion())
                                .callApi());
        assertThat(answers.get(0).size(), Matchers.greaterThan(0));
        assertThat(answers.get(0).get(0).code, equalTo(ERROR_CAPITALIZATION));
        assertThat(answers.get(0).get(0).word, equalTo(MOSCOW.getIncorVersion()));
        assertThat(answers.get(0).get(0).s.get(0), equalTo(MOSCOW.getCorVersion()));
    }

    @Test
    public void checkOptionIgnoreCapitalization() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with()
                                .language(Language.EN)
                                .options(Options.IGNORE_CAPITALIZATION.getValue())
                                .text(MOSCOW.getIncorVersion())
                                .callApi());
        assertThat(answers.get(0), Matchers.empty());
    }

    @Test
    public void checkWrongLanguage() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with()
                                .language(Language.RU)
                                .text(MOTHER_EN.getIncorVersion())
                                .callApi());
        assertThat(answers.get(0), Matchers.empty());
    }


    @Test
    public void checkIncorrectWordIsCorrected() {
        RestAssured
                .given()
                .queryParam(PARAM_TEXT, MOTHER_EN.getIncorVersion())
                .params(PARAM_LANG, Language.EN)
                .accept(ContentType.JSON)
                .and()
                .log().everything()
                .when()
                .get(YANDEX_SPELLER_API_URI)
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.allOf(
                        Matchers.stringContainsInOrder(Arrays.asList(MOTHER_EN.getIncorVersion(), MOTHER_EN.getCorVersion())),
                        Matchers.containsString("\"code\":1")))
                .contentType(ContentType.JSON)
                .time(lessThan(20000L)); // Milliseconds
    }

    @Test
    public void checkOptionIgnoreDigitsWorks() {
        YandexSpellerApi.with()
                .language(Language.EN)
                .options(Options.IGNORE_DIGITS.getValue())
                .text(WORD_WITH_LEADING_DIGITS)
                .callApi()
                .then().specification(YandexSpellerApi.successResponse())
                .assertThat()
                .body(Matchers.equalTo("[[]]"));
    }
}
