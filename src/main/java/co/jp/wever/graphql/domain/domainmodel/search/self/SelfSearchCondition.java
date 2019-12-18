package co.jp.wever.graphql.domain.domainmodel.search.self;

import co.jp.wever.graphql.domain.domainmodel.search.SearchOrder;
import co.jp.wever.graphql.domain.domainmodel.search.SearchPage;
import co.jp.wever.graphql.domain.domainmodel.search.SearchResultCount;
import co.jp.wever.graphql.domain.domainmodel.search.SearchSortField;

public class SelfSearchCondition {

    private SelfSearchFilter filter;
    private SearchSortField field;
    private SearchOrder order;
    private SearchPage page;
    private SearchResultCount resultCount;

    public SelfSearchCondition(
        SelfSearchFilter filter,
        SearchSortField field,
        SearchOrder order,
        SearchPage page,
        SearchResultCount resultCount) {
        this.filter = filter;
        this.field = field;
        this.order = order;
        this.page = page;
        this.resultCount = resultCount;
    }

    public static SelfSearchCondition of(String filter, String field, String order, int page, int resultCount) {
        return new SelfSearchCondition(SelfSearchFilter.of(filter),
                                       SearchSortField.of(field),
                                       SearchOrder.of(order),
                                       SearchPage.of(page),
                                       SearchResultCount.of(resultCount));
    }

    public SelfSearchFilter getFilter() {
        return filter;
    }

    public SearchSortField getField() {
        return field;
    }

    public SearchOrder getOrder() {
        return order;
    }

    public SearchPage getPage() {
        return page;
    }

    public SearchResultCount getResultCount() {
        return resultCount;
    }
}
