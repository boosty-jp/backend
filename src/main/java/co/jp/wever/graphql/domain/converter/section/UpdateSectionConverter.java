package co.jp.wever.graphql.domain.converter.section;

import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;

public class UpdateSectionConverter {
    public static UpdateSection toUpdateSection(UpdateSectionInput sectionInput) {
        return UpdateSection.of(sectionInput.getId(),
                                sectionInput.getTitle(),
                                sectionInput.getText(),
                                sectionInput.getNumber());
    }
}
