package co.jp.wever.graphql.domain.domainmodel.article.statistics;

public class ArticleLikeCount {

    private long value;
    private final static long MIN_VALUE = 0;
    private final static long MAX_VALUE = 999_999_999;

    private ArticleLikeCount(long value) {
        this.value = value;
    }

    public static ArticleLikeCount of(long value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_VALUE) {
            throw new IllegalArgumentException();
        }

        return new ArticleLikeCount(value);
    }

    public long getValue() {
        return value;
    }
}
