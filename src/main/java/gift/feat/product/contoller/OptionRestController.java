package gift.feat.product.contoller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import gift.feat.product.contoller.dto.request.OptionRequest;
import gift.feat.product.contoller.dto.request.ProductCreateRequest;
import gift.feat.product.service.OptionService;
import gift.feat.product.service.ProductService;
import jakarta.validation.Valid;

@Controller
public class OptionRestController {
	private final OptionService optionService;
	private final ProductService productService;

	// 상품 옵션 확인 메서드
	public OptionRestController(OptionService optionService, ProductService productService) {
		this.optionService = optionService;
		this.productService = productService;
	}

	// 상품 옵션 추가 메서드
	@PostMapping("/api/v1/product/{id}/option")
	@ResponseBody
	public ResponseEntity<?> addOption(@PathVariable Long id, @RequestBody @Valid List<OptionRequest> optionRequests) {
		Long l = optionService.addOptions(id, optionRequests);
		return ResponseEntity.ok(l);
	}

	// 상품 옵션 삭제 메서드
	@PostMapping("/api/v1/product/{id}/option/{optionId}")
	@ResponseBody
	public ResponseEntity<?> deleteOption(@PathVariable Long id, @PathVariable Long optionId) {
		optionService.deleteOption(optionId);
		return ResponseEntity.ok().build();
	}
}
