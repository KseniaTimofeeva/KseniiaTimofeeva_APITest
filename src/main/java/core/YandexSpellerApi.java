package core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import entity.YandexSpellerAnswer;
import entity.YandexSpellerAnswerMultiText;
import enums.Language;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static core.YandexSpellerConstants.PARAM_LANG;
import static core.YandexSpellerConstants.PARAM_OPTIONS;
import static core.YandexSpellerConstants.PARAM_TEXT;
import static core.YandexSpellerConstants.YANDEX_SPELLER_API_URI;
import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerApi {

    private YandexSpellerApi() {

    }

    private HashMap<String, List<String>> textParams = new HashMap<>();
    private HashMap<String, String> params = new HashMap<>();

    public static class ApiBuilder {

        YandexSpellerApi spellerApi;

        private ApiBuilder(YandexSpellerApi gcApi) {
            spellerApi = gcApi;
        }

        public ApiBuilder text(String... texts) {
            spellerApi.textParams.put(PARAM_TEXT, Arrays.asList(texts));
            return this;
        }

        public ApiBuilder options(int... options) {
            int optionSum = 0;
            for (int optn : options) {
                optionSum += optn;
            }
            spellerApi.params.put(PARAM_OPTIONS, String.valueOf(optionSum));
            return this;
        }

        public ApiBuilder language(Language language) {
            spellerApi.params.put(PARAM_LANG, language.langCode());
            return this;
        }

        public Response callApi() {
            return RestAssured.with()
                    .queryParams(spellerApi.params)
                    .queryParams(spellerApi.textParams)
                    .log().all()
                    .get(YANDEX_SPELLER_API_URI).prettyPeek();
        }
    }

    public static ApiBuilder with() {
        YandexSpellerApi api = new YandexSpellerApi();
        return new ApiBuilder(api);
    }

    //get ready Speller answers list form api response
    public static List<YandexSpellerAnswerMultiText> getYandexSpellerAnswers(Response response) {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();

        JsonArray answerArray = parser.parse(response.asString().trim()).getAsJsonArray();

        List<YandexSpellerAnswerMultiText> result = new ArrayList<>();

        for (JsonElement array : answerArray) {
            YandexSpellerAnswerMultiText list = new YandexSpellerAnswerMultiText();

            if (array instanceof JsonArray) {
                for (JsonElement jsonElement : (JsonArray) array) {
                    list.answerList.add(gson.fromJson(jsonElement, new TypeToken<YandexSpellerAnswer>() {
                    }.getType()));

                }
                result.add(list);
            }
        }
        return result;

    }

    //    set base request and response specifications to use in tests
    public static ResponseSpecification successResponse() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

}
