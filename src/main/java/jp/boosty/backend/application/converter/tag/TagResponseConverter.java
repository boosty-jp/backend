package jp.boosty.backend.application.converter.tag;

import jp.boosty.backend.application.datamodel.response.query.tag.TagResponse;
import jp.boosty.backend.domain.domainmodel.tag.Tag;
import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;

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
