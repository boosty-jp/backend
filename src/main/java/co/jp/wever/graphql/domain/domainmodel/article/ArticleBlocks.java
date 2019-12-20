package co.jp.wever.graphql.domain.domainmodel.article;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ArticleBlocks {
    private List<ArticleBlock> blocks;
    private final static int MAX_SIZE = 50;

    private ArticleBlocks(List<ArticleBlock> blocks) {
        this.blocks = blocks;
    }

    public static ArticleBlocks of(List<ArticleBlock> blocks) {
        if (blocks.size() > MAX_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TOO_LONG_ARTICLE_BLOCK.getString());
        }

        return new ArticleBlocks(blocks);
    }

    public List<ArticleBlock> getBlocks() {
        return blocks;
    }

}
