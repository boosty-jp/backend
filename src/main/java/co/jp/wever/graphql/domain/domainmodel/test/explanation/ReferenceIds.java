package co.jp.wever.graphql.domain.domainmodel.test.explanation;

import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ReferenceIds {
    private List<ReferenceId> referenceIds;
    private final static int MAX_REFERENCE_SIZE = 5;

    public ReferenceIds(List<ReferenceId> referenceIds) {
        this.referenceIds = referenceIds;
    }

    public static ReferenceIds of(List<String> referenceIds) {
        if (Objects.isNull(referenceIds)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        if (referenceIds.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        if (referenceIds.size() > MAX_REFERENCE_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.REFERENCE_OVER.getString());
        }


        boolean duplicated = (referenceIds.size() != new HashSet<>(referenceIds).size());

        if (duplicated) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.DUPLICATED_REFERENCE.getString());
        }

        return new ReferenceIds(referenceIds.stream().map(r -> ReferenceId.of(r)).collect(Collectors.toList()));
    }

    public List<String> getReferenceIds() {
        return referenceIds.stream().map(ReferenceId::getValue).collect(Collectors.toList());
    }
}
