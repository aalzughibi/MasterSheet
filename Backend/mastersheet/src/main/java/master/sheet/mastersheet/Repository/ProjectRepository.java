package master.sheet.mastersheet.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.ProjectEntity;
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long>{
    // Optional<ProjectEntity> findByProject_id(String project_id);
    Optional<ProjectEntity> findByProjectId(String project_id);
    Boolean existsByProjectId(String project_id);

}