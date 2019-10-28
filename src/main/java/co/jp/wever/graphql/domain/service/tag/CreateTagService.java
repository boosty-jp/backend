package co.jp.wever.graphql.domain.service.tag;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.infrastructure.repository.tag.CreateTagRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.tag.FindTagRepositoryImpl;

@Service
public class CreateTagService {
    private final FindTagRepositoryImpl findTagRepository;
    private final CreateTagRepositoryImpl createTagRepository;

    public CreateTagService(
        FindTagRepositoryImpl findTagRepository, CreateTagRepositoryImpl createTagRepository) {
        this.findTagRepository = findTagRepository;
        this.createTagRepository = createTagRepository;
    }

    public String createTag(String name) {
        return createTagRepository.createTag(name);
    }
}
