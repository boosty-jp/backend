package co.jp.wever.graphql.domain.domainmodel.article;

public class ArticleBlock {
    private ArticleBlockType type;
    private ArticleBlockData data;

    private ArticleBlock(ArticleBlockType type, ArticleBlockData data) {
        this.type = type;
        this.data = data;
    }

    public static ArticleBlock of(String type, String data) {
        return new ArticleBlock(ArticleBlockType.of(type), ArticleBlockData.of(data));
    }

    public ArticleBlockType getType() {
        return type;
    }

    public ArticleBlockData getData() {
        return data;
    }

}
