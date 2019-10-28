package co.jp.wever.graphql.domain.domainmodel.user;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class UserImageUrl {
    private String value;

    // TODO: サンプル画像のURLを入れる
    private final static String DEFAULT_IMAGE_URL = "http://defaultimage.png";

    // TODO: S3の画像URLのサイズを確認する
    private final static int MAX_URL_SIZE = 2048;

    private UserImageUrl(String value) {
        this.value = value;
    }

    public static UserImageUrl of(String value) {
        //TODO: URL先の画像が存在するかどうかチェックしたい
        if (StringUtil.isNullOrEmpty(value)) {
            return new UserImageUrl(DEFAULT_IMAGE_URL);
        }

        if (value.length() > MAX_URL_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_IMAGE_URL.getString());
        }

        // TODO: S3にアップロードされているURLかチェックする

        return new UserImageUrl(value);
    }

    public String getValue() {
        return value;
    }
}
