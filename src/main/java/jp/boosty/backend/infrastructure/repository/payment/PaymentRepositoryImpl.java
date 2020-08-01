package jp.boosty.backend.infrastructure.repository.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.LoginLink;
import com.stripe.model.PaymentIntent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.boosty.backend.domain.repository.payment.PaymentRepository;
import jp.boosty.backend.infrastructure.constant.payment.PaymentData;
import jp.boosty.backend.infrastructure.datamodel.payment.PaymentEntity;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {
    @Value("${stripe.apiKey}")
    private String STRIPE_API_KEY;

    @Override
    public String findDashboardLink(String stripeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Map<String, Object> params = new HashMap<>();
        return LoginLink.createOnAccount(stripeId, params, null).getUrl();
    }

    @Override
    public String createPaymentIntent(int price, String stripeId, String bookId, String userId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        ArrayList paymentMethodTypes = new ArrayList();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("payment_method_types", paymentMethodTypes);
        params.put("amount", price);
        params.put("currency", "jpy");

        // 本棚追加時のチェック情報を入れる
        Map<String, String> metadata = new HashMap<String, String>();
        metadata.put(PaymentData.BOOK_ID.getString(), bookId);
        metadata.put(PaymentData.PURCHASE_USER_ID.getString(), userId);
        params.put("metadata", metadata);

        Map<String, Object> transferDataParams = new HashMap<String, Object>();
        transferDataParams.put("amount", (int) Math.floor(price * 0.8));
        transferDataParams.put("destination", stripeId);
        params.put("transfer_data", transferDataParams);
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret();
    }

    @Override
    public PaymentEntity findOne(String paymentIntentId) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        return PaymentEntity.builder()
                            .amount(paymentIntent.getAmount().intValue())
                            .bookId(paymentIntent.getMetadata().get(PaymentData.BOOK_ID.getString()))
                            .purchaseUserId(paymentIntent.getMetadata().get(PaymentData.PURCHASE_USER_ID.getString()))
                            .status(paymentIntent.getStatus())
                            .build();
    }
}
