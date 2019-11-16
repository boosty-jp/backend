package co.jp.wever.graphql.domain.domainmodel.plan.element;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class PlanElementImageUrl {

    private String value;

    // TODO: S3の画像URLのサイズを確認する
    private final static int MAX_URL_SIZE = 2048;

    private PlanElementImageUrl(String value) {
        this.value = value;
    }

    public static PlanElementImageUrl of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new PlanElementImageUrl("");
        }

        if (value.length() > MAX_URL_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_IMAGE_URL.getString());
        }

        // TODO: URL先の画像が存在するかどうかチェックしたい
        // TODO: S3にアップロードされているURLかチェックする

        return new PlanElementImageUrl(value);
    }

    public String getValue() {
        return value;
    }}
