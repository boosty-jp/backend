package co.jp.wever.graphql.domain.domainmodel.user;

public class UserDisplayName {

    private String value;
    private final static int MAX_WORD_COUNT = 60;

    private UserDisplayName(String value) {
        this.value = value;
    }

    public static UserDisplayName of(String value) throws IllegalArgumentException {
        if (value.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new UserDisplayName(value);
    }

    public String getValue() {
        return value;
    }
}
