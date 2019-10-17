package co.jp.wever.graphql.domain.domainmodel.plan.base;

import io.netty.util.internal.StringUtil;

public class PlanImageUrl {
    private String value;

    private PlanImageUrl(String value) {
        this.value = value;
    }

    public static PlanImageUrl of(String value) throws IllegalArgumentException {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }

        return new PlanImageUrl(value);
    }

    public String getValue() {
        return value;
    }
}
