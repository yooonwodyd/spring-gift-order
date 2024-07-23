package gift.feat.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.core.exception.product.DuplicateOptionNameException;
import gift.core.exception.product.ProductNotFoundException;
import gift.feat.product.contoller.dto.request.OptionRequest;
import gift.feat.product.contoller.dto.request.ProductCreateRequest;
import gift.feat.product.contoller.dto.response.OptionResponse;
import gift.feat.product.domain.Option;
import gift.feat.product.domain.Product;
import gift.feat.product.repository.OptionJpaRepository;
import gift.feat.product.repository.ProductJpaRepository;

@Service
public class OptionService {
	private final OptionJpaRepository optionRepository;
	private final ProductJpaRepository productRepository;

	public OptionService(OptionJpaRepository optionRepository, ProductJpaRepository productRepository) {
		this.optionRepository = optionRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public Long addOptions(Long id, List<OptionRequest> optionRequests) {
		// 상품이 존재하는지 확인
		Product product = getProduct(id);
		checkOptions(id, optionRequests);

		// 옵션 저장
		optionRequests.stream()
			.map(optionRequest -> Option.of(optionRequest.name(), optionRequest.quantity(), product))
			.forEach(option -> {
				optionRepository.save(option);
			});

		return product.getId();
	}

	@Transactional
	public OptionResponse getOption(Long id) {
		Option option = optionRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException(id));
		return OptionResponse.from(option);
	}

	@Transactional
	public List<OptionResponse> getOptions(Long productId) {
		List<Option> options = optionRepository.findAllByProductId(productId);
		return options.stream()
			.map(OptionResponse::from)
			.toList();
	}

	@Transactional
	public Long updateOptions(Long id, OptionRequest optionRequest) {
		Option option = optionRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException(id));

		option.setName(optionRequest.name());
		option.setQuantity(optionRequest.quantity());

		return optionRepository.save(option).getId();
	}

	@Transactional
	public Long deleteOption(Long id) {
		optionRepository.deleteById(id);
		return id;
	}

	private Product getProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException(id));
		return product;
	}

	private void checkOptions(Long id, List<OptionRequest> optionRequests) {
		List<Option> options = optionRepository.findAllByProductId(id);

		// List<OptionRequest>에서 중복되는 optionRequest.name이 중복되는 값이 있는지 확인
		optionRequests.stream()
			.forEach(optionRequest -> {
				if (optionRequests.stream().filter(request -> request.name().equals(optionRequest.name())).count() > 1) {
					throw new DuplicateOptionNameException(optionRequest.name());
				}
			});


		// 이미 존재하는 옵션이라면 에러 반환
		optionRequests.stream()
			.forEach(optionRequest -> {
				if (options.stream().anyMatch(option -> option.getName().equals(optionRequest.name()))) {
					throw new DuplicateOptionNameException(optionRequest.name());
				}
			});
	}
}
