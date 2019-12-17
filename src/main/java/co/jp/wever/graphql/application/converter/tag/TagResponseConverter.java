package co.jp.wever.graphql.application.converter.tag;

import co.jp.wever.graphql.application.datamodel.response.query.tag.TagResponse;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;

public class TagResponseConverter {
    public static TagResponse toTagResponse(Tag tag) {
        return TagResponse.builder().id(tag.getId().getValue()).name(tag.getName().getValue()).build();
    }

    public static TagResponse toTagResponse(TagEntity tag) {
        return TagResponse.builder().id(tag.getId()).name(tag.getName()).build();
    }

    public static TagResponse toTagResponseWithRelatedCount(TagEntity tag) {
        return TagResponse.builder().id(tag.getId()).name(tag.getName()).relatedCount(tag.getRelatedCount()).build();
    }
}
