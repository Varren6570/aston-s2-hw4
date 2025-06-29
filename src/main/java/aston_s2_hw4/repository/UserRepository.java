package aston_s2_hw4.repository;

import aston_s2_hw4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
