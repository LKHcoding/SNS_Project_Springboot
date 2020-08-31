package com.cos.instagram.config.oauth;

import java.util.function.Supplier;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.instagram.config.auth.PrincipalDetails;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.domain.user.UserRole;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService{

	private static final Logger log = LoggerFactory.getLogger(PrincipalOAuth2UserService.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private HttpSession session;
	
	@Value("${cos.secret}")
	private String cosSecret;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info(userRequest.getAccessToken().getTokenValue());
		log.info(userRequest.getClientRegistration().toString());
		
		// super.loadUser() 는 OAuth 서버에 내 서버정보와 AccessToken을 던져서
		// 회원 프로필 정보를 OAuth2User타입으로 받아온다.
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("OAuth2User : "+oAuth2User.getAttributes());
		
		User userEntity = oauthLoginOrJoin(oAuth2User);
		
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
	
	private User oauthLoginOrJoin(OAuth2User oAuth2User) {

		String provider = "facebook";
		String providerId = oAuth2User.getAttribute("id");
		String username = provider+"_"+providerId;
		// 추가함
		String name = oAuth2User.getAttribute("name");
		String password = bCryptPasswordEncoder.encode(cosSecret);
		String email = oAuth2User.getAttribute("email");
		
		// login할 때도 사실 OAuth에서 가져오는 정보이기 때문에 회원정보를 매번 업데이트 해주는 것이 좋다.
		// 이때는 orElseGet전에 .map을 사용해서 update해주면 된다.
		
		User userEntity = 
				userRepository.findByUsername(username)
				.orElseGet(new Supplier<User>() {
					@Override
					public User get() {
						//회원가입
						User user = User.builder()
								.username(username)
								.name(name)
								.password(password)
								.email(email)
								.role(UserRole.USER)
								.provider(provider)
								.providerId(providerId)
								.build();
						return userRepository.save(user);
					}
				});
		
		session.setAttribute("loginUser", new LoginUser(userEntity));
		return userEntity;	
	}
}
