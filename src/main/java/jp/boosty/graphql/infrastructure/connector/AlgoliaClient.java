package jp.boosty.graphql.infrastructure.connector;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;

import jp.boosty.graphql.infrastructure.datamodel.algolia.PageSearchEntity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jp.boosty.graphql.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.graphql.infrastructure.datamodel.algolia.BookSearchEntity;

@Component
public class AlgoliaClient {
    private SearchClient client;
    private SearchIndex<PageSearchEntity> pageIndex;
    private SearchIndex<BookSearchEntity> bookIndex;

    public AlgoliaClient(
        @Value("${algolia.applicationId}") String applicationId, @Value("${algolia.adminKey}") String adminApiKey) {
        this.client = DefaultSearchClient.create(applicationId, adminApiKey);
        this.bookIndex = client.initIndex(VertexLabel.BOOK.getString(), BookSearchEntity.class);
        this.pageIndex = client.initIndex(VertexLabel.PAGE.getString(), PageSearchEntity.class);
    }

    public SearchIndex<PageSearchEntity> getPageIndex() {
        return pageIndex;
    }

    public SearchIndex<BookSearchEntity> getBookIndex() {
        return bookIndex;
    }
}
