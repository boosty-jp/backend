package co.jp.wever.graphql.domain.domainmodel.test;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentDescription;
import co.jp.wever.graphql.domain.domainmodel.content.ContentTitle;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Test {
    private ContentTitle title;
    private ContentDescription description;
    private Questions questions;

    private Test(
        ContentTitle title, ContentDescription description, Questions questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    public static Test of(ContentTitle title, ContentDescription description, Questions questions) {
        if (Objects.isNull(title) || Objects.isNull(description) || Objects.isNull(questions)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        return new Test(title, description, questions);
    }

    public boolean canPublish() {
        return title.valid();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public Questions getQuestions() {
        return questions;
    }

}
