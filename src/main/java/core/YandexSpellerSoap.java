package core;

import enums.Language;
import enums.SoapAction;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static core.YandexSpellerConstants.PARAM_LANG;
import static core.YandexSpellerConstants.PARAM_OPTIONS;
import static core.YandexSpellerConstants.PARAM_TEXT;
import static core.YandexSpellerConstants.QUOTES;
import static core.YandexSpellerConstants.YANDEX_SPELLER_BASE_URI;
import static core.YandexSpellerConstants.YANDEX_SPELLER_HOST;

public class YandexSpellerSoap {
    private static RequestSpecification spellerSOAPreqSpec = new RequestSpecBuilder()
            .addHeader("Accept-Encoding", "gzip,deflate")
            .setContentType("text/xml;charset=UTF-8")
            .addHeader("Host", YANDEX_SPELLER_HOST)
            .setBaseUri(YANDEX_SPELLER_BASE_URI)
            .build();

    //builder pattern
    private YandexSpellerSoap() {
    }

    private SoapAction action = SoapAction.CHECK_TEXTS;
    private HashMap<String, List<String>> params = new HashMap<>();

    public static class SOAPBuilder {
        YandexSpellerSoap soapReq;

        private SOAPBuilder(YandexSpellerSoap soap) {
            this.soapReq = soap;
        }

        public YandexSpellerSoap.SOAPBuilder text(String... texts) {
            List<String> textValues = new ArrayList<>();
            textValues.addAll(Arrays.asList(texts));
            soapReq.params.put(PARAM_TEXT, textValues);
            return this;
        }

        public YandexSpellerSoap.SOAPBuilder options(String option) {
            soapReq.params.put(PARAM_OPTIONS, Collections.singletonList(option));
            return this;
        }

        public YandexSpellerSoap.SOAPBuilder language(Language language) {
            soapReq.params.put(PARAM_LANG, Collections.singletonList(language.langCode()));
            return this;
        }

        public Response callSOAP() {
            String soapBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spel=\"http://speller.yandex.net/services/spellservice\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <spel:" + soapReq.action.reqName + " lang=" + QUOTES + (soapReq.params.getOrDefault(PARAM_LANG, Arrays.asList("en"))).get(0) + QUOTES
                    + " options=" + QUOTES + (soapReq.params.getOrDefault(PARAM_OPTIONS, Arrays.asList("0"))).get(0) + QUOTES
                    + " format=\"\">\n";

            for (String str : soapReq.params.getOrDefault(PARAM_TEXT, Collections.emptyList())) {
                soapBody += "         <spel:text>" + (str) + "</spel:text>\n";

            }
            soapBody += "      </spel:" + soapReq.action.reqName + ">\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            return RestAssured.with()
                    .spec(spellerSOAPreqSpec)
                    .header("SOAPAction", "http://speller.yandex.net/services/spellservice/" + soapReq.action.method)
                    .body(soapBody)
                    .log().all().with()
                    .post().prettyPeek();
        }
    }

    public static SOAPBuilder with() {
        core.YandexSpellerSoap soap = new YandexSpellerSoap();
        return new SOAPBuilder(soap);
    }
}
