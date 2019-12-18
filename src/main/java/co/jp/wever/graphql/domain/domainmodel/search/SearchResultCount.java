package co.jp.wever.graphql.domain.domainmodel.search;

public class SearchResultCount {
    private int value;
    private final static int MIN_RESULT_COUNT = 20;
    private final static int MAX_RESULT_COUNT = 100;

    private SearchResultCount(int value) {
        this.value = value;
    }

    public static SearchResultCount of(int value) {
        if (value < MIN_RESULT_COUNT) {
            value = MIN_RESULT_COUNT;
        }
        if (value > MAX_RESULT_COUNT) {
            value = MAX_RESULT_COUNT;
        }
        return new SearchResultCount(value);
    }

    public int getValue() {
        return value;
    }

}