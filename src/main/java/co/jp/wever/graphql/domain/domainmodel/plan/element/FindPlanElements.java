package co.jp.wever.graphql.domain.domainmodel.plan.element;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class FindPlanElements {
    private List<FindPlanElement> elements;

    private final static int MAX_ELEMENTS_SIZE = 30;

    private FindPlanElements(List<FindPlanElement> elements) {
        this.elements = elements;
    }

    public static FindPlanElements of(List<FindPlanElement> elements) {
        if (elements.size() > MAX_ELEMENTS_SIZE) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_OVER.getString());
        }

        return new FindPlanElements(elements);
    }

    public List<FindPlanElement> getElements() {
        return elements;
    }

}
