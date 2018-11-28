import core.YandexSpellerApi;
import entity.YandexSpellerAnswer;
import enums.Language;
import enums.Message;
import enums.Options;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerConstants.PARAM_LANG;
import static core.YandexSpellerConstants.PARAM_TEXT;
import static core.YandexSpellerConstants.WORD_WITH_LEADING_DIGITS;
import static core.YandexSpellerConstants.YANDEX_SPELLER_API_URI;
import static enums.Message.EMPTY_ANSWER;
import static enums.Message.EXPECTED_CORRECT_WORD_ISNT_FOUND;
import static enums.Message.INCORRECT_ERROR_CODE;
import static enums.SimpleText.HOLIDAYS_RU;
import static enums.SimpleText.MOSCOW;
import static enums.SimpleText.MOTHER_EN;
import static enums.YandexSpellerAnswerCode.ERROR_CAPITALIZATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.stringContainsInOrder;

public class TestYandexSpellerJSON {

    @Test
    public void checkRussianWordIsCorrected() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with()
                                .language(Language.RU)
                                .text(HOLIDAYS_RU.getIncorVersion(), HOLIDAYS_RU.getIncorVersion())
                                .callApi());
        for (int i = 0; i < 2; i++) {
            assertThat(answers.get(i).get(0).word, equalTo(HOLIDAYS_RU.getIncorVersion()));
            assertThat(EXPECTED_CORRECT_WORD_ISNT_FOUND.toString(), answers.get(i).get(0).s.get(0), equalTo(HOLIDAYS_RU.getCorVersion()));
        }
    }

    @Test
    //feature doesn't work (test fails expectedly)
    public void checkCapitalizationIsCorrected() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with()
                                .language(Language.EN)
                                .text(MOSCOW.getIncorVersion())
                                .callApi());
        assertThat(EMPTY_ANSWER.toString(), answers.get(0).size(), greaterThan(0));
        assertThat(INCORRECT_ERROR_CODE.toString(), answers.get(0).get(0).code, equalTo(ERROR_CAPITALIZATION));
        assertThat(answers.get(0).get(0).word, equalTo(MOSCOW.getIncorVersion()));
        assertThat(EXPECTED_CORRECT_WORD_ISNT_FOUND.toString(), answers.get(0).get(0).s.get(0), equalTo(MOSCOW.getCorVersion()));
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
        assertThat(Message.OPTION_DOESNT_WORK.toString() + Options.IGNORE_CAPITALIZATION, answers.get(0), empty());
    }

    @Test
    //feature works incorrect (test fails expectedly)
    public void checkWrongLanguage() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with()
                                .language(Language.RU)
                                .text(MOTHER_EN.getIncorVersion())
                                .callApi());
        assertThat(Message.LANGUAGE_ISNT_APPLIED.toString(), answers.get(0), empty());
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
                .statusCode(describedAs(Message.WRONG_HTTP_STATUS_CODE.toString(), is(HttpStatus.SC_OK)))
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(MOTHER_EN.getIncorVersion(), MOTHER_EN.getCorVersion())),
                        describedAs(Message.INCORRECT_ERROR_CODE.toString(), containsString("\"code\":1"))))
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
                .body(describedAs(Message.OPTION_DOESNT_WORK.toString() + Options.IGNORE_DIGITS, equalTo("[[]]")));
    }
}
