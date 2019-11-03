package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class FindPlanElement {
    private PlanElementId id;
    private PlanElementNumber number;
    private PlanElementTitle title;
    private PlanElementType type;
    private PlanElementImageUrl imageUrl;

    public FindPlanElement(
        PlanElementId id,
        PlanElementNumber number,
        PlanElementTitle title,
        PlanElementType type,
        PlanElementImageUrl imageUrl) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id.getValue();
    }

    public int getNumber() {
        return number.getValue();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getType() {
        return type.getString();
    }

    public String getImageUrl() {
        return imageUrl.getValue();
    }
}
