package master.sheet.mastersheet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long>{
    
}
