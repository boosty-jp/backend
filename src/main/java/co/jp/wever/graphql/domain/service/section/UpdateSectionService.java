package co.jp.wever.graphql.domain.service.section;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.section.UpdateSectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.section.FindSectionRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.section.UpdateSectionRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UpdateSectionService {

    private final FindSectionRepositoryImpl findSectionRepository;
    private final UpdateSectionRepositoryImpl updateSectionRepository;

    public UpdateSectionService(
        FindSectionRepositoryImpl findSectionRepository, UpdateSectionRepositoryImpl updateSectionRepository) {
        this.findSectionRepository = findSectionRepository;
        this.updateSectionRepository = updateSectionRepository;
    }

    public void updateSection(String sectionId, UpdateSectionInput sectionInput, Requester requester) {
        log.info("update section id: {}", sectionInput.getId());
        log.info("update section number: {}", sectionInput.getNumber());
        log.info("update section text size: {}", sectionInput.getText().length());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findSectionRepository.findAuthorId(sectionId));
        UserId updaterId = UserId.of(requester.getUserId());

        if (!authorId.same(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        UpdateSection section = UpdateSectionConverter.toUpdateSection(sectionInput);

        updateSectionRepository.updateOne(section);
    }

    public void likeSection(String sectionId, Requester requester) {
        log.info("like sectionId: {}", sectionId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        updateSectionRepository.likeOne(sectionId, requester.getUserId());
    }

    public void deleteLikeSection(String sectionId, Requester requester) {
        log.info("delete sectionId: {}", sectionId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        updateSectionRepository.deleteLikeOne(sectionId, requester.getUserId());
    }
}

