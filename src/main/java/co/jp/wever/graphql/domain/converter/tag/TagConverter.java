package co.jp.wever.graphql.domain.converter.tag;

import co.jp.wever.graphql.application.datamodel.request.tag.TagInput;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;

public class TagConverter {
    public static Tag toTag(TagEntity tagEntity) {
        return Tag.of(tagEntity.getId(), tagEntity.getName());
    }

    public static Tag toTag(TagInput tagInput) {
        return Tag.of(tagInput.getId(), tagInput.getName());
    }
}
