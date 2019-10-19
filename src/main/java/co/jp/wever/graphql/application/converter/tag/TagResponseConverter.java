package co.jp.wever.graphql.application.converter.tag;

import co.jp.wever.graphql.application.datamodel.response.query.TagResponse;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;

public class TagResponseConverter {
    public static TagResponse toTagResponse(Tag tag) {
        return TagResponse.builder().id(tag.getId().getValue()).name(tag.getName().getValue()).build();
    }
}
