package co.jp.wever.graphql.domain.domainmodel.content;

import org.springframework.http.HttpStatus;

import java.util.Date;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ContentDate {
    private java.util.Date createDate;
    private java.util.Date updateDate;

    private ContentDate(java.util.Date createDate, java.util.Date updateDate) {
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static ContentDate of(Date createDate, Date updateDate) {
        if (createDate.after(updateDate)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return new ContentDate(createDate, updateDate);
    }

    public java.util.Date getCreateDate() {
        return createDate;
    }

    public long getUnixCreateDate() {
        return createDate.getTime();
    }

    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    public long getUnixUpdateDate() {
        return updateDate.getTime();
    }
}
