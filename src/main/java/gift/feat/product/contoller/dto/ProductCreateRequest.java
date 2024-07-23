package gift.feat.product.contoller.dto;

import gift.core.annotaions.NoKakao;
import gift.core.annotaions.ValidCharset;
import gift.core.exception.ValidationMessage;
import gift.feat.product.domain.Category;
import gift.feat.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
	@Size(max = 15, message = ValidationMessage.INVALID_SIZE_MSG)
	@ValidCharset
	@NoKakao
	String name,
	Long price,
	String imageUrl,
	@NotNull(message = ValidationMessage.NOT_NULL_MSG)
	String categoryName
) {
	public Product toEntity(Category category) {
		return Product.of(name, price, imageUrl, category);
	}
}
