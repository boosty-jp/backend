package co.jp.wever.graphql.domain.domainmodel.plan.base.draft;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanDescription;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanId;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanImageUrl;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanTagIds;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanTitle;

public class DraftPlanBase {
    private PlanId id;
    private PlanTitle title;
    private PlanDescription description;
    private PlanImageUrl imageUrl;
    private PlanTagIds tags;


    private DraftPlanBase(
        PlanId id, PlanTitle title, PlanDescription description, PlanImageUrl imageUrl, PlanTagIds tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tags = tags;
    }

    public static DraftPlanBase of(String id, String title, String description, String imageUrl, List<String> tagIds) {
        PlanId planId = PlanId.of(id);
        PlanTitle planTitle = PlanTitle.of(title);
        PlanDescription planDescription = PlanDescription.of(description);
        PlanImageUrl planImageUrl = PlanImageUrl.of(imageUrl);
        PlanTagIds planTagIds = PlanTagIds.of(tagIds);

        return new DraftPlanBase(planId, planTitle, planDescription, planImageUrl, planTagIds);
    }

    public String getId() {
        return id.getValue();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public String getImageUrl() {
        return imageUrl.getValue();
    }

    public List<String> getTags() {
        return tags.getValue();
    }

}