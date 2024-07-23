package gift.feat.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import gift.feat.product.contoller.dto.response.ProductResponse;
import gift.feat.product.service.ProductService;

import gift.feat.user.repository.UserJpaRepository;
import gift.feat.wishProduct.repository.WishProductJpaRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	private final ProductService productService;
	private final WishProductJpaRepository wishListRepository;
	private final UserJpaRepository userRepository;

	@GetMapping("/admin/product")
	public String viewProducts(Model model) {
		List<ProductResponse> products = productService.getAllProducts().stream()
			.map(ProductResponse::from)
			.toList();
		model.addAttribute("products", products);
		return "admin/productPage";
	}


	@GetMapping("/admin/user/wishlist/{userId}")
	public String viewWishList(Model model,@PathVariable Long userId) {
		List<ProductResponse> products = wishListRepository.findByUserId(userId).stream()
			.map(wishProduct -> ProductResponse.from(wishProduct.getProduct()))
			.toList();
		model.addAttribute("products", products);
		return "admin/wishListPage";
	}

}
