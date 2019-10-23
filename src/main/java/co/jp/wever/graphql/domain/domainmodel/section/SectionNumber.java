package co.jp.wever.graphql.domain.domainmodel.section;

public class SectionNumber {
    private final static int MIN_NUMBER = 1;
    private final static int MAX_NUMBER = 100;
    private int value;

    private SectionNumber(int value) {
        this.value = value;
    }

    public static SectionNumber of(int value) {
        if (value < MIN_NUMBER) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_NUMBER) {
            throw new IllegalArgumentException();
        }

        return new SectionNumber(value);
    }

    public int getValue() {
        return value;
    }

    public boolean isSame(SectionNumber target) {
        return target.value == value;
    }
}
