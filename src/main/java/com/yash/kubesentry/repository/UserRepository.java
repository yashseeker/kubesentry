package com.yash.kubesentry.repository;

import com.yash.kubesentry.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // read what is this
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameOrEmail(
            String username,
            String email
    );

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
};