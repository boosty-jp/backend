package co.jp.wever.graphql.infrastructure.datamodel.test;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestEntity {
    private TestBaseEntity base;
    private List<QuestionEntity> questions;
    private UserEntity author;
}
