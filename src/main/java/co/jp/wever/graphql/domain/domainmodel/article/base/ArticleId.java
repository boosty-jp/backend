package co.jp.wever.graphql.domain.domainmodel.article.base;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ArticleId {

    private String value;

    private ArticleId(String value) {
        this.value = value;
    }

    public static ArticleId of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return new ArticleId(value);
    }

    public String getValue() {
        return value;
    }

}
