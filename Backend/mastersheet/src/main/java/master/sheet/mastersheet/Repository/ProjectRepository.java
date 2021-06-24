package master.sheet.mastersheet.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.ProjectEntity;
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long>{
    
}
