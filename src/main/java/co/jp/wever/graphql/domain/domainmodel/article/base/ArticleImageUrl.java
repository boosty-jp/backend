package co.jp.wever.graphql.domain.domainmodel.article.base;

import io.netty.util.internal.StringUtil;

public class ArticleImageUrl {

    private String value;

    private ArticleImageUrl(String value) {
        this.value = value;
    }

    public static ArticleImageUrl of(String value) throws IllegalArgumentException {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }

        return new ArticleImageUrl(value);
    }

    public String getValue() {
        return value;
    }
}
