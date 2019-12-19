package co.jp.wever.graphql.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.tag.TagId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ContentTagIds {
    private List<TagId> tagIds;
    private final static int MAX_TAG_SIZE = 5;

    public ContentTagIds(List<TagId> tagIds) {
        this.tagIds = tagIds;
    }

    public static ContentTagIds of(List<String> tagsIds) {
        if (Objects.isNull(tagsIds)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        if (tagsIds.size() > MAX_TAG_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }


        boolean duplicated = (tagsIds.size() == new HashSet<>(tagsIds).size());

        if (duplicated) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TAG_DUPLICATED.getString());
        }

        return new ContentTagIds(tagsIds.stream().map(t -> TagId.of(t)).collect(Collectors.toList()));
    }

    public List<TagId> getTagIds() {
        return tagIds;
    }
}
