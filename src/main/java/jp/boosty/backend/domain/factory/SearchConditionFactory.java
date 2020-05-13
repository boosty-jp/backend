package jp.boosty.backend.domain.factory;

import jp.boosty.backend.application.datamodel.request.search.SearchConditionInput;
import jp.boosty.backend.domain.domainmodel.search.SearchCondition;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;

public class SearchConditionFactory {
    public static SearchCondition make(SearchConditionInput input) {
        return SearchCondition.of(input.getFilter(),
                                  input.getSortField(),
                                  input.getSortOrder(),
                                  input.getPage(),
                                  input.getResultCount());
    }

    public static SearchCondition makeFilteredPublished(SearchConditionInput input) {
        return SearchCondition.of(EdgeLabel.PUBLISH.getString(),
                                  input.getSortField(),
                                  input.getSortOrder(),
                                  input.getPage(),
                                  input.getResultCount());
    }
}
