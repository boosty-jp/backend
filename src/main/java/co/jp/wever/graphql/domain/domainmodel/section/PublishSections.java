package co.jp.wever.graphql.domain.domainmodel.section;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class PublishSections {
    private List<UpdateSection> UpdateSections;
    private static final int MIN_SECTION_SIZE = 1;
    private static final int MAX_SECTION_SIZE = 30;

    private PublishSections(List<UpdateSection> updateSections) {
        this.UpdateSections = updateSections;
    }

    public static PublishSections of(List<UpdateSection> updateSections) {
        if (updateSections.size() < MIN_SECTION_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_EMPTY.getString());
        }

        if (updateSections.size() > MAX_SECTION_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_SIZE_OVER.getString());
        }

        return new PublishSections(updateSections);
    }

    public List<UpdateSection> getUpdateSections() {
        return UpdateSections;
    }

}
