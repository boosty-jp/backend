package co.jp.wever.graphql.domain.domainmodel.section;

public class SectionText {
    private final static int MIN_WORD_COUNT = 1;
    private final static int MAX_WORD_COUNT = 10000;
    private String value;

    private SectionText(String value) {
        this.value = value;
    }

    public static SectionText of(String value) {
        if (value.length() <  MIN_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new SectionText(value);
    }

    public String getValue() {
        return this.value;
    }
}
