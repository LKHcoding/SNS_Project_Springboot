package com.cos.instagram.config.auth;

import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(PrincipalDetailsService.class);
	private final UserRepository userRepository;
	private final HttpSession session;

	// Security Session > Authentication > UserDetails
	// 해당 함수가 정상적으로 리턴되면 @AuthenticationPricipal 어노테이션 활성화됨.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername : username : " + username);

		/**
		 * LKH - User정보가 널이면 값을 가져올때 nullpointException이 발생하는데 여기까지 User자체가 빈값인지 아닌지에 대한
		 * 에러가 처리되어 있지 않았음. 나머지 소스는 건드리지않고 새로 User정보를 가져와서 널인지 비교하고 강제로 예외처리되도록 처리함.
		 */
		Optional<User> searchNullUser = userRepository.findByUsername(username);
		log.info("searchUser : " + searchNullUser);

		if (searchNullUser.equals(Optional.empty())) {
			throw new UsernameNotFoundException(username + "is not found.");
		}

		User userEntity = userRepository.findByUsername(username).map(new Function<User, User>() {
			@Override
			public User apply(User t) {
				session.setAttribute("loginUser", new LoginUser(t));
				return t;
			}
		}).orElse(null);
		return new PrincipalDetails(userEntity);
	}

}
