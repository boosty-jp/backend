package jp.boosty.backend.domain.service;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.service.datafetchers.BookDataFetcher;
import jp.boosty.backend.domain.service.datafetchers.PageDataFetcher;
import jp.boosty.backend.domain.service.datafetchers.TagDataFetcher;
import jp.boosty.backend.domain.service.datafetchers.UserDataFetcher;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphQLService {

    private GraphQL graphQL;

    @Autowired
    private BookDataFetcher bookDataFetcher;

    @Autowired
    private PageDataFetcher pageDataFetcher;

    @Autowired
    private UserDataFetcher userDataFetcher;

    @Autowired
    private TagDataFetcher tagDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        // getFileはだとDaemon実行時のjavaコマンドで使えないのであえてInputStreamを使う
        InputStream is = new ClassPathResource("schema.graphql").getInputStream();
        File file = File.createTempFile("tempSchema", ".graphql");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } catch (IOException e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(file);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                            .type("Query",
                                  typeWiring -> typeWiring.dataFetcher("page", pageDataFetcher.pageDataFetcher())
                                                          .dataFetcher("pageToEdit",
                                                                       pageDataFetcher.pageToEditDataFetcher())
                                                          .dataFetcher("likedPages",
                                                                       pageDataFetcher.LikedPagesDataFetcher())
                                                          .dataFetcher("createdPages",
                                                                       pageDataFetcher.createdPagesDataFetcher())
                                                          .dataFetcher("createdPagesBySelf",
                                                                       pageDataFetcher.createdPagesBySelfDataFetcher())
                                                          .dataFetcher("book", bookDataFetcher.bookDataFetcher())
                                                          .dataFetcher("editBook",
                                                                       bookDataFetcher.editBookDataFetcher())
                                                          .dataFetcher("pageIdToRead",
                                                                       bookDataFetcher.pageIdToReadDataFetcher())
                                                          .dataFetcher("createdBooks",
                                                                       bookDataFetcher.createdBooksDataFetcher())
                                                          .dataFetcher("createdBooksBySelf",
                                                                       bookDataFetcher.createdBooksBySelfDataFetcher())
                                                          .dataFetcher("ownBooks",
                                                                       bookDataFetcher.ownBooksDataFetcher())
                                                          .dataFetcher("searchedBooks",
                                                                       bookDataFetcher.searchedBooksDataFetcher())
                                                          .dataFetcher("recentlyViewedBooks",
                                                                       bookDataFetcher.recentlyViewedBooksDataFetcher())
                                                          .dataFetcher("newBooks",
                                                                       bookDataFetcher.newBooksDataFetcher())
                                                          .dataFetcher("allNewBooks",
                                                                       bookDataFetcher.allNewBooksDataFetcher())
                                                          .dataFetcher("famousBooks",
                                                                       bookDataFetcher.famousBooksDataFetcher())
                                                          .dataFetcher("famousFreeBooks",
                                                                       bookDataFetcher.famousFreeBooksDataFetcher())
                                                          .dataFetcher("likedBooks",
                                                                       bookDataFetcher.likedBooksDataFetcher())
                                                          .dataFetcher("user", userDataFetcher.userDataFetcher())
                                                          .dataFetcher("account", userDataFetcher.accountDataFetcher())
                                                          .dataFetcher("canSale", userDataFetcher.canSaleDataFetcher())
                                                          .dataFetcher("paymentIntent",
                                                                       bookDataFetcher.paymentIntentDataFetcher())
                                                          .dataFetcher("salesLink",
                                                                       userDataFetcher.salesLinkDataFetcher())
                                                          .dataFetcher("orderHistories",
                                                                       userDataFetcher.orderHistoriesDataFetcher())
                                                          .dataFetcher("famousTags",
                                                                       tagDataFetcher.famousTagDataFetcher()))
                            .type("Mutation",
                                  typeWiring -> typeWiring.dataFetcher("savePage",
                                                                       pageDataFetcher.savePageDataFetcher())
                                                          .dataFetcher("deletePage",
                                                                       pageDataFetcher.deletePageDataFetcher())
                                                          .dataFetcher("likePage",
                                                                       pageDataFetcher.likePageDataFetcher())
                                                          .dataFetcher("unLikePage",
                                                                       pageDataFetcher.unLikePageDataFetcher())
                                                          .dataFetcher("updatePageTrialRead",
                                                                       pageDataFetcher.updatePageTrialReadDataFetcher())
                                                          .dataFetcher("reorderPages",
                                                                       bookDataFetcher.reorderPagesDataFetcher())
                                                          .dataFetcher("createBook",
                                                                       bookDataFetcher.createBookDataFetcher())
                                                          .dataFetcher("updateBookCoverImage",
                                                                       bookDataFetcher.updateBookImageUrlDataFetcher())
                                                          .dataFetcher("updateBookBase",
                                                                       bookDataFetcher.updateBookBaseDataFetcher())
                                                          .dataFetcher("updateBookFeatures",
                                                                       bookDataFetcher.updateBookFeaturesDataFetcher())
                                                          .dataFetcher("updateBookTargets",
                                                                       bookDataFetcher.updateBookTargetsDataFetcher())
                                                          .dataFetcher("updateBookTags",
                                                                       bookDataFetcher.updateBookTagsDataFetcher())
                                                          .dataFetcher("updateBookMeaningful",
                                                                       bookDataFetcher.updateBookMeaningful())
                                                          .dataFetcher("purchaseBook",
                                                                       bookDataFetcher.purchaseBookDataFetcher())
                                                          .dataFetcher("addBookShelf",
                                                                       bookDataFetcher.addBookShelfDataFetcher())
                                                          .dataFetcher("addSection",
                                                                       bookDataFetcher.addSectionTagsDataFetcher())
                                                          .dataFetcher("updateSectionTitle",
                                                                       bookDataFetcher.updateSectionTitleDataFetcher())
                                                          .dataFetcher("createPage",
                                                                       bookDataFetcher.createPageDataFetcher())
                                                          .dataFetcher("deleteSection",
                                                                       bookDataFetcher.deleteSectionTagsDataFetcher())
                                                          .dataFetcher("reorderSections",
                                                                       bookDataFetcher.reorderSectionsDataFetcher())
                                                          .dataFetcher("publishBook",
                                                                       bookDataFetcher.publishBookDataFetcher())
                                                          .dataFetcher("suspendBook",
                                                                       bookDataFetcher.suspendBookDataFetcher())
                                                          .dataFetcher("deleteBook",
                                                                       bookDataFetcher.deleteBookDataFetcher())
                                                          .dataFetcher("likeBook",
                                                                       bookDataFetcher.likeBookDataFetcher())
                                                          .dataFetcher("unLikeBook",
                                                                       bookDataFetcher.unLikeBookDataFetcher())
                                                          .dataFetcher("createUser",
                                                                       userDataFetcher.createUserDataFetcher())
                                                          .dataFetcher("updateUser",
                                                                       userDataFetcher.updateUserDataFetcher())
                                                          .dataFetcher("deleteUser",
                                                                       userDataFetcher.deleteUserDataFetcher())
                                                          .dataFetcher("registerStripe",
                                                                       userDataFetcher.registerStripeDataFetcher()))
                            .build();
    }

    @Bean
    public GraphQL getGraphQL() {
        return graphQL;
    }
}
