package project.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springsecurity.entity.User;

@Repository
public interface RoleRepository extends JpaRepository<User, Long> {
}