package jp.boosty.backend.domain.converter.tag;

import jp.boosty.backend.application.datamodel.request.tag.TagInput;
import jp.boosty.backend.domain.domainmodel.tag.Tag;
import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;

public class TagConverter {
    public static Tag toTag(TagEntity tagEntity) {
        return Tag.of(tagEntity.getId(), tagEntity.getName());
    }

    public static Tag toTag(TagInput tagInput) {
        return Tag.of(tagInput.getId(), tagInput.getName());
    }
}
