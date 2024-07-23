package gift.feat.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gift.feat.product.domain.Option;

public interface OptionJpaRepository extends JpaRepository<Option, Long> {
	Optional<Option> findByNameAndProductId(String name, Long productId);
	List<Option> findAllByProductId(Long id);
}
