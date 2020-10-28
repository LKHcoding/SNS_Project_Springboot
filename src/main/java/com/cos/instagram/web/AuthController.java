package com.cos.instagram.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.service.UserService;
import com.cos.instagram.web.dto.JoinReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final UserService userService;

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		log.info("/auth/loginForm 진입");
		return "auth/loginForm";
	}

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		log.info("/auth/joinForm 진입");
		return "auth/joinForm";
	}

	// 회원가입 처리하는 부분
	@PostMapping("/auth/join")
	public String join(JoinReqDto joinReqDto) {
		log.info(joinReqDto.toString());
		userService.회원가입(joinReqDto);
		return "redirect:/auth/loginForm";
	}

	@GetMapping("/auth/pwChange")
	public String pwChange(@LoginUserAnnotation LoginUser loginUser, Model model) {
		log.info("/auth/pwChange 진입 ");
		User userEntity = userService.회원정보(loginUser);
		model.addAttribute("user", userEntity);

		return "/auth/pwChange";
	}

	// 비밀번호 변경 요청
	@PostMapping(value = "/auth/pwChange", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String pwChange(@LoginUserAnnotation LoginUser loginUser, String oldPassword, String newPassword,
			String newRePassword, JoinReqDto joinReqDto, HttpSession session, Model model) throws Exception {

		log.info("비밀번호 변경 요청 발생!!", joinReqDto.toString());

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		User user = userService.회원정보(loginUser);

		if (encoder.matches(oldPassword, user.getPassword())==true) {
			System.out.println("oldPassword and DBpassword matches: "+encoder.matches(oldPassword, user.getPassword()));

			if (newPassword.equals(newRePassword)) {
				System.out.println("변경 할 비밀번호 일치!");

				joinReqDto.setPassword(newPassword);

				userService.비밀번호변경(joinReqDto);
				// 비밀번호 성공 시 다시 로그인 세션 객체에 담음
				JoinReqDto modifyUser = new JoinReqDto();
				modifyUser.setUsername(joinReqDto.getUsername());

				System.out.println("비밀번호 변경 완료!!");
			}
		} else {
			System.out.println("oldPassword and DBpassword matches: "+encoder.matches(oldPassword, user.getPassword()));
			System.out.println("비밀번호 변경 실패!!");
		}

		return "redirect:/";
	}
}
