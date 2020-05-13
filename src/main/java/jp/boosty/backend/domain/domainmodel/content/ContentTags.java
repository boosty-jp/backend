package jp.boosty.backend.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.tag.TagName;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class ContentTags {
    private List<TagName> tags;
    private final static int MAX_TAG_SIZE = 5;

    public ContentTags(List<TagName> tags) {
        this.tags = tags;
    }

    public static ContentTags of(List<String> tags) {
        if (Objects.isNull(tags)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        if (tags.isEmpty()) {
            return new ContentTags(tags.stream().map(t -> TagName.of(t)).collect(Collectors.toList()));
        }

        if (tags.size() > MAX_TAG_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }


        boolean duplicated = (tags.size() != new HashSet<>(tags).size());

        if (duplicated) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.TAG_DUPLICATED.getString());
        }

        return new ContentTags(tags.stream().map(t -> TagName.of(t)).collect(Collectors.toList()));
    }

    public boolean hasValue(){
        return !tags.isEmpty();
    }

    public List<TagName> getTags() {
        return tags;
    }
}
