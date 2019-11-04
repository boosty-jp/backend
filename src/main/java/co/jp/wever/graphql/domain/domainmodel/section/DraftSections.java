package co.jp.wever.graphql.domain.domainmodel.section;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class DraftSections {
    private List<UpdateSection> updateSections;
    private static final int MAX_SECTION_SIZE = 30;

    private DraftSections(List<UpdateSection> findSections) {
        this.updateSections = findSections;
    }

    public static DraftSections of(List<UpdateSection> findSections) {
        if (findSections.size() > MAX_SECTION_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_SIZE_OVER.getString());
        }

        return new DraftSections(findSections);
    }

    public List<UpdateSection> getUpdateSections() {
        return updateSections;
    }

}
