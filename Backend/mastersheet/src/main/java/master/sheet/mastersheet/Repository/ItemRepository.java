package master.sheet.mastersheet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.ItemEntity;
@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
    
}
