package co.jp.wever.graphql.domain.domainmodel.article.statistics;

public class ArticleLikeCount {
    private int value;
    private final static int MIN_VALUE = 0;
    private final static int MAX_VALUE = 999_999_999;

    private ArticleLikeCount(int value) {
        this.value = value;
    }

    public static ArticleLikeCount of(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_VALUE) {
            throw new IllegalArgumentException();
        }

        return new ArticleLikeCount(value);
    }
}
