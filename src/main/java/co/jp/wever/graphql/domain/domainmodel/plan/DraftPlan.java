package co.jp.wever.graphql.domain.domainmodel.plan;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.plan.base.draft.DraftPlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElements;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class DraftPlan {
    private DraftPlanBase base;
    private PlanElements elements;

    private DraftPlan(DraftPlanBase base, PlanElements elements) {
        this.base = base;
        this.elements = elements;
    }

    public static DraftPlan of(DraftPlanBase base, PlanElements elements){
        if(elements.getElements().stream().anyMatch(e -> e.getId().equals(base.getId()))){
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_SELF_ID.getString());
        }

        return new DraftPlan(base, elements);
    }

    public String getId() {
        return base.getId();
    }

    public String getTitle() {
        return base.getTitle();
    }

    public String getDescription() {
        return base.getDescription();
    }

    public String getImageUrl() {
        return base.getImageUrl();
    }

    public List<PlanElement> getElements() {
        return elements.getElements();
    }

    public List<String> getTagIds() {
        return base.getTags();
    }
}
