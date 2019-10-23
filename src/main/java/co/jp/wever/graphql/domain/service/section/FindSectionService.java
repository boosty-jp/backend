package co.jp.wever.graphql.domain.service.section;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.article.ArticleDetailConverter;
import co.jp.wever.graphql.domain.converter.section.SectionConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.domain.repository.article.FindArticleRepository;
import co.jp.wever.graphql.domain.repository.section.FindSectionRepository;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.repository.article.FindArticleRepositoryImpl;

@Service
public class FindSectionService {

    private final FindArticleRepositoryImpl findArticleRepository;
    private final FindSectionRepository findSectionRepository;

    public FindSectionService(
        FindSectionRepository findSectionRepository, FindArticleRepositoryImpl findArticleRepository) {
        this.findArticleRepository = findArticleRepository;
        this.findSectionRepository = findSectionRepository;
    }

    public List<Section> findAllSectionsOnArticle(String articleId, String userId) throws IllegalAccessException {
        // TODO: ステータスだけ見たいので別の軽いクエリにしたい
        ArticleDetailEntity articleDetailEntity = findArticleRepository.findOne(articleId);
        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(articleDetailEntity);

        if (!articleDetail.canRead(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        return findSectionRepository.findAllDetailOnArticle(articleId)
                                    .stream()
                                    .map(s -> SectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }

    public List<Section> findAllLikedSections(String userId) {
        return findSectionRepository.findAllLiked(userId)
                                    .stream()
                                    .map(s -> SectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }

    public List<Section> findAllBookmarkedSections(String userId) {
        return findSectionRepository.findAllBookmarked(userId)
                                    .stream()
                                    .map(s -> SectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }

    public List<Section> findFamousSections() {
        return findSectionRepository.findFamous()
                                    .stream()
                                    .map(s -> SectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }

    public List<Section> findRelatedSections(String userId) {
        return findSectionRepository.findRelated(userId)
                                    .stream()
                                    .map(s -> SectionConverter.toSection(s))
                                    .collect(Collectors.toList());
    }
}
