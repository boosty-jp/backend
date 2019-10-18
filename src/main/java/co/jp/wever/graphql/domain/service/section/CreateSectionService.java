package co.jp.wever.graphql.domain.service.section;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.converter.section.SectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.section.CreateSectionRepositoryImpl;

@Service
public class CreateSectionService {


    private final CreateSectionRepositoryImpl createSectionRepository;

    public CreateSectionService(CreateSectionRepositoryImpl createSectionRepository) {
        this.createSectionRepository = createSectionRepository;
    }

    public String createSection(String userId, SectionInput sectionInput){
        Section section = SectionConverter.toSection(sectionInput, userId);

        return createSectionRepository.addOne(userId, SectionEntityConverter.toSectionEntity(section));
    }
}
