package id.muhadif.spring.authmanagement.repository;

import id.muhadif.spring.authmanagement.entity.Role;
import id.muhadif.spring.authmanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}