package co.jp.wever.graphql.domain.domainmodel.article;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ArticleBlockData {
    private String value;
    // 1ブロック10000文字入れられる+文字の装飾を考慮し3倍まで許容する
    private final static int MAX_SIZE = 30000;

    private ArticleBlockData(String value) {
        this.value = value;
    }

    public static ArticleBlockData of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_ARTICLE_BLOCK.getString());
        }

        if(value.length() > MAX_SIZE){
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TOO_LONG_ARTICLE_BLOCK.getString());
        }

        //TODO: JSONチェック + タグごとのセキュリティチェックを行う(パフォーマンス次第だが)

        return new ArticleBlockData(value);
    }

    public String getValue() {
        return value;
    }
}
