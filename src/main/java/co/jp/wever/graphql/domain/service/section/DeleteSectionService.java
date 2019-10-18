package co.jp.wever.graphql.domain.service.section;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.converter.section.SectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.domain.domainmodel.user.User;
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

    public void deleteSection(String sectionId, String userId) throws IllegalAccessException {
        Section section = SectionConverter.toSection(findSectionRepository.findOne(sectionId));

        if (!section.canDelete(User.of(userId))) {
            throw new IllegalAccessException();
        }

        deleteSectionRepository.deleteOne(sectionId, userId);
    }
}
