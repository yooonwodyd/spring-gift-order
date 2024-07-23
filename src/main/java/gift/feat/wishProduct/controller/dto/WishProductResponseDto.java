package gift.feat.wishProduct.controller.dto;

import gift.feat.product.contoller.dto.ProductResponseDto;
import gift.feat.wishProduct.domain.WishProduct;

public record WishProductResponseDto(
	ProductResponseDto product
) {
	static public WishProductResponseDto from(WishProduct wishProduct) {
		return new WishProductResponseDto(ProductResponseDto.from(wishProduct.getProduct()));
	}
}
