package co.jp.wever.graphql.domain.service.section;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.converter.section.SectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.section.CreateSectionRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.section.FindSectionRepositoryImpl;

@Service
public class CreateSectionService {

    private final FindSectionRepositoryImpl findSectionRepository;
    private final CreateSectionRepositoryImpl createSectionRepository;

    public CreateSectionService(
        FindSectionRepositoryImpl findSectionRepository, CreateSectionRepositoryImpl createSectionRepository) {
        this.findSectionRepository = findSectionRepository;
        this.createSectionRepository = createSectionRepository;
    }

    public String createSection(String userId, String articleId, SectionInput sectionInput) {
        Section section = SectionConverter.toSection(sectionInput, userId);
        List<Section> sections = findSectionRepository.findAllOnArticle(articleId)
                                                      .stream()
                                                      .map(s -> SectionConverter.toSection(s))
                                                      .collect(Collectors.toList());
        List<String> incrementIds = sections.stream()
                                            .filter(s -> s.getNumber() >= sectionInput.getNumber())
                                            .map(s -> s.getId().getValue())
                                            .collect(Collectors.toList());

        return createSectionRepository.addOne(userId,
                                              articleId,
                                              SectionEntityConverter.toSectionEntity(section),
                                              incrementIds);
    }
}
