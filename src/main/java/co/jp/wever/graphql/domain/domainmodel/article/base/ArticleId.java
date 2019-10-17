package co.jp.wever.graphql.domain.domainmodel.article.base;

import io.netty.util.internal.StringUtil;

public class ArticleId {
    private String value;

    private ArticleId(String value) {
        this.value = value;
    }

    public static ArticleId of(String value) throws IllegalArgumentException {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }

        return new ArticleId(value);
    }

}
