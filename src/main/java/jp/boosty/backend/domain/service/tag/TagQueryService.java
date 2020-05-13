package jp.boosty.backend.domain.service.tag;

import org.springframework.stereotype.Service;

import java.util.List;

import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;
import jp.boosty.backend.infrastructure.repository.tag.TagQueryRepositoryImpl;

@Service
public class TagQueryService {
    private final TagQueryRepositoryImpl findTagRepository;

    public TagQueryService(TagQueryRepositoryImpl findTagRepository) {
        this.findTagRepository = findTagRepository;
    }

    public List<TagEntity> famousTags() {
       return findTagRepository.famousTags();
    }
}
