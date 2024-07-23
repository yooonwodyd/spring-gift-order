package gift.feat.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.feat.user.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}