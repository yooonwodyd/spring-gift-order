package gift.feat.user.service;

import java.nio.file.FileAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.core.exception.user.PasswordNotMatchException;
import gift.core.exception.user.UserAlreadyExistsException;
import gift.core.exception.user.UserNotFoundException;
import gift.core.jwt.JwtProvider;
import gift.feat.user.User;
import gift.feat.user.dto.SignupRequestDto;
import gift.feat.user.repository.UserJpaRepository;

@Service
public class UserService {
	private final UserJpaRepository userJpaRepository;
	private final JwtProvider jwtProvider;

	@Autowired
	public UserService(UserJpaRepository userJpaRepository, JwtProvider jwtProvider) {
		this.userJpaRepository = userJpaRepository;
		this.jwtProvider = jwtProvider;
	}

	@Transactional(readOnly = true)
	public String checkEmailAndPassword(String email, String password) {
		User user =
			userJpaRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

		if (!user.getPassword().equals(password)) {
			throw new PasswordNotMatchException();
		}
		return jwtProvider.generateToken(user.getId().toString(), user.getRole());
	}

	@Transactional
	public void registerUser(SignupRequestDto userDto) {
		userJpaRepository.findByEmail(userDto.email()).ifPresent(
			(eUser) -> {
				throw new UserAlreadyExistsException(eUser.getEmail());
			}
		);
		userJpaRepository.save(userDto.toEntity());
	}

	@Transactional(readOnly = true)
	public User getUser(Long userId) {
		return userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
	}
}
