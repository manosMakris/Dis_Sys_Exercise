package gr.hua.distSys.backend.BusinessManagement.repository;

import gr.hua.distSys.backend.BusinessManagement.entity.Role;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String roleName);

    Boolean existsByName(String name);
}
