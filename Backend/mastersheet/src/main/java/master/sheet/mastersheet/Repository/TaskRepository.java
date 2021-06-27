package master.sheet.mastersheet.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long>{
    Optional<TaskEntity> findByTaskId(String task_id);
    Boolean existsByTaskId(String task_id);
}
