package co.jp.wever.graphql.domain.converter.tag;

import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;

public class TagConverter {
    public static Tag toTag(TagEntity tagEntity) {
        return Tag.of(tagEntity.getTagId(), tagEntity.getName());
    }
}
