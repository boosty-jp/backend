package co.jp.wever.graphql.domain.domainmodel.plan.base;

public class PlanTitle {
    private final static int MIN_WORD_COUNT = 1;
    private final static int MAX_WORD_COUNT = 60;
    private String value;

    private PlanTitle(String value) {
        this.value = value;
    }

    public static PlanTitle of(String title) {
        if (title.length() < MIN_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        if (title.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new PlanTitle(title);
    }

    public String getValue() {
        return this.value;
    }
}
