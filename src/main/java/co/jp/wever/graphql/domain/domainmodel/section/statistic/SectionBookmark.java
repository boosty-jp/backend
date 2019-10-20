package co.jp.wever.graphql.domain.domainmodel.section.statistic;

public class SectionBookmark {
    private final static int MIN_NUMBER = 0;
    private int value;

    private SectionBookmark(int value) {
        this.value = value;
    }

    public static SectionBookmark of(int value) {
        if (value < MIN_NUMBER) {
            throw new IllegalArgumentException();
        }

        return new SectionBookmark(value);
    }

    public int getValue() {
        return value;
    }
}
