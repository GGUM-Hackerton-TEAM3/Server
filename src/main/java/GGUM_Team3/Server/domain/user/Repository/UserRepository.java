package GGUM_Team3.Server.domain.user.Repository;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findById(String id);

}
