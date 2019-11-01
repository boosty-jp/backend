package co.jp.wever.graphql.domain.domainmodel.plan.base;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class PlanImageUrl {
    private String value;

    // TODO: S3の画像URLのサイズを確認する
    private final static int MAX_URL_SIZE = 2048;

    private PlanImageUrl(String value) {
        this.value = value;
    }

    public static PlanImageUrl of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new PlanImageUrl("");
        }

        if (value.length() > MAX_URL_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_IMAGE_URL.getString());
        }

        // TODO: URL先の画像が存在するかどうかチェックしたい
        // TODO: S3にアップロードされているURLかチェックする

        return new PlanImageUrl(value);
    }

    public String getValue() {
        return value;
    }
}
