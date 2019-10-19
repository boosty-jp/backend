package co.jp.wever.graphql.domain.domainmodel.user;

public class UserUrl {
    private String value;

    private final static int MAX_URL_SIZE = 2048;

    private UserUrl(String value) {
        this.value = value;
    }

    public static UserUrl of(String value) throws IllegalArgumentException {
        if (value.length() > MAX_URL_SIZE) {
            throw new IllegalArgumentException();
        }

        // TODO: URL先のリンクが存在するかどうかチェックしたい
        // TODO: URL先のリンクが安全かどうかチェックしたい

        return new UserUrl(value);
    }

    public String getValue() {
        return value;
    }
}
