package co.jp.wever.graphql.domain.domainmodel.test.explanation;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentText;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Explanation {
    private ContentText text;
    private ReferenceIds referenceIds;

    private Explanation(
        ContentText text, ReferenceIds referenceIds) {
        this.text = text;
        this.referenceIds = referenceIds;
    }

    public static Explanation of(ContentText text, ReferenceIds referenceIds) {
        if (Objects.isNull(text) || Objects.isNull(referenceIds)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        return new Explanation(text, referenceIds);
    }

    public List<String> getReferenceIds() {
        return referenceIds.getReferenceIds();
    }

    public ContentText getText() {
        return text;
    }
}
