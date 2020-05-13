package jp.boosty.backend.domain.repository.tag;

import org.springframework.stereotype.Repository;

import jp.boosty.backend.domain.domainmodel.tag.TagName;

@Repository
public interface TagMutationRepository {
    String createTag(TagName name);
}
