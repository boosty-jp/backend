package jp.boosty.backend.domain.repository.user;

import com.stripe.exception.StripeException;

import jp.boosty.backend.domain.domainmodel.user.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserMutationRepository {
    String createOne(String displayName, String imageUrl, String uid);

    void deleteUser(String userId);

    void updateOne(User user);

    void registerStripe(String userId, String code) throws StripeException;
}
