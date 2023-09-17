package com.wonnapark.wnpserver.user.infrastructure;

import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderIdAndPlatform(String providerId, OAuthProvider platform);

    @Query(value = "select * from users u where u.is_deleted is not null", nativeQuery = true)
    List<User> findWithdrewUser();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from users u where u.is_deleted is not null", nativeQuery = true)
    void deleteAllWithdrewUser();
}
