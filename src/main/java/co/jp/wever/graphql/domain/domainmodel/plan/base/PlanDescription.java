package co.jp.wever.graphql.domain.domainmodel.plan.base;

public class PlanDescription {
    private final static int MIN_WORD_COUNT = 0;
    private final static int MAX_WORD_COUNT = 10000;
    private String value;

    private PlanDescription(String value) {
        this.value = value;
    }

    public static PlanDescription of(String description) {
        if (description.length() < MIN_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        if (description.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new PlanDescription(description);
    }

    public String getValue() {
        return this.value;
    }
}
