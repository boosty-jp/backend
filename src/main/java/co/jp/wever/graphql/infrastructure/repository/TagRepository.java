package co.jp.wever.graphql.infrastructure.repository;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.datamodel.Section;

@Repository
public interface TagRepository{
    //Mutation
    void followOne(String id);
}
