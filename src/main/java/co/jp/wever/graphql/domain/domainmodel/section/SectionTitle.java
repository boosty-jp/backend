package co.jp.wever.graphql.domain.domainmodel.section;

public class SectionTitle {
    private final static int MIN_WORD_COUNT = 1;
    private final static int MAX_WORD_COUNT = 60;
    private String value;

    private SectionTitle(String value) {
        this.value = value;
    }

    public static SectionTitle of(String value) {
        if (value.length() < MIN_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new SectionTitle(value);
    }

    public String getValue() {
        return this.value;
    }
}
