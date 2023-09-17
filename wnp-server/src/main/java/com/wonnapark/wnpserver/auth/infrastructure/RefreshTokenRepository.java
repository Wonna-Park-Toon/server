package com.wonnapark.wnpserver.auth.infrastructure;

import com.wonnapark.wnpserver.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
