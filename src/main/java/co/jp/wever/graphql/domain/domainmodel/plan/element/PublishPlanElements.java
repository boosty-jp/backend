package co.jp.wever.graphql.domain.domainmodel.plan.element;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PublishPlanElements {

    private List<WritePlanElement> elements;
    private final static int MIN_ELEMENTS_SIZE = 1;
    private final static int MAX_ELEMENTS_SIZE = 30;

    private PublishPlanElements(List<WritePlanElement> elements) {
        this.elements = elements;
    }

    public static PublishPlanElements of(List<WritePlanElement> elements) {
        if (elements.size() < MIN_ELEMENTS_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_SHORTAGE.getString());
        }

        if (elements.size() > MAX_ELEMENTS_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_OVER.getString());
        }

        List<Integer> nums = elements.stream().map(e -> e.getNumber()).collect(Collectors.toList());
        if (nums.stream().filter(i -> Collections.frequency(nums, i) > 1).collect(Collectors.toSet()).size() > 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_NUMBER_DUPLICATED.getString());
        }

        List<String> ids = elements.stream().map(e -> e.getId()).collect(Collectors.toList());
        if (ids.stream().filter(i -> Collections.frequency(ids, i) > 1).collect(Collectors.toSet()).size() > 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.PLAN_ELEMENT_ID_DUPLICATED.getString());
        }

        return new PublishPlanElements(elements);
    }

    public List<WritePlanElement> getElements() {
        return elements;
    }
}
