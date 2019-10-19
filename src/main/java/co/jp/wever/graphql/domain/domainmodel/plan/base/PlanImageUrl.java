package co.jp.wever.graphql.domain.domainmodel.plan.base;

import io.netty.util.internal.StringUtil;

public class PlanImageUrl {
    private String value;

    // TODO: サンプル画像のURLを入れる
    private final static String DEFAULT_IMAGE_URL = "http://defaultimage.png";

    // TODO: S3の画像URLのサイズを確認する
    private final static int MAX_URL_SIZE = 2048;

    private PlanImageUrl(String value) {
        this.value = value;
    }

    public static PlanImageUrl of(String value) throws IllegalArgumentException {
        //TODO: URL先の画像が存在するかどうかチェックしたい
        if (StringUtil.isNullOrEmpty(value)) {
            return new PlanImageUrl(DEFAULT_IMAGE_URL);
        }

        if (value.length() > MAX_URL_SIZE) {
            throw new IllegalArgumentException();
        }

        // TODO: S3にアップロードされているURLかチェックする

        return new PlanImageUrl(value);
    }

    public String getValue() {
        return value;
    }
}
