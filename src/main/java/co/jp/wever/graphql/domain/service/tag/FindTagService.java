package co.jp.wever.graphql.domain.service.tag;

import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import co.jp.wever.graphql.infrastructure.repository.tag.FindTagRepositoryImpl;

@Service
public class FindTagService {
    private final FindTagRepositoryImpl findTagRepository;

    public FindTagService(FindTagRepositoryImpl findTagRepository) {
        this.findTagRepository = findTagRepository;
    }

    public List<TagEntity> famousTags() {
       return findTagRepository.famousTags();
    }
}
