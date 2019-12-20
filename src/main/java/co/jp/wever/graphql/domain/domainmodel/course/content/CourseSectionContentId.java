package co.jp.wever.graphql.domain.domainmodel.course.content;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class CourseSectionContentId {
    private String value;

    private CourseSectionContentId(String value) {
        this.value = value;
    }

    public static CourseSectionContentId of(String value){
        if(StringUtil.isNullOrEmpty(value)){
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SECTION_CONTENT.getString());
        }

        return new CourseSectionContentId(value);
    }

    public String getValue() {
        return value;
    }
}
