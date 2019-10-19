package co.jp.wever.graphql.domain.service.tag;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.infrastructure.repository.tag.CreateTagRepositoryImpl;

@Service
public class CreateTagService {
    private final CreateTagRepositoryImpl createTagRepository;

    public CreateTagService(CreateTagRepositoryImpl createTagRepository) {
        this.createTagRepository = createTagRepository;
    }


    public String createTag(String name) {
        return createTagRepository.createTag(name);
    }
}
