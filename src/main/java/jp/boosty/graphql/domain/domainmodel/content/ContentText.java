package jp.boosty.graphql.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ContentText {
    private String value;
    // 1ブロック1000文字入れられる+文字の装飾を考慮し3倍まで許容する
    private final static int MAX_WORD_COUNT = 3000;

    private ContentText(String value) {
        this.value = value;
    }

    public static ContentText of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new ContentText("");
        }

        if (value.length() > MAX_WORD_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.CONTENT_TEXT_OVER.getString());
        }

        return new ContentText(value);
    }

    public String getValue() {
        return value;
    }
}
