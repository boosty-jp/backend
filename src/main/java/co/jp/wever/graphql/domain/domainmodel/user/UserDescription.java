package co.jp.wever.graphql.domain.domainmodel.user;

public class UserDescription {

    private String value;
    private final static int MAX_WORD_COUNT = 300;

    private UserDescription(String value) {
        this.value = value;
    }

    public static UserDescription of(String value) throws IllegalArgumentException {
        if (value.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new UserDescription(value);
    }

    public String getValue() {
        return value;
    }
}
