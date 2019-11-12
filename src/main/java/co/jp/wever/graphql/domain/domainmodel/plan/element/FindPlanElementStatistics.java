package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class FindPlanElementStatistics {

    private long like;
    private long learned;

    public FindPlanElementStatistics(long like, long learned) {
        this.like = like;
        this.learned = learned;
    }

    public long getLike() {
        return like;
    }

    public long getLearned() {
        return learned;
    }

}
