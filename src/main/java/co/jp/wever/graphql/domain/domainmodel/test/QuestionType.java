package co.jp.wever.graphql.domain.domainmodel.test;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class QuestionType {
   private String value;
    private final static String TEXT = "text";
    private final static String SELECT = "select";

    private QuestionType(String value) {
        this.value = value;
    }

    public static QuestionType of(String value) {
        if(StringUtil.isNullOrEmpty(value)){
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.EMPTY_ANSWER_TYPE.getString());
        }

        if (!value.equals(TEXT) && !value.equals(SELECT) ){
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INVALID_ANSWER_TYPE.getString());
        }

        return new QuestionType(value);
    }

    public boolean isText(){
        return value.equals(TEXT);
    }

    public boolean isSelect(){
        return value.equals(SELECT);
    }

    public String getValue() {
        return value;
    }
}
