package co.jp.wever.graphql.domain.converter.section;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.section.PublishSections;
import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;

public class PublishSectionsConverter {
    public static PublishSections toPublishSections(List<UpdateSectionInput> sectionInputs) {
        List<UpdateSection> updateSections =
            sectionInputs.stream().map(s -> UpdateSectionConverter.toUpdateSection(s)).collect(Collectors.toList());
        return PublishSections.of(updateSections);
    }
}
