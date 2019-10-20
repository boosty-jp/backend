package co.jp.wever.graphql.domain.domainmodel.section.statistic;


public class SectionLike {
    private final static int MIN_NUMBER = 0;
    private int value;

    private SectionLike(int value) {
        this.value = value;
    }

    public static SectionLike of(int value) {
        if (value < MIN_NUMBER) {
            throw new IllegalArgumentException();
        }

        return new SectionLike(value);
    }

    public int getValue() {
        return value;
    }
}
