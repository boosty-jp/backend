package co.jp.wever.graphql.domain.domainmodel.test.answer;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class TextAnswer {
    private Text answer;
    private boolean showCount;


    private TextAnswer(Text answer, boolean showCount) {
        this.answer = answer;
        this.showCount = showCount;
    }

    public static TextAnswer of(Text answer, boolean showCount){
        if(Objects.isNull(answer)){
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        return new TextAnswer(answer, showCount);
    }

    public String getAnswer() {
        return answer.getValue();
    }

    public boolean isShowCount() {
        return showCount;
    }

}
