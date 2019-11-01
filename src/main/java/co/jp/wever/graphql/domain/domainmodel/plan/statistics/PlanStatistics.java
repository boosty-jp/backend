package co.jp.wever.graphql.domain.domainmodel.plan.statistics;

public class PlanStatistics {
    private PlanLearnedCount learnedCount;
    private PlanLearningCount learningCount;
    private PlanLikeCount likeCount;

    public PlanStatistics(
        PlanLearnedCount learnedCount, PlanLearningCount learningCount, PlanLikeCount likeCount) {
        this.learnedCount = learnedCount;
        this.learningCount = learningCount;
        this.likeCount = likeCount;
    }

    public PlanLearnedCount getLearnedCount() {
        return learnedCount;
    }

    public PlanLearningCount getLearningCount() {
        return learningCount;
    }

    public PlanLikeCount getLikeCount() {
        return likeCount;
    }

}
