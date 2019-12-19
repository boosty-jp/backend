package co.jp.wever.graphql.domain.domainmodel.article;

import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ArticleTextUrl {
    private String value;

    // TODO: S3の画像URLのサイズを確認する(2048は一般的なURLのサイズより設定)
    private final static int MAX_URL_SIZE = 2048;
    private final static Pattern URL_PATTERN = Pattern.compile("^https://(.png|.jpg|.jpeg)$");

    private ArticleTextUrl(String value) {
        this.value = value;
    }

    public static ArticleTextUrl of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new ArticleTextUrl("");
        }

        if (value.length() > MAX_URL_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_IMAGE_URL.getString());
        }

        if (URL_PATTERN.matcher(value).find()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_IMAGE_URL.getString());
        }

        return new ArticleTextUrl(value);
    }

    public String getValue() {
        return value;
    }

    public boolean valid() {
        return !value.isEmpty();
    }
}