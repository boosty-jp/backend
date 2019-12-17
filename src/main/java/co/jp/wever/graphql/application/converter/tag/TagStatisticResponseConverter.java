package co.jp.wever.graphql.application.converter.tag;

import co.jp.wever.graphql.infrastructure.datamodel.tag.TagStatisticEntity;

public class TagStatisticResponseConverter {
    public static TagStatisticResponse toTagStatisticResponse(TagStatisticEntity tagStatisticEntity) {
        return TagStatisticResponse.builder()
                                   .id(tagStatisticEntity.getId())
                                   .name(tagStatisticEntity.getName())
                                   .related(tagStatisticEntity.getRelatedCount())
                                   .build();
    }
}
