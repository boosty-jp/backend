package jp.boosty.graphql.domain.converter.tag;

import jp.boosty.graphql.application.datamodel.request.tag.TagInput;
import jp.boosty.graphql.domain.domainmodel.tag.Tag;
import jp.boosty.graphql.infrastructure.datamodel.tag.TagEntity;

public class TagConverter {
    public static Tag toTag(TagEntity tagEntity) {
        return Tag.of(tagEntity.getId(), tagEntity.getName());
    }

    public static Tag toTag(TagInput tagInput) {
        return Tag.of(tagInput.getId(), tagInput.getName());
    }
}
