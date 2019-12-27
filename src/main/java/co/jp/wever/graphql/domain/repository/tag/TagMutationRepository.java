package co.jp.wever.graphql.domain.repository.tag;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.domain.domainmodel.tag.TagName;

@Repository
public interface TagMutationRepository {
    String createTag(TagName name);
}
