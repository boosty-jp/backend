package jp.boosty.backend.domain.domainmodel.book;

import jp.boosty.backend.domain.GraphQLCustomException;

import org.springframework.http.HttpStatus;

import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

import io.netty.util.internal.StringUtil;

public class BookTargetDescription {
    private String description;
    private final static int MAX_WORD_COUNT = 50;

    private BookTargetDescription(String description) {
        this.description = description;
    }

    public static BookTargetDescription of(String description){
        if(StringUtil.isNullOrEmpty(description)){
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_TARGET_DESCRIPTION_WORD.getString());
        }

        if(description.length()  > MAX_WORD_COUNT){
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.OVER_TARGET_DESCRIPTION_WORD.getString());
        }

        return new BookTargetDescription(description);
    }

    public String getDescription() {
        return description;
    }

}
