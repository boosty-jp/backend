package jp.boosty.backend.domain.service.datafetchers;

import jp.boosty.backend.application.converter.page.LikePageResponseConverter;
import jp.boosty.backend.application.converter.page.PageInputConverter;
import jp.boosty.backend.application.converter.page.PageResponseConverter;
import jp.boosty.backend.application.converter.requester.RequesterConverter;
import jp.boosty.backend.application.converter.search.SearchConditionConverter;
import jp.boosty.backend.application.datamodel.request.page.PageInput;
import jp.boosty.backend.application.datamodel.request.search.SearchConditionInput;
import jp.boosty.backend.application.datamodel.request.user.Requester;
import jp.boosty.backend.application.datamodel.response.query.page.LikedPageListResponse;
import jp.boosty.backend.domain.service.page.PageMutationService;
import jp.boosty.backend.domain.service.page.PageQueryService;
import jp.boosty.backend.infrastructure.datamodel.page.LikedPageListEntity;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import jp.boosty.backend.application.datamodel.response.query.page.PageListResponse;
import jp.boosty.backend.infrastructure.datamodel.page.PageListEntity;

import graphql.schema.DataFetcher;

@Component
public class PageDataFetcher {

    private final PageQueryService pageQueryService;
    private final PageMutationService pageMutationService;
    private final RequesterConverter requesterConverter;

    public PageDataFetcher(
        PageQueryService pageQueryService,
        PageMutationService pageMutationService,
        RequesterConverter requesterConverter) {
        this.pageQueryService = pageQueryService;
        this.pageMutationService = pageMutationService;
        this.requesterConverter = requesterConverter;
    }

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////

    public DataFetcher pageDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String pageId = dataFetchingEnvironment.getArgument("pageId");
            String bookId = dataFetchingEnvironment.getArgument("bookId");

            return PageResponseConverter.toPageResponse(pageQueryService.findPage(bookId, pageId, requester));
        };
    }

    public DataFetcher pageToEditDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String pageId = dataFetchingEnvironment.getArgument("pageId");

            return PageResponseConverter.toPageResponse(pageQueryService.findPageToEdit(pageId, requester));
        };
    }

    public DataFetcher createdPagesDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            PageListEntity result = pageQueryService.findCreatedPages(userId, searchConditionInput);

            return PageListResponse.builder()
                                   .pages(result.getPages()
                                                .stream()
                                                .map(r -> PageResponseConverter.toPageResponseForList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher createdPagesBySelfDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            SearchConditionInput searchConditionInput =
                SearchConditionConverter.toSearchCondition(dataFetchingEnvironment);

            PageListEntity result = pageQueryService.findCreatedPagesBySelf(requester, searchConditionInput);

            return PageListResponse.builder()
                                   .pages(result.getPages()
                                                .stream()
                                                .map(r -> PageResponseConverter.toPageResponseForList(r))
                                                .collect(Collectors.toList()))
                                   .sumCount(result.getSumCount());
        };
    }

    public DataFetcher LikedPagesDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            int page = dataFetchingEnvironment.getArgument("page");
            LikedPageListEntity result = pageQueryService.findLikedPages(requester, page);

            return LikedPageListResponse.builder()
                                        .pages(result.getLikedPages()
                                                     .stream()
                                                     .map(r -> LikePageResponseConverter.toLikePageResponse(r))
                                                     .collect(Collectors.toList()))
                                        .sumCount(result.getSumCount());
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher savePageDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String pageId = dataFetchingEnvironment.getArgument("pageId");
            String bookId = dataFetchingEnvironment.getArgument("bookId");
            PageInput pageInput = PageInputConverter.toPageInput(dataFetchingEnvironment);

            pageMutationService.save(bookId, pageId, pageInput, requester);
            return true;
        };
    }

    public DataFetcher deletePageDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String pageId = dataFetchingEnvironment.getArgument("pageId");
            pageMutationService.delete(pageId, requester);

            return true;
        };
    }

    public DataFetcher likePageDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String pageId = dataFetchingEnvironment.getArgument("pageId");
            pageMutationService.like(pageId, requester);

            return true;
        };
    }

    public DataFetcher unLikePageDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String pageId = dataFetchingEnvironment.getArgument("pageId");
            pageMutationService.unLike(pageId, requester);

            return true;
        };
    }

    public DataFetcher updatePageTrialReadDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String pageId = dataFetchingEnvironment.getArgument("pageId");
            boolean canPreview = dataFetchingEnvironment.getArgument("canPreview");
            pageMutationService.updateTrialRead(pageId, canPreview, requester);

            return true;
        };
    }
}
