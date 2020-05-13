package jp.boosty.backend.domain.domainmodel.page;

import jp.boosty.backend.domain.domainmodel.content.ContentTitle;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class Page {
    private ContentTitle title;
    private PageText text;

    private Page(
        ContentTitle title, PageText text) {
        this.title = title;
        this.text = text;
    }

    public static Page of(ContentTitle title, PageText text ) {
        if (Objects.isNull(title) || Objects.isNull(text)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        return new Page(title, text);
    }

    public ContentTitle getTitle() {
        return title;
    }

    public PageText getText() {
        return text;
    }
}
