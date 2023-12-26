package gr.hua.distSys.backend.BusinessManagement.repository;

import gr.hua.distSys.backend.BusinessManagement.entity.BusinessRequest;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Hidden
public interface BusinessRequestRepository extends JpaRepository<BusinessRequest, Integer> {
    List<BusinessRequest> findAllByStateOfRequest(String state);

    Boolean existsByAfm(String afm);
}
