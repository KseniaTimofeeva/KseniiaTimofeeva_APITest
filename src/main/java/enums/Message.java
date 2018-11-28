package enums;

public enum Message {
    INCORRECT_ERROR_CODE("Error code isn't right"),
    EXPECTED_CORRECT_WORD_ISNT_FOUND("Expected correct word isn't contained"),
    EMPTY_ANSWER("Answer is empty"),
    OPTION_DOESNT_WORK("Option doesn't work: "),
    LANGUAGE_ISNT_APPLIED("Language wasn't applied by service"),
    WRONG_HTTP_STATUS_CODE("Wrong http status code"),;

    private String message;

    Message(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
