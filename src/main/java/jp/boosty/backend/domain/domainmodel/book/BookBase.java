package jp.boosty.backend.domain.domainmodel.book;

import jp.boosty.backend.domain.domainmodel.content.ContentTitle;
import jp.boosty.backend.infrastructure.datamodel.user.UserEntity;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

import io.netty.util.internal.StringUtil;

public class BookBase {
    private ContentTitle title;
    private BookDescription description;
    private BookPrice price;

    private BookBase(
        ContentTitle title, BookDescription description, BookPrice price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public static BookBase of(ContentTitle title, BookDescription description, BookPrice price) {
        if (Objects.isNull(title) || Objects.isNull(description) || Objects.isNull(price)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_BOOK_DATA.getString());
        }

        return new BookBase(title, description, price);
    }

    public void publishValidation() {
        if (!title.hasValue()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_BOOK_TITLE.getString());
        }
    }

    public ContentTitle getTitle() {
        return title;
    }

    public BookDescription getDescription() {
        return description;
    }

    public BookPrice getPrice() {
        return price;
    }

    public boolean canSale(UserEntity userEntity) {
        if (price.getValue() == 0)
            return true;

        return !StringUtil.isNullOrEmpty(userEntity.getStripeId());
    }
}
