package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class FindPlanElementAction {


    private boolean liked;
    private boolean learned;

    public FindPlanElementAction(boolean liked, boolean learned) {
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
