package co.jp.wever.graphql.domain.domainmodel.tag;

import io.netty.util.internal.StringUtil;

public class TagName {

    private String value;

    private TagName(String value) {
        this.value = value;
    }

    public static TagName of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }

        return new TagName(value);
    }

    public String getValue() {
        return value;
    }
}
