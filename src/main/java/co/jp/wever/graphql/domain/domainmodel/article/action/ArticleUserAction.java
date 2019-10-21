package co.jp.wever.graphql.domain.domainmodel.article.action;

public class ArticleUserAction {

    private boolean liked;
    private boolean learned;

    public ArticleUserAction(boolean liked, boolean learned) {
        this.liked = liked;
        this.learned = learned;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isLearned() {
        return learned;
    }

}
