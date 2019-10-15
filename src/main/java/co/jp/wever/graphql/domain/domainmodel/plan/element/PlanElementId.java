package co.jp.wever.graphql.domain.domainmodel.plan.element;

import io.netty.util.internal.StringUtil;

public class PlanElementId {
    private String value;

    private PlanElementId(String value) {
        this.value = value;
    }

    public static PlanElementId of(String value) throws IllegalArgumentException {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }

        return new PlanElementId(value);
    }

    public String getValue() {
        return value;
    }
}
