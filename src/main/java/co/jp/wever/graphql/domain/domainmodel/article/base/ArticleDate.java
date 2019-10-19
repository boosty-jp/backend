package co.jp.wever.graphql.domain.domainmodel.article.base;

import java.util.Date;

public class ArticleDate {

    private Date createDate;
    private Date updateDate;

    private ArticleDate(Date createDate, Date updateDate) {
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static ArticleDate of(Date createDate, Date updateDate) throws IllegalArgumentException {
        if (createDate.after(updateDate)) {
            throw new IllegalArgumentException();
        }

        return new ArticleDate(createDate, updateDate);
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

}
