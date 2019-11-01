package co.jp.wever.graphql.domain.domainmodel.plan.action;

public class PlanUserAction {
    private boolean liked;
    private boolean learning;
    private boolean learned;

    public PlanUserAction(boolean liked, boolean learning, boolean learned) {
        this.liked = liked;
        this.learning = learning;
        this.learned = learned;
    }

    public boolean isLearning() {
        return learning;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isLearned() {
        return learned;
    }

}
