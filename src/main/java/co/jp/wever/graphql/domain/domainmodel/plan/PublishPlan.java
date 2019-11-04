package co.jp.wever.graphql.domain.domainmodel.plan;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.plan.base.publish.PublishPlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.element.WritePlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PublishPlanElements;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PublishPlan {
    private PublishPlanBase base;
    private PublishPlanElements elements;

    private PublishPlan(PublishPlanBase base, PublishPlanElements elements) {
        this.base = base;
        this.elements = elements;
    }

    public static PublishPlan of(PublishPlanBase base, PublishPlanElements elements) {
        if (elements.getElements().stream().anyMatch(e -> e.getId().equals(base.getId()))) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_SELF_ID.getString());
        }

        return new PublishPlan(base, elements);
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

    public List<WritePlanElement> getElements() {
        return elements.getElements();
    }

    public List<String> getTagIds() {
        return base.getTagIds();
    }
}
