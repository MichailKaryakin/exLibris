package org.example.exlibris.user.repository;

import lombok.NonNull;
import org.example.exlibris.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
