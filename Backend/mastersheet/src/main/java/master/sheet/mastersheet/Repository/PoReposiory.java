package master.sheet.mastersheet.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.PoEntity;
@Repository
public interface PoReposiory extends JpaRepository<PoEntity,Long>{
    
}
