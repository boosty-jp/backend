package co.jp.wever.graphql.domain.domainmodel.course.content;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class CourseSectionContents {
    private List<CourseSectionContent> contents;
    private final static int MAX_CONTENT_SIZE = 10;

    private CourseSectionContents(List<CourseSectionContent> contents) {
        this.contents = contents;
    }

    public static CourseSectionContents of(List<CourseSectionContent> contents) {
        if (contents.size() > MAX_CONTENT_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_CONTENT_NUMBER_OVER.getString());
        }

        return new CourseSectionContents(contents);
    }

    public List<CourseSectionContent> getContents() {
        return contents;
    }
}
