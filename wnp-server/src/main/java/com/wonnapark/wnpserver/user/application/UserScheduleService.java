package com.wonnapark.wnpserver.user.application;

import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserScheduleService {

    private final UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteWithdrewUser() {
        List<User> withdrewUsers = userRepository.findWithdrewUser();

        if (!withdrewUsers.isEmpty()) {
            userRepository.deleteAllWithdrewUser();
        }
    }
}
