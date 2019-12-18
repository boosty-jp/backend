package co.jp.wever.graphql.domain.domainmodel.article.base;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ArticleTextUrl {
    private String value;

    // TODO: S3の画像URLのサイズを確認する
    private final static int MAX_URL_SIZE = 2048;

    private ArticleTextUrl(String value) {
        this.value = value;
    }

    public static ArticleTextUrl of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        if (value.length() > MAX_URL_SIZE) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return new ArticleTextUrl(value);
    }

    public String getValue() {
        return value;
    }
}
