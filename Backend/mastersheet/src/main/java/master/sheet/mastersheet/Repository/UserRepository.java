package master.sheet.mastersheet.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import master.sheet.mastersheet.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUid(String uid);
	Boolean existsByUid(String uid);
	Boolean existsByEmail(String email);
}
