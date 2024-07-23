package gift.feat.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.core.exception.product.CategoryNotFoundException;
import gift.feat.product.contoller.dto.response.ProductResponse;
import gift.feat.product.domain.Category;
import gift.feat.product.domain.Option;
import gift.feat.product.domain.Product;
import gift.feat.product.domain.SearchType;
import gift.feat.product.contoller.dto.request.ProductCreateRequest;
import gift.core.exception.product.DuplicateProductIdException;
import gift.core.exception.product.ProductNotFoundException;
import gift.feat.product.repository.CategoryJpaRepository;
import gift.feat.product.repository.OptionJpaRepository;
import gift.feat.product.repository.ProductJpaRepository;

import java.util.List;

@Service
public class ProductService {
	private final ProductJpaRepository productRepository;
	private final CategoryJpaRepository categoryRepository;
	private final OptionJpaRepository optionRepository;

	public ProductService(ProductJpaRepository productRepository, CategoryJpaRepository categoryRepository,
		OptionJpaRepository optionRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.optionRepository = optionRepository;
	}

	//DB에 해당 카테고리가 존재하는지, 같은 이름으로 생성된 상품이 이미 존재하는지 확인한 뒤 상품을 저장한다.
	@Transactional
	public Long saveProduct(ProductCreateRequest productCreateRequest) {
		Category category = categoryRepository.findByName(productCreateRequest.categoryName())
			.orElseThrow(() -> new CategoryNotFoundException(productCreateRequest.categoryName()));

		productRepository.findByName(productCreateRequest.name())
			.ifPresent(product -> {
				throw new DuplicateProductIdException(productCreateRequest.name());
			});

		Product product = Product.of(productCreateRequest.name(), productCreateRequest.price(), productCreateRequest.imageUrl(), category);
		return productRepository.save(product).getId();
	}

	// 상품 ID로 상품을 찾아 반환한다. 상품이 존재하지 않으면 ProductNotFoundException을 발생시킨다.
	@Transactional(readOnly = true)
	public Product getProductById(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException(id));
		return product;
	}

	@Transactional(readOnly = true)
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Page<ProductResponse> getProductsWithPaging(Pageable pageable, SearchType searchType, String searchValue) {
		if (searchValue == null || searchValue.isBlank()) {
			return productRepository.findAll(pageable).map(ProductResponse::from);
		}
		return switch (searchType) {
			case NAME -> productRepository.findByNameContaining(searchValue, pageable).map(ProductResponse::from);
			case CATEGORY -> productRepository.findByCategoryName(searchValue, pageable).map(ProductResponse::from);
		};
	}

	// 변경하려는 상품 이름이 이미 존재하는지 확인한다. 카테고리 또한 확인한다.
	@Transactional
	public Long updateProduct(Long id, ProductCreateRequest productCreateRequest) {
		Category category = categoryRepository.findByName(productCreateRequest.categoryName())
			.orElseThrow(() -> new CategoryNotFoundException(productCreateRequest.categoryName()));

		Product existingProduct = productRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException(id));

		existingProduct.setName(productCreateRequest.name());
		existingProduct.setPrice(productCreateRequest.price());
		existingProduct.setImageUrl(productCreateRequest.imageUrl());
		existingProduct.setCategory(category);

		return productRepository.save(existingProduct).getId();
	}

	// 상품 ID로 상품을 찾아 삭제한다. 상품이 존재하지 않으면 ProductNotFoundException을 발생시킨다.
	@Transactional
	public void deleteProduct(Long id) {
		Product existingProduct = productRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException(id));
		productRepository.deleteById(id);
	}
}