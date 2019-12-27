package co.jp.wever.graphql.domain.service.tag;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.tag.TagName;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.tag.TagMutationRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TagMutationService {
    private final TagMutationRepositoryImpl createTagRepository;

    public TagMutationService(TagMutationRepositoryImpl createTagRepository) {
        this.createTagRepository = createTagRepository;
    }

    public String createTag(String name, Requester requester) {
        log.info("create tag name: {}", name);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        TagName  tagName = TagName.of(name);
        return createTagRepository.createTag(tagName);
    }
}
