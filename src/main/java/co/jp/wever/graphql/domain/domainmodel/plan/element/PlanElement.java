package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class PlanElement {

    private PlanElementId id;
    private PlanElementTitle title;
    private String imageUrl; //TODO: ドメイン化する
    private PlanElementNumber number;
    private PlanElementType elementType;

    public PlanElement(
        PlanElementId id,
        PlanElementTitle title,
        String imageUrl,
        PlanElementNumber number,
        PlanElementType elementType) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.number = number;
        this.elementType = elementType;
    }

    public int getNumber() {
        return number.getValue();
    }

    public String getId() {
        return id.getValue();
    }

    public String getElementType() {
        return elementType.getString();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
