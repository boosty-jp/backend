package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class FindPlanElementDetail {

    private PlanElementId id;
    private PlanElementTitle title;
    private PlanElementImageUrl imageUrl;
    private PlanElementType type;
    private PlanElementNumber number;
    private FindPlanElementStatistics statistics;
    private FindPlanElementAction action;

    public FindPlanElementDetail(
        PlanElementId id,
        PlanElementTitle title,
        PlanElementImageUrl imageUrl,
        PlanElementType type,
        PlanElementNumber number,
        FindPlanElementStatistics statistics,
        FindPlanElementAction action) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.type = type;
        this.number = number;
        this.statistics = statistics;
        this.action = action;
    }
    public String getId() {
        return id.getValue();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getImageUrl() {
        return imageUrl.getValue();
    }

    public String getType() {
        return type.getString();
    }

    public int getNumber() {
        return number.getValue();
    }

    public FindPlanElementStatistics getStatistics() {
        return statistics;
    }

    public FindPlanElementAction getAction() {
        return action;
    }
}
