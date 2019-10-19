package co.jp.wever.graphql.domain.domainmodel.article.base;

public class Articletitle {

    private String value;
    private final static int MAX_WORD_COUNT = 100;

    private Articletitle(String value) {
        this.value = value;
    }

    public static Articletitle of(String value) throws IllegalArgumentException {
        if (value.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new Articletitle(value);
    }

    public String getValue() {
        return value;
    }
}
