package co.jp.wever.graphql.domain.service.section;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.section.SectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.repository.section.DeleteSectionRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.section.FindSectionRepositoryImpl;

@Service
public class DeleteSectionService {
    private final FindSectionRepositoryImpl findSectionRepository;
    private final DeleteSectionRepositoryImpl deleteSectionRepository;

    public DeleteSectionService(
        FindSectionRepositoryImpl findSectionRepository, DeleteSectionRepositoryImpl deleteSectionRepository) {
        this.findSectionRepository = findSectionRepository;
        this.deleteSectionRepository = deleteSectionRepository;
    }

    public void deleteSection(String articleId, String sectionId, String userId) throws IllegalAccessException {
        Section section = SectionConverter.toSection(findSectionRepository.findOne(sectionId));

        if (!section.canDelete(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        List<Section> sections = findSectionRepository.findAllOnArticle(articleId)
                                                      .stream()
                                                      .map(s -> SectionConverter.toSection(s))
                                                      .collect(Collectors.toList());
        List<String> decrementIds = sections.stream()
                                            .filter(s -> s.getNumber() > section.getNumber())
                                            .map(s -> s.getId().getValue())
                                            .collect(Collectors.toList());

        deleteSectionRepository.deleteOne(sectionId, userId, decrementIds);
    }
}
