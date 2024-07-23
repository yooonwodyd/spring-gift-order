package gift.feat.wishProduct.controller.dto;

import gift.feat.product.contoller.dto.response.ProductResponse;
import gift.feat.wishProduct.domain.WishProduct;

public record WishProductResponseDto(
	ProductResponse product
) {
	static public WishProductResponseDto from(WishProduct wishProduct) {
		return new WishProductResponseDto(ProductResponse.from(wishProduct.getProduct()));
	}
}
