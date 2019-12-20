package co.jp.wever.graphql.domain.service.section;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.section.FindSectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.FindSection;
import co.jp.wever.graphql.domain.repository.section.FindSectionRepository;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.section.LikedSectionEntity;
import co.jp.wever.graphql.infrastructure.repository.article.ArticleQueryRepositoryImpl;

@Service
public class FindSectionService {

    private final ArticleQueryRepositoryImpl findArticleRepository;
    private final FindSectionRepository findSectionRepository;

    public FindSectionService(
        FindSectionRepository findSectionRepository, ArticleQueryRepositoryImpl findArticleRepository) {
        this.findArticleRepository = findArticleRepository;
        this.findSectionRepository = findSectionRepository;
    }

    public List<FindSection> findAllSectionsOnArticle(String articleId, Requester requester) {
        // TODO: ステータスだけ見たいので別の軽いクエリにしたい
        ArticleDetailEntity articleDetailEntity = findArticleRepository.findOneForGuest(articleId);
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(articleDetailEntity);

        if (!articleDetail.canRead(requester)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return findSectionRepository.findAllDetailOnArticle(articleId, requester.getUserId())
                                    .stream()
                                    .map(s -> FindSectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }

    public List<LikedSectionEntity> findAllLikedSections(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        return findSectionRepository.findAllLiked(requester.getUserId());
    }

    public List<FindSection> findAllBookmarkedSections(String userId) {
        return findSectionRepository.findAllBookmarked(userId)
                                    .stream()
                                    .map(s -> FindSectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }

    public List<FindSection> findFamousSections() {
        return findSectionRepository.findFamous()
                                    .stream()
                                    .map(s -> FindSectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }

    public List<FindSection> findRelatedSections(Requester requester) {
        return findSectionRepository.findRelated(requester.getUserId())
                                    .stream()
                                    .map(s -> FindSectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }
}
