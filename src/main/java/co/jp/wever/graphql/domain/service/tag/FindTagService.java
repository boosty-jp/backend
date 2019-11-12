package co.jp.wever.graphql.domain.service.tag;

import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.tag.TagStatisticEntity;
import co.jp.wever.graphql.infrastructure.repository.tag.FindTagRepositoryImpl;

@Service
public class FindTagService {
    private final FindTagRepositoryImpl findTagRepository;

    public FindTagService(FindTagRepositoryImpl findTagRepository) {
        this.findTagRepository = findTagRepository;
    }

    public List<TagStatisticEntity> famousTags() {
       return findTagRepository.famousTags();
//        return results.stream()
//                      .sorted((r1, r2) -> Long.compare(r2.getRelatedCount(), r1.getRelatedCount()))
//                      .limit()
//                      .collect(Collectors.toList());
    }
}
