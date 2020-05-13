package jp.boosty.graphql.application.converter.tag;

import jp.boosty.graphql.application.datamodel.response.query.tag.TagResponse;
import jp.boosty.graphql.domain.domainmodel.tag.Tag;
import jp.boosty.graphql.infrastructure.datamodel.tag.TagEntity;

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
