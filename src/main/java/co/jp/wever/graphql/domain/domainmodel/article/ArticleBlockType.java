package co.jp.wever.graphql.domain.domainmodel.article;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class ArticleBlockType {
    private String value;
    private final static String HEADER = "header";
    private final static String PARAGRAPH = "paragraph";
    private final static String IMAGE_URL = "imageUrl";
    private final static String IMAGE = "image";
    private final static String CODE = "code";
    private final static String LIST = "list";
    private final static String EMBED = "embed";
    private final static String WARNING = "warning";
    private final static String TABLE = "table";
    private final static String QUOTE = "quote";
    private final static List<String> ALLOWED_LIST =
        Arrays.asList(HEADER, PARAGRAPH, IMAGE_URL, IMAGE, CODE, LIST, EMBED, WARNING, TABLE, QUOTE);

    private ArticleBlockType(String value) {
        this.value = value;
    }

    public static ArticleBlockType of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_ARTICLE_BLOCK.getString());
        }

        if (!ALLOWED_LIST.stream().anyMatch(b -> b.equals(value))) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_ARTICLE_BLOCK.getString());
        }
        return new ArticleBlockType(value);
    }

    public String getValue() {
        return value;
    }
}
