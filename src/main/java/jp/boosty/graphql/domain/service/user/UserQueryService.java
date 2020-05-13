package jp.boosty.graphql.domain.service.user;

import com.stripe.exception.StripeException;

import jp.boosty.graphql.application.datamodel.request.user.Requester;
import jp.boosty.graphql.infrastructure.repository.payment.PaymentRepositoryImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.graphql.infrastructure.datamodel.user.OrderHistoriesEntity;
import jp.boosty.graphql.infrastructure.datamodel.user.UserEntity;
import jp.boosty.graphql.infrastructure.repository.user.UserQueryRepositoryImpl;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserQueryService {
    private final UserQueryRepositoryImpl userQueryRepository;
    private final PaymentRepositoryImpl paymentRepository;

    public UserQueryService(
        UserQueryRepositoryImpl userQueryRepository, PaymentRepositoryImpl paymentRepository) {
        this.userQueryRepository = userQueryRepository;
        this.paymentRepository = paymentRepository;
    }

    public UserEntity findUser(String userId) {
        log.info("find user: {}", userId);
        if (userId.isEmpty()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.USER_ID_EMPTY.getString());
        }

        UserEntity userEntity;
        //        UserEntity userEntity = userQueryRepository.findOne(userId);
        try {
            userEntity = userQueryRepository.findOne(userId);
        } catch (NoSuchElementException e) {
            throw new GraphQLCustomException(HttpStatus.NOT_FOUND.value(),
                                             GraphQLErrorMessage.USER_NOT_FOUND.getString());
        }

        if (userEntity.getDeleted()) {
            throw new GraphQLCustomException(HttpStatus.NOT_FOUND.value(),
                                             GraphQLErrorMessage.USER_NOT_FOUND.getString());
        }
        return userEntity;
    }

    public UserEntity findAccount(Requester requester) {
        log.info("find account: {}", requester.getUserId());
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserEntity userEntity = userQueryRepository.findOne(requester.getUserId());
        if (userEntity.getDeleted()) {
            throw new GraphQLCustomException(HttpStatus.NOT_FOUND.value(),
                                             GraphQLErrorMessage.USER_NOT_FOUND.getString());
        }
        return userEntity;
    }

    public String findSalesLink(Requester requester) {
        log.info("find sales link: {}", requester.getUserId());
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        String stripeId = userQueryRepository.findOne(requester.getUserId()).getStripeId();

        if (StringUtil.isNullOrEmpty(stripeId)) {
            return "";
        }

        try {
            return paymentRepository.findDashboardLink(stripeId);
        } catch (StripeException e) {
            log.error(e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    public OrderHistoriesEntity findOrderHistories(int page, Requester requester) {
        log.info("find orderHistories: {} {}", page, requester.getUserId());
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        return userQueryRepository.findOrderHistories(page, requester.getUserId());
    }

    public boolean canSale(Requester requester) {
        log.info("can sale: {}", requester.getUserId());
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        String stripeId = userQueryRepository.findOne(requester.getUserId()).getStripeId();

        return !StringUtil.isNullOrEmpty(stripeId);
    }
}
