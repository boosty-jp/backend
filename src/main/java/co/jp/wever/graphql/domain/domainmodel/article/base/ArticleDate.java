package co.jp.wever.graphql.domain.domainmodel.article.base;

import org.springframework.http.HttpStatus;

import java.util.Date;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ArticleDate {

    private Date createDate;
    private Date updateDate;

    private ArticleDate(Date createDate, Date updateDate) {
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static ArticleDate of(Date createDate, Date updateDate) {
        if (createDate.after(updateDate)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return new ArticleDate(createDate, updateDate);
    }

    public Date getCreateDate() {
        return createDate;
    }

    public long getUnixCreateDate() {
        return createDate.getTime();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public long getUnixUpdateDate() {
        return updateDate.getTime();
    }
}
