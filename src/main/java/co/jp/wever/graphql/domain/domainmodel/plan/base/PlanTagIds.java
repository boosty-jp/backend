package co.jp.wever.graphql.domain.domainmodel.plan.base;

import java.util.List;

public class PlanTagIds {
    private List<String> value;
    private final static int MAX_TAG_COUNT = 5;

    private PlanTagIds(List<String> value) {
        this.value = value;
    }

    public static PlanTagIds of(List<String> value) throws IllegalArgumentException {
        if (value.size() > MAX_TAG_COUNT) {
            throw new IllegalArgumentException();
        }

        return new PlanTagIds(value);
    }

    public List<String> getValue() {
        return value;
    }
}
