package jp.boosty.graphql.domain.repository.payment;

import com.stripe.exception.StripeException;

import org.springframework.stereotype.Repository;

import jp.boosty.graphql.infrastructure.datamodel.payment.PaymentEntity;

@Repository
public interface PaymentRepository {
    String findDashboardLink(String stripeId) throws StripeException;

    String createPaymentIntent(int price, String stripeId, String bookId, String userId) throws StripeException;

    PaymentEntity findOne(String paymentIntentId) throws StripeException;
}
