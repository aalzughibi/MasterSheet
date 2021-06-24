package master.sheet.mastersheet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
    
}
