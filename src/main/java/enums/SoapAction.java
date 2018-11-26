package enums;

public enum SoapAction {
    CHECK_TEXT("checkText", "CheckTextRequest"),
    CHECK_TEXTS("checkTexts", "CheckTextsRequest");

    public String method;
    public String reqName;

    SoapAction(String action, String reqName) {
        this.method = action;
        this.reqName = reqName;
    }
}
