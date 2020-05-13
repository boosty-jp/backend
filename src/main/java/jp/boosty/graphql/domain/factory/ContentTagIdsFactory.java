package jp.boosty.graphql.domain.factory;

import java.util.List;
import java.util.stream.Collectors;

import jp.boosty.graphql.domain.domainmodel.content.ContentTags;
import jp.boosty.graphql.infrastructure.datamodel.tag.TagEntity;

public class ContentTagIdsFactory {
    public static ContentTags make(List<TagEntity> entities) {
        return ContentTags.of(entities.stream().map(e -> e.getId()).collect(Collectors.toList()));
    }
}
