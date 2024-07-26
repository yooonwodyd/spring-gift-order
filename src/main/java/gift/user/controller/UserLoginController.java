package gift.user.controller;

import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.core.api.kakao.KakaoRestClient;
import gift.dto.request.LoginRequest;
import gift.dto.request.SignupRequest;
import gift.user.service.OAuthservice;
import gift.user.service.UserService;

@RestController
@RequestMapping("/api/user/v1")
public class UserLoginController {
	private final UserService userService;
	private final OAuthservice oAuthService;

	public UserLoginController(UserService userService, OAuthservice oAuthService, KakaoRestClient kakaoRestClient,
		CacheManager cacheManager) {
		this.userService = userService;
		this.oAuthService = oAuthService;
	}

	// email과 패스워드를 입력하면, 해당 유저의 정보를 JWT access token으로 반환
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest loginRequest) {
		return userService.loginUser(loginRequest.email(), loginRequest.password());
	}

	@PostMapping("/signup")
	public void signup(@RequestBody SignupRequest signupRequest) {
		userService.registerUser(signupRequest);
	}

	@GetMapping("/auth/oauth2/kakao")
	public ResponseEntity<String> login(@RequestParam("code") String code) {
		String email = oAuthService.authenticate(code);
		String token = userService.loginOauth2User(email);
		return ResponseEntity.ok(token);
	}
}
