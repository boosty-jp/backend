package co.jp.wever.graphql.domain.domainmodel.action;

public class ActionCount {

    private LearnedCount learnedCount;

    private LikeCount likeCount;

    public ActionCount(LearnedCount learnedCount, LikeCount likeCount) {
        this.learnedCount = learnedCount;
        this.likeCount = likeCount;
    }

    public LearnedCount getLearnedCount() {
        return learnedCount;
    }

    public LikeCount getLikeCount() {
        return likeCount;
    }
}
