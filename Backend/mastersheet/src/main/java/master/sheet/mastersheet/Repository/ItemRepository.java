package master.sheet.mastersheet.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import master.sheet.mastersheet.Entity.ItemEntity;
@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
    Optional<ItemEntity> findByItemId(String Item_id);
    Boolean existsByItemId(String Item_id);
}
