package master.sheet.mastersheet.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.PoEntity;
@Repository
public interface PoReposiory extends JpaRepository<PoEntity,Long>{
    Optional<PoEntity> findByPoId(String po_id);
    Boolean existsByPoId(String po_id);
}
