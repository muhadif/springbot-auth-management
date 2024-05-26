package id.muhadif.spring.authmanagement.repository;

import id.muhadif.spring.authmanagement.entity.User;
import id.muhadif.spring.authmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}