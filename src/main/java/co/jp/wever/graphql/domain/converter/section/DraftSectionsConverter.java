package co.jp.wever.graphql.domain.converter.section;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.domainmodel.section.DraftSections;
import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;

public class DraftSectionsConverter {
    public static DraftSections toDraftSections(List<UpdateSectionInput> sectionInputs) {
        List<UpdateSection> updateSections =
            sectionInputs.stream().map(s -> UpdateSectionConverter.toUpdateSection(s)).collect(Collectors.toList());
        return DraftSections.of(updateSections);
    }
}
