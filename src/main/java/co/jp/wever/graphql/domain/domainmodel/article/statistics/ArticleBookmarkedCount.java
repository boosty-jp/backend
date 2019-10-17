package co.jp.wever.graphql.domain.domainmodel.article.statistics;

public class ArticleBookmarkedCount {
    private int value;
    private final static int MIN_VALUE = 0;
    private final static int MAX_VALUE = 999_999_999;

    private ArticleBookmarkedCount(int value) {
        this.value = value;
    }

    public static ArticleBookmarkedCount of(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_VALUE) {
            throw new IllegalArgumentException();
        }

        return new ArticleBookmarkedCount(value);
    }
}


