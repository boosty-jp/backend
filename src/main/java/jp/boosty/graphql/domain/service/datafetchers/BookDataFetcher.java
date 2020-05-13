package jp.boosty.graphql.domain.service.datafetchers;

import jp.boosty.graphql.application.converter.book.BookBaseInputConverter;
import jp.boosty.graphql.application.converter.book.BookResponseConverter;
import jp.boosty.graphql.application.converter.book.BookTargetsInputConverter;
import jp.boosty.graphql.application.converter.requester.RequesterConverter;
import jp.boosty.graphql.application.converter.search.SearchConditionConverter;
import jp.boosty.graphql.application.datamodel.request.book.BookBaseInput;
import jp.boosty.graphql.application.datamodel.request.book.BookTargetsInput;
import jp.boosty.graphql.application.datamodel.request.search.SearchConditionInput;
import jp.boosty.graphql.application.datamodel.request.user.Requester;
import jp.boosty.graphql.application.datamodel.response.mutation.CreateResponse;
import jp.boosty.graphql.application.datamodel.response.query.book.BookListResponse;
import jp.boosty.graphql.domain.service.book.BookMutationService;
import jp.boosty.graphql.domain.service.book.BookQueryService;
import jp.boosty.graphql.domain.service.user.UserQueryService;
import jp.boosty.graphql.infrastructure.datamodel.book.BookListEntity;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import graphql.schema.DataFetcher;

@Component
public class BookDataFetcher {

    @Qualifier("bookMutation")
    private final BookMutationService bookMutationService;
    private final BookQueryService bookQueryService;
    private final UserQueryService userQueryService;
    private final RequesterConverter requesterConverter;

    public BookDataFetcher(
        BookMutationService bookMutationService,
        BookQueryService bookQueryService,
        UserQueryService userQueryService,
        RequesterConverter requesterConverter) {
        this.bookMutationService = bookMutationService;
        this.bookQueryService = bookQueryService;
        this.userQueryService = userQueryService;
        this.requesterConverter = requesterConverter;
    }

    public DataFetcher bookDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            return BookResponseConverter.toBookResponse(bookQueryService.findBook(bookId, requester), true );
        };
    }

    public DataFetcher editBookDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            return BookResponseConverter.toBookResponse(bookQueryService.findBook(bookId, requester),
                                                        userQueryService.canSale(requester));
        };
    }

    public DataFetcher pageIdToReadDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            return bookQueryService.findPageIdToRead(bookId, requester);
        };
    }

    public DataFetcher createdBooksDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            BookListEntity result = bookQueryService.findCreatedBook(userId, searchConditionInput);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher createdBooksBySelfDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            BookListEntity result = bookQueryService.findCreatedBooksBySelf(requester, searchConditionInput);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher ownBooksDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            int page = dataFetchingEnvironment.getArgument("page");

            BookListEntity result = bookQueryService.findOwnBooks(requester, page);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForOwnList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher searchedBooksDataFetcher() {
        return dataFetchingEnvironment -> {
            String query = dataFetchingEnvironment.getArgument("query");
            int page = dataFetchingEnvironment.getArgument("page");

            BookListEntity result = bookQueryService.findSearchedBooks(query, page);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForOwnList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher recentlyViewedBooksDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            BookListEntity result = bookQueryService.findRecentlyViewedBooks(requester);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForOwnList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher newBooksDataFetcher() {
        return dataFetchingEnvironment -> {
            int page = dataFetchingEnvironment.getArgument("page");

            BookListEntity result = bookQueryService.findNewBooks(page);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForOwnList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher famousBooksDataFetcher() {
        return dataFetchingEnvironment -> {
            int page = dataFetchingEnvironment.getArgument("page");

            BookListEntity result = bookQueryService.findFamousBooks(page);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForOwnList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher famousFreeBooksDataFetcher() {
        return dataFetchingEnvironment -> {
            int page = dataFetchingEnvironment.getArgument("page");

            BookListEntity result = bookQueryService.findFamousFreeBooks(page);

            return BookListResponse.builder()
                                   .books(result.getBooks()
                                                .stream()
                                                .map(r -> BookResponseConverter.toBookResponseForOwnList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher paymentIntentDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            return bookQueryService.findPaymentIntent(bookId, requester);
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher createBookDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String id = bookMutationService.create(requester);
            return CreateResponse.builder().id(id).build();
        };
    }

    public DataFetcher updateBookBaseDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            BookBaseInput bookBaseInput = BookBaseInputConverter.toBookBaseInput(dataFetchingEnvironment);

            bookMutationService.updateBase(bookId, bookBaseInput, requester);
            return true;
        };
    }

    public DataFetcher updateBookImageUrlDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            String imageUrl = dataFetchingEnvironment.getArgument("imageUrl");

            bookMutationService.updateImageUrl(bookId, imageUrl, requester);
            return true;
        };
    }

    public DataFetcher updateBookFeaturesDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            List<String> features = dataFetchingEnvironment.getArgument("features");

            bookMutationService.updateFeatures(bookId, features, requester);
            return true;
        };
    }

    public DataFetcher updateBookTargetsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            BookTargetsInput targetsInput = BookTargetsInputConverter.toBookTargetsInput(dataFetchingEnvironment);

            bookMutationService.updateTargets(bookId, targetsInput, requester);
            return true;
        };
    }

    public DataFetcher updateBookTagsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            List<String> tags = dataFetchingEnvironment.getArgument("tags");

            bookMutationService.updateTags(bookId, tags, requester);
            return true;
        };
    }

    public DataFetcher addSectionTagsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            String title = dataFetchingEnvironment.getArgument("title");

            String id = bookMutationService.addSection(bookId, title, requester);
            return CreateResponse.builder().id(id).build();
        };
    }

    public DataFetcher deleteSectionTagsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");

            bookMutationService.deleteSection(bookId, sectionId, requester);
            return true;
        };
    }

    public DataFetcher updateSectionTitleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            String sectionTitle = dataFetchingEnvironment.getArgument("title");

            bookMutationService.updateSectionTitle(bookId, sectionId, sectionTitle, requester);
            return true;
        };
    }

    public DataFetcher reorderSectionsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            List<String> sectionIds = dataFetchingEnvironment.getArgument("sectionIds");

            bookMutationService.reorderSections(bookId, sectionIds, requester);
            return true;
        };
    }

    public DataFetcher reorderPagesDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");
            List<String> pageIds = dataFetchingEnvironment.getArgument("pageIds");

            bookMutationService.reorderPages(bookId, sectionId, pageIds, requester);
            return true;
        };
    }

    public DataFetcher createPageDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            String sectionId = dataFetchingEnvironment.getArgument("sectionId");

            String id = bookMutationService.createPage(bookId, sectionId, requester);
            return CreateResponse.builder().id(id).build();
        };
    }

    public DataFetcher deleteBookDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            bookMutationService.delete(bookId, requester);
            return true;
        };
    }

    public DataFetcher publishBookDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            bookMutationService.publish(bookId, requester);
            return true;
        };
    }

    public DataFetcher suspendBookDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            bookMutationService.suspend(bookId, requester);
            return true;
        };
    }

    public DataFetcher purchaseBookDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String paymentIntentId = dataFetchingEnvironment.getArgument("paymentIntentId");

            bookMutationService.purchase(paymentIntentId, requester);
            return true;
        };
    }

    public DataFetcher addBookShelfDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            bookMutationService.addBookShelf(bookId, requester);
            return true;
        };
    }
}
