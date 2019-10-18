package co.jp.wever.graphql.domain.domainmodel.tag;

import io.netty.util.internal.StringUtil;

public class TagId {
    private String value;

    private TagId(String value) {
        this.value = value;
    }

    public static TagId of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }

        return new TagId(value);
    }
}
