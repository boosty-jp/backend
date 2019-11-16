package co.jp.wever.graphql.domain.service.tag;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.tag.CreateTagRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.tag.FindTagRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateTagService {
    private final FindTagRepositoryImpl findTagRepository;
    private final CreateTagRepositoryImpl createTagRepository;

    public CreateTagService(
        FindTagRepositoryImpl findTagRepository, CreateTagRepositoryImpl createTagRepository) {
        this.findTagRepository = findTagRepository;
        this.createTagRepository = createTagRepository;
    }

    public String createTag(String name, Requester requester) {
        log.info("create tag name: {}", name);
        if(requester.isGuest()){
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return createTagRepository.createTag(name);
    }
}
