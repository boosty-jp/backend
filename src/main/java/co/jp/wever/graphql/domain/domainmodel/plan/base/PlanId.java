package co.jp.wever.graphql.domain.domainmodel.plan.base;

import io.netty.util.internal.StringUtil;

public class PlanId {
    private String value;
    private PlanId(String value) {
        this.value = value;
    }

    public static PlanId of(String value) throws IllegalArgumentException {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }

        return new PlanId(value);
    }

    public String getValue() {
        return value;
    }
}
