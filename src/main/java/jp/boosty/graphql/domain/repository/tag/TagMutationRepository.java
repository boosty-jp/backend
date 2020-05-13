package jp.boosty.graphql.domain.repository.tag;

import org.springframework.stereotype.Repository;

import jp.boosty.graphql.domain.domainmodel.tag.TagName;

@Repository
public interface TagMutationRepository {
    String createTag(TagName name);
}
