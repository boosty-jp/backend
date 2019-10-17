package co.jp.wever.graphql.domain.domainmodel.article.action;

public class ArticleUserAction {

    private boolean liked;
    private boolean bookmarked;
    private boolean learned;

    public ArticleUserAction(boolean liked, boolean bookmarked, boolean learned) {
        this.liked = liked;
        this.bookmarked = bookmarked;
        this.learned = learned;
    }
}
